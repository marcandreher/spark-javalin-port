package spark;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.staticfiles.Location;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Spark {
    Logger logger = Logger.getLogger("SparkJavalinBridge");

    private static Spark instance;
    private Javalin javalin;
    private StaticFiles staticFiles = new StaticFiles();
    private int port = 7070;
    private boolean initialized = false;

    private Spark() {
        logger.info("Spark Javalin Bridge is igniting");
    }

    public static synchronized Spark getInstance() {
        if (instance == null) {
            instance = new Spark();
        }
        return instance;
    }

    public static void port(int port) {
        getInstance().setPort(port);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void get(String path, Route route) {
        getInstance().addRoute("GET", path, route);
    }

    public static void post(String path, Route route) {
        getInstance().addRoute("POST", path, route);
    }

    public static void put(String path, Route route) {
        getInstance().addRoute("PUT", path, route);
    }

    public static void delete(String path, Route route) {
        getInstance().addRoute("DELETE", path, route);
    }

    public static void patch(String path, Route route) {
        getInstance().addRoute("PATCH", path, route);
    }

    public static void options(String path, Route route) {
        getInstance().addRoute("OPTIONS", path, route);
    }

    public static void head(String path, Route route) {
        getInstance().addRoute("HEAD", path, route);
    }

    public static StaticFiles staticFiles() {
        return getInstance().staticFiles;
    }

    public static void before(String path, Filter filter) {
        getInstance().addFilter("BEFORE", path, filter);
    }

    public static void before(Filter filter) {
        getInstance().addFilter("BEFORE", "/*", filter);
    }

    public static void after(String path, Filter filter) {
        getInstance().addFilter("AFTER", path, filter);
    }

    public static void after(Filter filter) {
        getInstance().addFilter("AFTER", "/*", filter);
    }

    public static <T extends Exception> void exception(Class<T> exceptionClass, ExceptionHandler<T> handler) {
        getInstance().addExceptionHandler(exceptionClass, handler);
    }

    public static void notFound(Route route) {
        getInstance().addNotFoundHandler(route);
    }

    public static void internalServerError(Route route) {
        getInstance().addInternalServerErrorHandler(route);
    }

    public static void stop() {
        getInstance().stopServer();
    }

    public static void awaitInitialization() {
        getInstance().initializeServer();
    }

    private void addRoute(String method, String path, Route route) {
        ensureInitialized();
        
        // Convert Spark-style path parameters (:param) to Javalin-style ({param})
        String javalinPath = convertSparkPathToJavalinPath(path);
        
        Handler handler = ctx -> {
            Request request = new Request(ctx);
            Response response = new Response(ctx);
            Object result = route.handle(request, response);
            if (result != null && !response.isRedirected()) {
                ctx.result(result.toString());
            }
        };

        switch (method.toUpperCase()) {
            case "GET" -> javalin.get(javalinPath, handler);
            case "POST" -> javalin.post(javalinPath, handler);
            case "PUT" -> javalin.put(javalinPath, handler);
            case "DELETE" -> javalin.delete(javalinPath, handler);
            case "PATCH" -> javalin.patch(javalinPath, handler);
            case "OPTIONS" -> javalin.options(javalinPath, handler);
            case "HEAD" -> javalin.head(javalinPath, handler);
        }
    }

    /**
     * Converts Spark-style path parameters (:param) to Javalin-style ({param})
     */
    private String convertSparkPathToJavalinPath(String sparkPath) {
        // Replace :param with {param}
        return sparkPath.replaceAll(":([^/]+)", "{$1}");
    }

    private void addFilter(String type, String path, Filter filter) {
        ensureInitialized();
        
        // Convert Spark-style path parameters (:param) to Javalin-style ({param})
        String javalinPath = convertSparkPathToJavalinPath(path);
        
        Handler handler = ctx -> {
            Request request = new Request(ctx);
            Response response = new Response(ctx);
            filter.handle(request, response);
        };

        if ("BEFORE".equals(type)) {
            javalin.before(javalinPath, handler);
        } else if ("AFTER".equals(type)) {
            javalin.after(javalinPath, handler);
        }
    }

    private <T extends Exception> void addExceptionHandler(Class<T> exceptionClass, ExceptionHandler<T> handler) {
        ensureInitialized();
        javalin.exception(exceptionClass, (exception, ctx) -> {
            Request request = new Request(ctx);
            Response response = new Response(ctx);
            handler.handle(exception, request, response);
        });
    }

    private void addNotFoundHandler(Route route) {
        ensureInitialized();
        javalin.error(404, ctx -> {
            Request request = new Request(ctx);
            Response response = new Response(ctx);
            Object result = route.handle(request, response);
            if (result != null) {
                ctx.result(result.toString());
            }
        });
    }

    private void addInternalServerErrorHandler(Route route) {
        ensureInitialized();
        javalin.error(500, ctx -> {
            Request request = new Request(ctx);
            Response response = new Response(ctx);
            Object result = route.handle(request, response);
            if (result != null) {
                ctx.result(result.toString());
            }
        });
    }

    private void ensureInitialized() {
        if (!initialized) {
            initializeServer();
        }
    }

    public synchronized void initializeServer() {
        if (!initialized) {
            javalin = Javalin.create(config -> {
                if (staticFiles.externalLocation != null) {
                    // Check if the directory exists before configuring static files
                    if (Files.exists(Paths.get(staticFiles.externalLocation))) {
                        config.staticFiles.add(staticFiles -> {
                            staticFiles.directory = this.staticFiles.externalLocation;
                            staticFiles.location = Location.EXTERNAL;
                        });
                    } else {
                        System.out.println("Warning: Static file directory '" + staticFiles.externalLocation + "' does not exist. Skipping static file configuration.");
                    }
                }
            }).start(port);
            initialized = true;
        }
    }

    private void stopServer() {
        if (javalin != null) {
            javalin.stop();
            initialized = false;
        }
    }
}

package spark;

import static spark.Spark.*;

/**
 * Example demonstrating how to use the Spark-Javalin wrapper
 */
public class SparkExample {

    public static void main(String[] args) {
        // Set the port
        port(8080);

        // Configure static files to serve from an external directory
        staticFiles().externalLocation("public");

        // Add a before filter that runs before all routes
        before("/*", (request, response) -> {
            System.out.println("Before filter: " + request.requestMethod() + " " + request.pathInfo());
        });

        // Simple GET route
        get("/hello", (request, response) -> {
            return "Hello World!";
        });

        // GET route with path parameter
        get("/hello/:name", (request, response) -> {
            return "Hello " + request.params(":name") + "!";
        });

        // GET route with query parameter
        get("/search", (request, response) -> {
            String query = request.queryParams("q");
            if (query != null) {
                return "Searching for: " + query;
            } else {
                return "No search query provided";
            }
        });

        // POST route with JSON response
        post("/api/users", (request, response) -> {
            response.type("application/json");
            String name = request.queryParams("name");
            String email = request.queryParams("email");
            
            if (name != null && email != null) {
                response.status(201);
                return "{ \"id\": 1, \"name\": \"" + name + "\", \"email\": \"" + email + "\" }";
            } else {
                response.status(400);
                return "{ \"error\": \"Name and email are required\" }";
            }
        });

        // PUT route
        put("/api/users/:id", (request, response) -> {
            String id = request.params(":id");
            response.type("application/json");
            return "{ \"message\": \"User " + id + " updated\" }";
        });

        // DELETE route
        delete("/api/users/:id", (request, response) -> {
            String id = request.params(":id");
            response.status(204);
            return id;
        });

        // Route with session handling
        get("/session", (request, response) -> {
            Integer count = request.session("count");
            if (count == null) {
                count = 0;
            }
            count++;
            request.session("count", count);
            return "Session count: " + count;
        });

        // Route that sets a cookie
        get("/cookie", (request, response) -> {
            response.cookie("myCookie", "cookieValue");
            return "Cookie set!";
        });

        // Route that reads a cookie
        get("/read-cookie", (request, response) -> {
            String cookieValue = request.cookie("myCookie");
            if (cookieValue != null) {
                return "Cookie value: " + cookieValue;
            } else {
                return "No cookie found";
            }
        });

        // Route that redirects
        get("/redirect", (request, response) -> {
            response.redirect("/hello");
            return null;
        });

        // Add an after filter that runs after all routes
        after("/*", (request, response) -> {
            System.out.println("After filter: Response status " + response.status());
        });

        // Exception handling
        exception(IllegalArgumentException.class, (exception, request, response) -> {
            response.status(400);
            response.body("Bad request: " + exception.getMessage());
        });

        // 404 handling
        notFound((request, response) -> {
            response.status(404);
            return "Page not found: " + request.pathInfo();
        });

        // 500 handling
        internalServerError((request, response) -> {
            response.status(500);
            return "Internal server error";
        });

        // Initialize the server
        awaitInitialization();
        
        System.out.println("Server started on http://localhost:8080");
        System.out.println("Static files will be served from the 'public' directory");
        System.out.println("Visit http://localhost:8080/ to see the static index.html page");
        System.out.println("");
        System.out.println("Try these API endpoints:");
        System.out.println("  GET  http://localhost:8080/hello");
        System.out.println("  GET  http://localhost:8080/hello/YourName");
        System.out.println("  GET  http://localhost:8080/search?q=test");
        System.out.println("  POST http://localhost:8080/api/users?name=John&email=john@example.com");
        System.out.println("  GET  http://localhost:8080/session");
        System.out.println("  GET  http://localhost:8080/cookie");
        System.out.println("  GET  http://localhost:8080/read-cookie");
    }
}

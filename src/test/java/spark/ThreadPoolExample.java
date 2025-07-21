package spark;

import static spark.Spark.*;

/**
 * Example demonstrating thread pool configuration
 */
public class ThreadPoolExample {

    public static void main(String[] args) {
        // Configure thread pool BEFORE any routes or initialization
        threadPool(2, 8, 30000); // min: 2, max: 8, timeout: 30 seconds
        
        // Set the port
        port(8080);
        
        // Optional: set IP address
        // ipAddress("127.0.0.1");

        // Configure static files to serve from an external directory
        staticFiles().externalLocation("public");

        // Add a route that simulates some work
        get("/work", (request, response) -> {
            // Simulate some work
            Thread.sleep(1000);
            return "Work completed by thread: " + Thread.currentThread().getName();
        });

        // Simple GET route
        get("/hello", (request, response) -> {
            return "Hello from thread: " + Thread.currentThread().getName();
        });

        // Route to check thread info
        get("/thread-info", (request, response) -> {
            response.type("application/json");
            return "{ \"thread\": \"" + Thread.currentThread().getName() + 
                   "\", \"threadGroup\": \"" + Thread.currentThread().getThreadGroup().getName() + "\" }";
        });

        // Initialize the server
        awaitInitialization();
        
        System.out.println("Server started on http://localhost:8080");
        System.out.println("Thread pool configured: min=2, max=8, timeout=30s");
        System.out.println("");
        System.out.println("Try these endpoints to see different threads:");
        System.out.println("  GET  http://localhost:8080/hello");
        System.out.println("  GET  http://localhost:8080/work");
        System.out.println("  GET  http://localhost:8080/thread-info");
    }
}

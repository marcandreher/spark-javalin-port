package spark;

/**
 * A Route is built up by a path (for url-matching) and the implementation of the 'handle' method.
 * When a request is made, if present, the matching route's handle method is invoked.
 */
@FunctionalInterface
public interface Route {

    /**
     * Invoked when a request is made on this route's corresponding path e.g. '/hello'
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     * @return The content to be set in the response
     * @throws Exception when handle fails
     */
    Object handle(Request request, Response response) throws Exception;

}

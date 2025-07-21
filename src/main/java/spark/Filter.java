package spark;

/**
 * A Filter is a piece of code that is executed before or after a Route.
 */
@FunctionalInterface
public interface Filter {

    /**
     * Invoked when a request is made on this filter's corresponding path
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     * @throws Exception when filter fails
     */
    void handle(Request request, Response response) throws Exception;

}

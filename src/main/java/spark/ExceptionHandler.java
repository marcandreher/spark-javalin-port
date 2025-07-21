package spark;

/**
 * Exception handler interface
 */
@FunctionalInterface
public interface ExceptionHandler<T extends Exception> {

    /**
     * Invoked when an exception occurs
     *
     * @param exception the exception that occurred
     * @param request   The request object providing information about the HTTP request
     * @param response  The response object providing functionality for modifying the response
     */
    void handle(T exception, Request request, Response response);

}

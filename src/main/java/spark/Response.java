package spark;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

/**
 * Response wrapper that provides Spark-like API over Javalin's Context
 */
public class Response {

    private final Context context;
    private boolean redirected = false;

    public Response(Context context) {
        this.context = context;
    }

    /**
     * Sets the status code for the response
     */
    public void status(int statusCode) {
        context.status(statusCode);
    }

    /**
     * Gets the status code for the response
     */
    public int status() {
        return context.status().getCode();
    }

    /**
     * Sets the content type for the response
     */
    public void type(String contentType) {
        context.contentType(contentType);
    }

    /**
     * Gets the content type for the response
     */
    public String type() {
        return context.contentType();
    }

    /**
     * Sets the body
     */
    public void body(String body) {
        context.result(body);
    }

    /**
     * Gets the body
     */
    public String body() {
        return context.result();
    }

    /**
     * Sets a response header
     */
    public void header(String header, String value) {
        context.header(header, value);
    }

    /**
     * Gets a response header
     */
    public String header(String header) {
        return context.header(header);
    }

    /**
     * Sets a cookie
     */
    public void cookie(String name, String value) {
        context.cookie(name, value);
    }

    /**
     * Sets a cookie with max age
     */
    public void cookie(String name, String value, int maxAge) {
        context.cookie(name, value, maxAge);
    }

    /**
     * Sets a cookie with path
     */
    public void cookie(String path, String name, String value) {
        context.cookie(name, value, -1);
    }

    /**
     * Sets a cookie with path and max age
     */
    public void cookie(String path, String name, String value, int maxAge) {
        context.cookie(name, value, maxAge);
    }

    /**
     * Sets a cookie with path, max age, and secured
     */
    public void cookie(String path, String name, String value, int maxAge, boolean secured) {
        context.cookie(name, value, maxAge);
    }

    /**
     * Removes a cookie
     */
    public void removeCookie(String name) {
        context.removeCookie(name);
    }

    /**
     * Removes a cookie with path
     */
    public void removeCookie(String path, String name) {
        context.removeCookie(name);
    }

    /**
     * Redirects the response to the given url
     */
    public void redirect(String location) {
        context.redirect(location);
        redirected = true;
    }

    /**
     * Redirects the response to the given url with status code
     */
    public void redirect(String location, int httpStatusCode) {
        context.redirect(location, HttpStatus.forStatus(httpStatusCode));
        redirected = true;
    }

    /**
     * @return whether the response has been redirected
     */
    public boolean isRedirected() {
        return redirected;
    }

    /**
     * Get the raw Javalin context (for advanced use)
     */
    public Context raw() {
        return context;
    }

}

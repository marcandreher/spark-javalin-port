package spark;

import io.javalin.http.Context;
import java.util.Map;
import java.util.Set;

/**
 * Request wrapper that provides Spark-like API over Javalin's Context
 */
public class Request {

    private final Context context;

    public Request(Context context) {
        this.context = context;
    }

    /**
     * Returns the map containing all route parameters
     */
    public Map<String, String> params() {
        return context.pathParamMap();
    }

    /**
     * Returns the value of the provided route parameter
     * Handles both Spark-style (:param) and Javalin-style (param) parameter names
     */
    public String params(String param) {
        // Remove leading colon if present (Spark-style :param -> param)
        String cleanParam = param.startsWith(":") ? param.substring(1) : param;
        return context.pathParam(cleanParam);
    }

    /**
     * @return request method e.g. GET, POST, PUT, ...
     */
    public String requestMethod() {
        return context.method().name();
    }

    /**
     * @return the scheme
     */
    public String scheme() {
        return context.scheme();
    }

    /**
     * @return the host
     */
    public String host() {
        return context.host();
    }

    /**
     * @return the user-agent
     */
    public String userAgent() {
        return context.userAgent();
    }

    /**
     * @return the server port
     */
    public int port() {
        return context.port();
    }

    /**
     * @return the path info
     * Example return: "/example/foo"
     */
    public String pathInfo() {
        return context.path();
    }

    /**
     * @return the servlet path
     */
    public String servletPath() {
        return context.path();
    }

    /**
     * @return the context path
     */
    public String contextPath() {
        return context.contextPath();
    }

    /**
     * @return the URL string
     */
    public String url() {
        return context.url();
    }

    /**
     * @return the content type of the body
     */
    public String contentType() {
        return context.contentType();
    }

    /**
     * @return the content length
     */
    public int contentLength() {
        return context.contentLength();
    }

    /**
     * Gets the query param
     */
    public String queryParams(String queryParam) {
        return context.queryParam(queryParam);
    }

    /**
     * Gets the query param, or returns default value
     */
    public String queryParams(String queryParam, String defaultValue) {
        String value = context.queryParam(queryParam);
        return value != null ? value : defaultValue;
    }

    /**
     * @return all query parameters
     */
    public Set<String> queryParams() {
        return context.queryParamMap().keySet();
    }

    /**
     * @return the query string
     */
    public String queryString() {
        return context.queryString();
    }

    /**
     * Gets header by name
     */
    public String headers(String header) {
        return context.header(header);
    }

    /**
     * @return all headers
     */
    public Set<String> headers() {
        return context.headerMap().keySet();
    }

    /**
     * @return the body of the request
     */
    public String body() {
        return context.body();
    }

    /**
     * @return the body of the request as bytes
     */
    public byte[] bodyAsBytes() {
        return context.bodyAsBytes();
    }

    /**
     * Gets cookie by name
     */
    public String cookie(String name) {
        return context.cookie(name);
    }

    /**
     * @return all cookies
     */
    public Set<String> cookies() {
        return context.cookieMap().keySet();
    }

    /**
     * @return the HTTP session
     */
    public Session session() {
        return new Session(context.sessionAttribute("spark-session"));
    }

    /**
     * @return true if session exists
     */
    public boolean hasSession() {
        return context.sessionAttribute("spark-session") != null;
    }

    /**
     * Gets a session attribute
     */
    public <T> T session(String attribute) {
        Session session = session();
        return session.attribute(attribute);
    }

    /**
     * Sets a session attribute
     */
    public void session(String attribute, Object value) {
        Session session = session();
        session.attribute(attribute, value);
    }

    /**
     * Get the raw Javalin context (for advanced use)
     */
    public Context raw() {
        return context;
    }

    /**
     * @return the IP address
     */
    public String ip() {
        return context.ip();
    }

    /**
     * Gets an attribute from this request
     */
    @SuppressWarnings("unchecked")
    public <T> T attribute(String attribute) {
        return (T) context.attribute(attribute);
    }

    /**
     * Sets an attribute on this request
     */
    public void attribute(String attribute, Object value) {
        context.attribute(attribute, value);
    }
}

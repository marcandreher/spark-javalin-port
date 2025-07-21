package spark;

import io.javalin.http.Context;
import java.util.Collection;
import javax.servlet.MultipartConfigElement;

/**
 * Wrapper that provides servlet-like methods for multipart handling
 * This allows existing servlet multipart code to work unchanged with Javalin
 */
public class RawRequestWrapper {
    
    private final Context context;
    private final Request sparkRequest;
    
    public RawRequestWrapper(Context context, Request sparkRequest) {
        this.context = context;
        this.sparkRequest = sparkRequest;
    }
    
    /**
     * Gets a multipart part by name (servlet-style API)
     * This allows code like: req.raw().getPart("banner")
     */
    public PartWrapper getPart(String name) {
        return sparkRequest.getPart(name);
    }
    
    /**
     * Gets all multipart parts (servlet-style API)
     * This allows code like: req.raw().getParts()
     */
    public Collection<PartWrapper> getParts() {
        return sparkRequest.getParts();
    }
    
    /**
     * Sets an attribute (servlet-style API)
     * This allows code like: req.raw().setAttribute("config", multipartConfig)
     * Special handling for multipart configuration
     */
    public void setAttribute(String name, Object value) {
        context.attribute(name, value);
        
        // Handle multipart configuration specially
        if ("org.eclipse.jetty.multipartConfig".equals(name) && value instanceof MultipartConfigElement) {
            MultipartConfigElement config = (MultipartConfigElement) value;
            // Store the config for reference (Javalin handles multipart automatically)
            // The limits will be enforced by Javalin's multipart handling
            context.attribute("multipartConfig", config);
        }
    }
    
    /**
     * Gets an attribute (servlet-style API)
     */
    public Object getAttribute(String name) {
        return context.attribute(name);
    }
    
    /**
     * Get the underlying Javalin context for advanced operations
     */
    public Context getContext() {
        return context;
    }
    
    // Delegate other common methods to context
    
    public String getMethod() {
        return context.method().name();
    }
    
    public String getRequestURI() {
        return context.path();
    }
    
    public String getQueryString() {
        return context.queryString();
    }
    
    public String getHeader(String name) {
        return context.header(name);
    }
    
    public String getContentType() {
        return context.contentType();
    }
    
    public int getContentLength() {
        return context.contentLength();
    }
}

package spark;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple session implementation
 */
public class Session {

    private Map<String, Object> attributes;

    public Session(Object existingSession) {
        if (existingSession instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) existingSession;
            this.attributes = sessionMap;
        } else {
            this.attributes = new HashMap<>();
        }
    }

    /**
     * Gets an attribute
     */
    @SuppressWarnings("unchecked")
    public <T> T attribute(String name) {
        return (T) attributes.get(name);
    }

    /**
     * Sets an attribute
     */
    public void attribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Removes an attribute
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * @return all attribute names
     */
    public Set<String> attributes() {
        return attributes.keySet();
    }

    /**
     * @return the creation time
     */
    public long creationTime() {
        Long time = attribute("creationTime");
        if (time == null) {
            time = System.currentTimeMillis();
            attribute("creationTime", time);
        }
        return time;
    }

    /**
     * @return the id
     */
    public String id() {
        String id = attribute("sessionId");
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
            attribute("sessionId", id);
        }
        return id;
    }

    /**
     * @return the last accessed time
     */
    public long lastAccessedTime() {
        Long time = attribute("lastAccessedTime");
        if (time == null) {
            time = System.currentTimeMillis();
            attribute("lastAccessedTime", time);
        }
        return time;
    }

    /**
     * @return the max inactive interval
     */
    public int maxInactiveInterval() {
        Integer interval = attribute("maxInactiveInterval");
        return interval != null ? interval : 1800; // 30 minutes default
    }

    /**
     * Sets the max inactive interval
     */
    public void maxInactiveInterval(int interval) {
        attribute("maxInactiveInterval", interval);
    }

    /**
     * Invalidates this session
     */
    public void invalidate() {
        attributes.clear();
    }

    /**
     * @return true if the session is new
     */
    public boolean isNew() {
        Boolean isNew = attribute("isNew");
        return isNew != null ? isNew : true;
    }

}

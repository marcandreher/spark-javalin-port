package spark;

import io.javalin.http.UploadedFile;
import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper class that provides servlet-like Part interface for uploaded files
 * This allows existing servlet-based multipart code to work with Javalin
 */
public class PartWrapper {
    
    private final UploadedFile uploadedFile;
    
    public PartWrapper(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    /**
     * @return the filename of the uploaded file
     */
    public String getSubmittedFileName() {
        return uploadedFile.filename();
    }
    
    /**
     * @return the size of the uploaded file in bytes
     */
    public long getSize() {
        return uploadedFile.size();
    }
    
    /**
     * @return the content type of the uploaded file
     */
    public String getContentType() {
        return uploadedFile.contentType();
    }
    
    /**
     * @return the name of the form field
     */
    public String getName() {
        return uploadedFile.filename(); // Javalin doesn't separate field name from filename
    }
    
    /**
     * @return an InputStream to read the file content
     */
    public InputStream getInputStream() throws IOException {
        return uploadedFile.content();
    }
    
    /**
     * Get the underlying Javalin UploadedFile for advanced operations
     */
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
}

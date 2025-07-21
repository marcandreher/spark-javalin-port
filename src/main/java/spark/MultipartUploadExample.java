package spark;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Example showing how to adapt servlet-based multipart code to work with Javalin-Spark bridge
 */
public class MultipartUploadExample {

    private static final String UPLOAD_DIR = "uploads";
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public static Object handleFileUpload(Request req, Response res) throws Exception {
        // Create upload directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            // Check if request contains multipart data
            if (!req.isMultipart()) {
                res.status(400);
                return "Error: No multipart data found.";
            }

            // OLD WAY (servlet-based - won't work with Javalin):
            // req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfig);
            // var part = req.raw().getPart("file");

            // NEW WAY (Javalin-compatible):
            PartWrapper filePart = req.getPart("file");
            
            if (filePart != null) {
                String fileName = filePart.getSubmittedFileName();

                if (fileName != null && filePart.getSize() <= MAX_FILE_SIZE) {
                    // Sanitize filename
                    String sanitizedFileName = fileName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
                    Path finalPath = Path.of(UPLOAD_DIR, sanitizedFileName);

                    try (InputStream input = filePart.getInputStream()) {
                        Files.copy(input, finalPath, StandardCopyOption.REPLACE_EXISTING);
                    }

                    res.status(200);
                    return "File uploaded successfully: " + sanitizedFileName;
                } else {
                    res.status(400);
                    if (fileName == null) {
                        return "Error: No filename provided.";
                    } else {
                        return "Error: File size exceeds limit (" + MAX_FILE_SIZE + " bytes).";
                    }
                }
            } else {
                res.status(400);
                return "Error: No file uploaded.";
            }
        } catch (Exception e) {
            res.status(500);
            return "Error processing upload: " + e.getMessage();
        }
    }

    // Example showing how to handle multiple files
    public static Object handleMultipleFiles(Request req, Response res) throws Exception {
        if (!req.isMultipart()) {
            res.status(400);
            return "Error: No multipart data found.";
        }

        StringBuilder result = new StringBuilder("Uploaded files:\n");
        int fileCount = 0;

        // Get all parts
        for (PartWrapper part : req.getParts()) {
            if (part.getSubmittedFileName() != null) {
                String fileName = part.getSubmittedFileName();
                long fileSize = part.getSize();
                
                result.append("- ").append(fileName)
                      .append(" (").append(fileSize).append(" bytes)\n");
                fileCount++;
            }
        }

        if (fileCount == 0) {
            res.status(400);
            return "No files found in request.";
        }

        return result.toString();
    }
}

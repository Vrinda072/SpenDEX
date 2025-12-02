package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticFileHandler implements HttpHandler {
    private final String webRoot;
    private static final String DEFAULT_FILE = "index.html";
    private static final String[] ALLOWED_EXTENSIONS = {"html", "css", "js", "png", "jpg", "jpeg", "gif", "ico", "svg", "json"};
    
    public StaticFileHandler(String webRoot) {
        this.webRoot = webRoot;
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            
            // Handle root path
            if ("/".equals(path)) {
                path = "/index.html";
            }
            
            // Security: Prevent directory traversal
            if (path.contains("..")) {
                sendError(exchange, 403, "Forbidden");
                return;
            }
            
            // Check if the file extension is allowed
            if (!isAllowedExtension(path)) {
                sendError(exchange, 403, "Forbidden: File type not allowed");
                return;
            }
            
            // Get the file path
            String filePath = webRoot + path;
            
            // Try to load the file from the classpath
            InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
            
            if (is == null) {
                // If not found in classpath, try to load from filesystem (for development)
                File file = new File(filePath);
                if (!file.exists() || file.isDirectory()) {
                    // Try with default file
                    file = new File(webRoot + "/" + DEFAULT_FILE);
                    if (!file.exists()) {
                        sendError(exchange, 404, "File not found");
                        return;
                    }
                }
                is = new FileInputStream(file);
            }
            
            // Set content type based on file extension
            String contentType = getContentType(path);
            exchange.getResponseHeaders().set("Content-Type", contentType);
            
            // Set CORS headers
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type,Authorization");
            
            // Handle OPTIONS request for CORS preflight
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            
            // Send the file
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                byte[] buffer = new byte[4096];
                int count;
                while ((count = is.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
            }
            is.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            sendError(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }
    
    private boolean isAllowedExtension(String path) {
        if (path.endsWith("/")) {
            return true; // Allow directory access (will be handled by default file)
        }
        
        String ext = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(ext)) {
                return true;
            }
        }
        return false;
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
    
    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        exchange.sendResponseHeaders(code, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes());
        }
    }
}

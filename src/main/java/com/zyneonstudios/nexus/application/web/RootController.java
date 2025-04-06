package com.zyneonstudios.nexus.application.web;

import com.zyneonstudios.nexus.application.main.NexusApplication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The {@code RootController} class is a REST controller responsible for handling HTTP requests
 * to the root path and its subpaths. It serves static content (HTML, CSS, JavaScript) from the
 * application's UI directory.
 */
@RestController
public class RootController {

    /**
     * Handles all GET requests to the root path ("/") and its subpaths ("/**").
     * It serves static content from the UI directory if the request is from a local address.
     *
     * @param request The incoming HTTP request.
     * @return A ResponseEntity containing the requested resource or an error response.
     */
    @GetMapping("/**")
    public ResponseEntity<Object> handleRequest(HttpServletRequest request) {
        // Extract the requested URL and path from the request.
        String url = request.getRequestURL().toString();
        String path = request.getRequestURI();

        // If the path is empty or just "/", default to "index.html".
        if (path.isEmpty() || path.equals("/")) {
            path = "/index.html";
        }

        // Check if the request is from a local address.
        if (isLocalRequest(url)) {
            try {
                // Construct the full path to the requested file in the UI directory.
                String frontendPath = NexusApplication.getUiPath();
                File file = new File(frontendPath + path);

                // Check if the file exists
                if (!file.exists()) {
                    throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
                }

                // Create an InputStreamResource from the file.
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

                // Determine the appropriate media type based on the file extension.
                MediaType mediaType = getMediaType(path);

                // Return the resource with the correct media type.
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } catch (FileNotFoundException e) {
                NexusApplication.getLogger().err("File not found: " + e.getMessage());
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                // Log any other errors that occur.
                NexusApplication.getLogger().err("Error serving file: " + e.getMessage());
                return ResponseEntity.internalServerError().build();
            }
        }

        // If the request is not from a local address, return a 401 Unauthorized response.
        return ResponseEntity.status(401).build();
    }

    /**
     * Checks if the given URL is from a local address.
     *
     * @param url The URL to check.
     * @return True if the URL is from a local address, false otherwise.
     */
    @SuppressWarnings("HttpUrlsUsage")
    private boolean isLocalRequest(String url) {
        return url.startsWith("http://localhost") ||
                url.startsWith("http://127.0.0.1") ||
                url.startsWith("http://0:0:0:0:0:0:0:1");
    }

    /**
     * Determines the appropriate media type based on the file extension.
     *
     * @param path The path to the file.
     * @return The corresponding MediaType.
     */
    private MediaType getMediaType(String path) {
        if (path.endsWith(".html")) {
            return MediaType.TEXT_HTML;
        } else if (path.endsWith(".css")) {
            return MediaType.valueOf("text/css");
        } else if (path.endsWith(".js")) {
            return MediaType.valueOf("application/javascript");
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
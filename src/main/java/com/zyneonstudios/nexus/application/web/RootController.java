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

@RestController
public class RootController {

    @GetMapping("/**") @SuppressWarnings("HttpUrlsUsage")
    public ResponseEntity<Object> handleRequest(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String path = request.getRequestURI();
        System.out.println(path);
        if(path.isEmpty()||path.equals("/")) {
            path = "/index.html";
        }
        System.out.println(path);
        if(url.startsWith("http://localhost")||url.startsWith("http://127.0.0.1")||url.startsWith("http://0:0:0:0:0:0:0:1")) {
            try {
                String frontendPath = NexusApplication.getWorkingDir().getAbsolutePath()+"/temp/ui";
                frontendPath = "A:/Workspaces/IntelliJ/zyneon/nexus-app/src/main/html";
                File file = new File(frontendPath + path);
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                MediaType mediaType = MediaType.APPLICATION_JSON;
                if(path.endsWith(".html")) {
                    mediaType = MediaType.TEXT_HTML;
                } else if(path.endsWith(".css")) {
                    mediaType = MediaType.valueOf("text/css");
                } else if(path.endsWith(".js")) {
                    mediaType = MediaType.valueOf("application/javascript");
                }
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } catch (Exception e) {
                NexusApplication.getLogger().err(e.getMessage());
            }
        }
        return ResponseEntity.status(401).build();
    }
}

package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.config.StaticInformation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/static")
@Slf4j
public class StaticResourceController {

    @GetMapping(value = "/background-image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> getBackgroundImage() {
        try {
            var resource = new FileUrlResource(
                    Paths.get(StaticInformation.BACKGROUND_IMAGE_PATH).toAbsolutePath().toString()
            );
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(resource.getInputStream()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException ignored) {
            return ResponseEntity.badRequest().build();
        }
    }
}

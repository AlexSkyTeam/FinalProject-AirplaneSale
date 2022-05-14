package org.example.manager;

import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.example.dto.MediaUploadResponseDTO;
import org.example.exception.UnsupportedImageTypeException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MediaManager {
    private static final String IMAGE_PNG = "image/png";
    private static final String PNG = ".png";
    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String JPG = ".jpg";
    private static final String IMAGE_GIF = "image/gif";
    private static final String GIF = ".gif";
    private Tika tika = new Tika();

    public MediaManager() throws IOException {
        Files.createDirectories(Paths.get("media"));
    }

    public MediaUploadResponseDTO upload(byte[] data) throws IOException {
        String name = UUID.randomUUID().toString();

        String mime = tika.detect(data);
        name = withExtension(name, mime);
        Path path = path(name);
        Files.write(path, data);
        return new MediaUploadResponseDTO(name);
    }

    /**
     * @param name имя файла
     * @param mime тип  содержимого
     * @return имя файла с расширением
     * @throws UnsupportedImageTypeException для неподдреживаемых форматов
     */
    private String withExtension(String name, String mime) throws UnsupportedImageTypeException {
        // обновленный формат записи, поэтому использовал.
        return switch (mime) {
            case IMAGE_PNG -> name + PNG;
            case IMAGE_JPEG -> name + JPG;
            case IMAGE_GIF -> name + GIF;
            default -> throw new UnsupportedImageTypeException("Unsupported Image Type: " + mime);
        };
    }

    private Path path(String name) {
        return Paths.get("media", name);
    }
}

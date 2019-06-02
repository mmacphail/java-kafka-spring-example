package eu.macphail.kafkaex;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Data
@AllArgsConstructor
public class FileInfo {

    public static FileInfo readFromPath(Path path) {
        String filename = path.toFile().getName();
        String extension = Optional.of(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse("");

        try {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            LocalDateTime creationTime = LocalDateTime
                    .ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime lastModifiedTime = LocalDateTime
                    .ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());

            return new FileInfo(path.toAbsolutePath().toString(),
                    attr.isDirectory(),
                    extension,
                    creationTime,
                    lastModifiedTime);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String filePath;

    private boolean directory;

    private String extension;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastModifiedTime;
}

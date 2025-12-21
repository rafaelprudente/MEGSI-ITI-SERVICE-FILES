package pt.iti.umdrive.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileModel {
    private UUID id;
    private UUID userId;
    private String originalName;
    private String storedName;
    private String mimeType;
    private long size;
    private long version;
    private Instant createAt;
}

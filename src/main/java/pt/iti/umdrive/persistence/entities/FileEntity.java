package pt.iti.umdrive.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.megsi.fwk.entities.UserEntity;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserEntity user;

    @Column(name = "original_name")
    private String originalName;
    @Column(name = "stored_name")
    private String storedName;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "size")
    private long size;
    @Column(name = "version")
    private long version;

    @Column(name = "create_at")
    private Instant createAt;
}
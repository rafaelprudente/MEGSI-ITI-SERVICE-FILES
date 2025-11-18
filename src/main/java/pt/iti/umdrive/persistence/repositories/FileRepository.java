package pt.iti.umdrive.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.iti.umdrive.persistence.entities.FileEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    List<FileEntity> findByUser_UsernameIgnoreCaseOrderByVersionAsc(String username);

    List<FileEntity> findByUser_UsernameAndOriginalName(String username, String originalName);
}
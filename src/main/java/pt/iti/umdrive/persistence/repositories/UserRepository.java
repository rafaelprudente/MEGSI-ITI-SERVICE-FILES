package pt.iti.umdrive.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.iti.umdrive.persistence.entities.AuthUserEntity;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AuthUserEntity, UUID> {
    AuthUserEntity findByUsername(String username);
}
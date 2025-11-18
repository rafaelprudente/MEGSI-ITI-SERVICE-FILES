package pt.megsi.fwk.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_details")
public class UserDetailEntity {
    @Column(name = "id", nullable = false)
    private UUID id;
    @Id
    @Column(name = "username", nullable = false)
    private String username;
}
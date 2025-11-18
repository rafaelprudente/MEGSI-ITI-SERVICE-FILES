package pt.iti.umdrive.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
@IdClass(AuthorityId.class)
public class AuthorityEntity {
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    @Id
    @Column(name = "authority", nullable = false)
    private String authority;
}
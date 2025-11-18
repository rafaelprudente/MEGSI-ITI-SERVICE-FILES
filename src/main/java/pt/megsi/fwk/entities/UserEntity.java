package pt.iti.umdrive.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "username", nullable = false, unique = true)
    private UserDetailEntity details;

    @Builder.Default
    @JoinColumn(name = "username")
    @OneToMany(orphanRemoval = true)
    private Set<AuthorityEntity> authorities = new LinkedHashSet<>();
}
package pt.iti.umdrive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.iti.umdrive.persistence.entities.AuthUserEntity;
import pt.iti.umdrive.persistence.repositories.UserRepository;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserEntity authUserEntity;

        if (!isUUID(username)) {
            authUserEntity = userRepository.findByUsername(username);
        } else {
            authUserEntity = userRepository.findById(UUID.fromString(username)).orElse(null);
        }

        if (authUserEntity == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                authUserEntity.getUsername(),
                authUserEntity.getPassword(),
                Collections.emptyList()
        );
    }

    private boolean isUUID(String username) {
        try {
            UUID.fromString(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

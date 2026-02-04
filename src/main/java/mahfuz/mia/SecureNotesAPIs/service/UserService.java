package mahfuz.mia.SecureNotesAPIs.service;

import lombok.RequiredArgsConstructor;
import mahfuz.mia.SecureNotesAPIs.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Get current logged-in user's ID
    public Long getCurrentUserId(Authentication auth){
        String username = auth.getName();
        Long currentUserId = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
                .getId();
        return currentUserId;
    }
}

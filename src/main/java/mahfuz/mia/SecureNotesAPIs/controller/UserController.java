package mahfuz.mia.SecureNotesAPIs.controller;

import lombok.RequiredArgsConstructor;
import mahfuz.mia.SecureNotesAPIs.entity.User;
import mahfuz.mia.SecureNotesAPIs.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/resgister")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}

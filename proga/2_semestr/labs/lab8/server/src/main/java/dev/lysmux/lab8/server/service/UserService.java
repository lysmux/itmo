package dev.lysmux.lab8.server.service;

import dev.lysmux.lab8.server.domain.User;
import dev.lysmux.lab8.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean registerUser(String login, String password) {
        if (userRepository.get(login) != null) {
            return false;
        }
        User user = new User(login, hashedPassword(password));
        userRepository.add(user);
        return true;
    }

    public User getUser(String login) {
        return userRepository.get(login);
    }

    public boolean checkUserCredentials(String login, String password) {
        User user = userRepository.get(login);
        return user != null && user.hashedPassword().equals(hashedPassword(password));
    }

    public void changePassword(String login, String password) {
       userRepository.changePassword(login, hashedPassword(password));
    }

    private String hashedPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            byte[] hash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

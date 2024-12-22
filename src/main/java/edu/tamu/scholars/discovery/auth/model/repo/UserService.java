package edu.tamu.scholars.discovery.auth.model.repo;

import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.auth.model.User;

@Service
public class UserService {

    private UserRepo userRepo;

    UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    public long count() {
        return userRepo.count();
    }

}

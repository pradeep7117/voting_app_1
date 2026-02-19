package com.example.VotingApplication.userRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.VotingApplication.Entity.User;
import com.example.VotingApplication.Entity.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(Status status); // ✅ Use Enum, not String


  //  List<User> findByStatus(String status);
    //User findByUsername(String username);
    Optional<User> findByUsername(String username);
}

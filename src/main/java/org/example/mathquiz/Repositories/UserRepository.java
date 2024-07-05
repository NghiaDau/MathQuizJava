package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
     User findFirstById(String id);

     User findUserByUserName(String username);

     @Query("select u from User u where u.email=?1")
     User findByEmail(String email);

     @Query("select u from User u where u.resetPasswordToken=?1")
     User findByToken(String token);

     User findByUserName(String userName);
}

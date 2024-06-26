package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}

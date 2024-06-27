package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Level;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILevelRepository extends JpaRepository<Level, String> {

}

package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.MathType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMathTypeRepository extends JpaRepository<MathType, String> {

}

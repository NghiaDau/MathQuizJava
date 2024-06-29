package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.MathType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMathTypeRepository extends JpaRepository<MathType, String> {
}

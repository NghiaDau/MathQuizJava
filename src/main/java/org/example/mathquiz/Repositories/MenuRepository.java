package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, String> {
    @Query("SELECT m FROM Menu m WHERE m.parent IS NULL ORDER BY m.name")
    List<Menu> findAllMenus();

    @Query("SELECT m FROM Menu m ORDER BY m.level, m.name")
    List<Menu> findAll();
}

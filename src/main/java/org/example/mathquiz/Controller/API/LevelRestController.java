package org.example.mathquiz.Controller.API;

import org.example.mathquiz.Entities.Level;
import org.example.mathquiz.Service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
public class LevelRestController {
    @Autowired
    private LevelService levelService;
    @CrossOrigin
    @GetMapping("")
    public List<Level> getLevels() {
        return levelService.getAllLevels();
    }
}

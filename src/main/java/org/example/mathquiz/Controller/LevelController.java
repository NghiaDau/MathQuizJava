package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.Level;
import org.example.mathquiz.Service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/levels")
public class LevelController {
    @Autowired
    private LevelService levelService;
    @GetMapping("")
    public String showAllLevels(@NotNull Model model,
                                   @RequestParam(defaultValue = "0")
                                   Integer pageNo,
                                   @RequestParam(defaultValue = "20")
                                   Integer pageSize,
                                   @RequestParam(defaultValue = "id")
                                   String sortBy) {
        model.addAttribute("levels", levelService.getAllLevels(pageNo,
                pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",
                levelService.getAllLevels(pageNo, pageSize, sortBy).size() / pageSize);
        return "level/index";
    }
    @GetMapping("/add")
    public String addLevelForm(@NotNull Model model) {
        model.addAttribute("level", new Level());
        return "level/add";
    }
    @PostMapping("/add")
    public String addLevel(
            @ModelAttribute("level") Level level,
            BindingResult bindingResult,
            Model model) {
        levelService.addLevel(level);
        return "redirect:/levels";
    }
}

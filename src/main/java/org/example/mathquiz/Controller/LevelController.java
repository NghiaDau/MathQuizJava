package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.Level;
import org.example.mathquiz.Service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/levels")
public class LevelController {
    @Autowired
    private LevelService levelService;

    @GetMapping("")
    public String showAllLevels(@NotNull Model model,
                                @RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "20") Integer pageSize,
                                @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("levels", levelService.getAllLevels(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", levelService.getAllLevels(pageNo, pageSize, sortBy).size() / pageSize);
        return "level/index";
    }

    @GetMapping("/add")
    public String addLevelForm(@NotNull Model model) {
        model.addAttribute("level", new Level());
        return "level/add";
    }

    @PostMapping("/add")
    public String addLevel(@ModelAttribute("level") Level level,
                           BindingResult bindingResult,
                           Model model) {
        levelService.addLevel(level);
        return "redirect:/levels";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteLevel(@PathVariable String id, Model model) {
        var level = levelService.getLevelById(id)
                .orElseThrow(() -> new IllegalArgumentException("Level not found"));
        model.addAttribute("level", level);
        return "level/delete";
    }

    @PostMapping("/delete")
    public String deleteLevel(@ModelAttribute("level") Level level) {
        levelService.getLevelById(level.getId())
                .ifPresentOrElse(
                        lvl -> levelService.deleteLevelById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("Level not found"); });
        return "redirect:/levels";
    }

    @GetMapping("/edit/{id}")
    public String editLevelForm(@NotNull Model model, @PathVariable String id) {
        var level = levelService.getLevelById(id);
        model.addAttribute("level", level.orElseThrow(() -> new IllegalArgumentException("Level not found")));
        return "level/edit";
    }

    @PostMapping("/edit")
    public String editLevel(@ModelAttribute("level") Level level,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "level/edit";
        }

        levelService.updateLevel(level);
        return "redirect:/levels";
    }
}
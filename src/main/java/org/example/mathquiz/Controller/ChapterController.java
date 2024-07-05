package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chapters")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private MathTypeService mathTypeService;
    @GetMapping("")
    public String showAllChapters(@NotNull Model model) {
        model.addAttribute("chapters", chapterService.getAllChapters());
        model.addAttribute("grades",
                gradeService.getAllGrades());
        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());
        return "chapter/index";
    }

    @GetMapping("/add")
    public String addChapterForm(@NotNull Model model) {
        model.addAttribute("chapter", new Chapter());
        model.addAttribute("grades",
                gradeService.getAllGrades());
        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());

        return "chapter/add";
    }

    @PostMapping("/add")
    public String addChapter(@ModelAttribute("chapter") Chapter chapter,
                           BindingResult bindingResult,
                           Model model) {
        Chapter savedChapter = chapterService.addChapter(chapter);
        return "redirect:/chapters";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteChapter(@PathVariable String id, Model model) {
        var chapter = chapterService.getChapterById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
        model.addAttribute("chapter", chapter);

        return "chapter/delete";
    }

    @PostMapping("/delete")
    public String deleteChapter(@ModelAttribute("chapter") Chapter chapter) {
        chapterService.getChapterById(chapter.getId())
                .ifPresentOrElse(
                        lvl -> chapterService.deleteChapterById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("Chapter not found"); });
        return "redirect:/chapters";
    }

    @GetMapping("/edit/{id}")
    public String editChapterForm(@NotNull Model model, @PathVariable String id) {
        var chapter = chapterService.getChapterById(id);
        model.addAttribute("grades",
                gradeService.getAllGrades());
        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());

        model.addAttribute("chapter", chapter.orElseThrow(() -> new IllegalArgumentException("Chapter not found")));
        return "chapter/edit";
    }

    @PostMapping("/edit")
    public String editChapter(@ModelAttribute("chapter") Chapter chapter,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "chapter/edit";
        }

        chapterService.updateChapter(chapter);
        return "redirect:/chapters";
    }

}
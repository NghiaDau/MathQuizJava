package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.MathType;
import org.example.mathquiz.Service.MathTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mathTypes")
public class MathTypeController {
    @Autowired
    private MathTypeService mathTypeService;

    @GetMapping("")
    public String showAllMathTypes(@NotNull Model model) {
        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());
        return "mathType/index";
    }

    @GetMapping("/add")
    public String addMathTypeForm(@NotNull Model model) {
        model.addAttribute("mathType", new MathType());
        return "mathType/add";
    }

    @PostMapping("/add")
    public String addMathType(@ModelAttribute("mathType") MathType mathType,
                           BindingResult bindingResult,
                           Model model) {
        mathTypeService.addMathType(mathType);
        return "redirect:/mathTypes";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteMathType(@PathVariable String id, Model model) {
        var mathType = mathTypeService.getMathTypeById(id)
                .orElseThrow(() -> new IllegalArgumentException("MathType not found"));
        model.addAttribute("mathType", mathType);
        return "mathType/delete";
    }

    @PostMapping("/delete")
    public String deleteMathType(@ModelAttribute("mathType") MathType mathType) {
        mathTypeService.getMathTypeById(mathType.getId())
                .ifPresentOrElse(
                        lvl -> mathTypeService.deleteMathTypeById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("MathType not found"); });
        return "redirect:/mathTypes";
    }

    @GetMapping("/edit/{id}")
    public String editMathTypeForm(@NotNull Model model, @PathVariable String id) {
        var mathType = mathTypeService.getMathTypeById(id);
        model.addAttribute("mathType", mathType.orElseThrow(() -> new IllegalArgumentException("MathType not found")));
        return "mathType/edit";
    }

    @PostMapping("/edit")
    public String editMathType(@ModelAttribute("mathType") MathType mathType,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "mathType/edit";
        }

        mathTypeService.updateMathType(mathType);
        return "redirect:/mathTypes";
    }
}
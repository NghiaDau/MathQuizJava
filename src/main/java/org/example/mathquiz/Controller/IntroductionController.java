package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/introduction")
public class IntroductionController {
    @GetMapping("")
    public String index(@NotNull Model model) {
        return "introduction/index";
    }
}

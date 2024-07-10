package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.Level;
import org.example.mathquiz.Entities.Menu;
import org.example.mathquiz.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/menus")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping()
    public String getAllMenu(Model model) {
        model.addAttribute("menus",menuService.getallMenus());
        return "menu/index";
    }

    @GetMapping("/add")
    public String newMenu(Model model){
        Menu menu = new Menu();
        List<Menu> menus = menuService.getallMenus();
        model.addAttribute("menus", menus);
        model.addAttribute("menu", menu);
        return "menu/add";
    }
    @PostMapping("add")
    public String newMenu(Menu menu){
        menu.setEnabled(true);
        menu.setLevel(menu.getParent() == null ? 0: menu.getParent().getLevel() + 1);
        menuService.saveMenu(menu);
        return "redirect:/menus";
    }
    @GetMapping("/edit/{id}")
    public String editLevelForm(@NotNull Model model, @PathVariable String id) {
        var menu = menuService.getMenu(id);
        model.addAttribute("menu", menu.orElseThrow(() -> new IllegalArgumentException("Menu not found")));
        List<Menu> menus = menuService.getallMenus();
        model.addAttribute("menus", menus);
        return "menu/edit";
    }

    @PostMapping("/edit")
    public String editLevel(@ModelAttribute("menu") Menu menu,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "menu/edit";
        }
        menuService.saveMenu(menu);
        return "redirect:/menus";
    }
    @GetMapping("/delete/{id}")
    public String confirmDeleteLevel(@PathVariable String id, Model model) {
        var menu = menuService.getMenu(id);
        model.addAttribute("menu", menu.orElseThrow(() -> new IllegalArgumentException("Menu not found")));
        return "menu/delete";
    }

    @PostMapping("/delete")
    public String deleteLevel(@ModelAttribute("menu") Menu menu) {
        menuService.getMenu(menu.getId_menu())
                .ifPresentOrElse(
                        menuTmp -> menuService.deleteMenu(menuTmp),
                        () -> { throw new IllegalArgumentException("Menu not found"); });
        return "redirect:/menus";
    }

    @GetMapping("/table/{id}")
    public String indexTale(@PathVariable String id , Model model) {
        model.addAttribute("stringid",id);
        return "menu/table";
    }
}

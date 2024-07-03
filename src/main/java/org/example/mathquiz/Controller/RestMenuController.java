package org.example.mathquiz.Controller;

import org.example.mathquiz.Entities.Menu;
import org.example.mathquiz.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class RestMenuController {
    @Autowired
    private MenuService menuService;
    @RequestMapping()
    public List<Menu> getAllMenu(){
        List<Menu> menus = menuService.getMenus();
        return menuService.getMenus();
    }
}

package org.example.mathquiz.Service;

import org.example.mathquiz.Entities.Menu;
import org.example.mathquiz.Repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;
    public List<Menu> getMenus() {
        return menuRepository.findAllMenus();
    }
    public Menu getMenu(String id) {
        return menuRepository.findById(id).get();
    }
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

}
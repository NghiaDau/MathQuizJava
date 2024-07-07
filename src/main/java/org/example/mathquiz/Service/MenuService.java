package org.example.mathquiz.Service;

import org.example.mathquiz.Controller.API.NotificationController;
import org.example.mathquiz.Entities.Menu;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private NotificationController notificationController;

    public void updateMenu(Menu updatedMenu) {
        menuRepository.save(updatedMenu);
        notificationController.notifyUpdate();
    }
    public List<Menu> getallMenus() {
        return menuRepository.findAll();
    }
    public List<Menu> getMenus() {
        return menuRepository.findAllMenus();
    }
    public Optional<Menu> getMenu(String id) {
        return menuRepository.findById(id);
    }
    public void saveMenu(Menu menu) {
        menuRepository.save(menu);
        notificationController.notifyUpdate();
    }

    public void deleteMenu(Menu menu) {
        menu.setEnabled(false);
        menuRepository.save(menu);
        notificationController.notifyUpdate();
    }
}
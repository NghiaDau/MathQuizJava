package org.example.mathquiz.Controller.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/notifyUpdate")
    public void notifyUpdate() {
        messagingTemplate.convertAndSend("/topic/updates", "Data updated!");
    }
}
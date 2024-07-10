package org.example.mathquiz.Controller.API;

import org.example.mathquiz.Service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/results")
public class ApiResultController {
    @Autowired
    private ResultService resultService;
}

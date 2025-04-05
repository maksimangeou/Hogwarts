package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Info;
import ru.hogwarts.school.service.InfoService;

@RestController
@RequestMapping("/info-port")
public class InfoController {

    @Autowired
    private final InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping
    public Info getPort() {
        return infoService.getPort();
    }

}

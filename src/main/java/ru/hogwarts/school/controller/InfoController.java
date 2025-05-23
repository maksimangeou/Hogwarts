package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Info;
import ru.hogwarts.school.service.InfoService;

@RestController
@RequestMapping
public class InfoController {

    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/port")
    public Info getPort() {
        return infoService.getPort();
    }

    @GetMapping("/progression-v1")
    public long getSumProgression() {
        return infoService.getSumProgression();
    }

    @GetMapping("/progression-v2")
    public long getSumProgressionV2() {
        return infoService.getSumProgressionV2();
    }

}

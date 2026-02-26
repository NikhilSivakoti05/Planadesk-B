package com.planadesk.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.planadesk.backend.model.Section;
import com.planadesk.backend.service.SectionService;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public Section createSection(
            @RequestParam int number,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam MultipartFile image
    ) {
        return sectionService.createSection(number, title, description, image);
    }

    @GetMapping
    public List<Section> getAllSections() {
        return sectionService.getAllSections();
    }
}
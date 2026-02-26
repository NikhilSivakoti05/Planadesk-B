package com.planadesk.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.planadesk.backend.model.Section;
import com.planadesk.backend.repository.SectionRepository;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final ImageKitService imageKitService;

    public SectionService(SectionRepository sectionRepository,
                          ImageKitService imageKitService) {
        this.sectionRepository = sectionRepository;
        this.imageKitService = imageKitService;
    }

    public Section createSection(int number, String title,
                                 String description,
                                 MultipartFile image) {

        if (sectionRepository.findByNumber(number).isPresent()) {
            throw new RuntimeException("Section number already exists");
        }

        String imageUrl = imageKitService.upload(image);

        Section section = new Section();
        section.setNumber(number);
        section.setTitle(title);
        section.setDescription(description);
        section.setImageUrl(imageUrl);

        return sectionRepository.save(section);
    }

    public List<Section> getAllSections() {
        return sectionRepository.findAll()
                .stream()
                .sorted((a, b) -> Integer.compare(a.getNumber(), b.getNumber()))
                .toList();
    }
}
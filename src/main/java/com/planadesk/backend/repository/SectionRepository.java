package com.planadesk.backend.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.planadesk.backend.model.Section;

public interface SectionRepository extends MongoRepository<Section, String> {
    Optional<Section> findByNumber(int number);
}
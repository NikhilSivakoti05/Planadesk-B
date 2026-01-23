package com.planadesk.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.planadesk.backend.model.Country;

public interface CountryRepository extends MongoRepository<Country, String> {

    Optional<Country> findByCountryCode(String countryCode);

    List<Country> findByActiveTrue();
}

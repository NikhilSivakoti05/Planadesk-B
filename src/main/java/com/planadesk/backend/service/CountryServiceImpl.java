package com.planadesk.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.planadesk.backend.model.Country;
import com.planadesk.backend.repository.CountryRepository;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country createCountry(Country country) {
        countryRepository.findByCountryCode(country.getCountryCode())
            .ifPresent(c -> { throw new RuntimeException("Country code already exists"); });
        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(String id, Country updated) {
        Country existing = countryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Country not found"));

        existing.setCountryCode(updated.getCountryCode());
        existing.setCountryName(updated.getCountryName());
        existing.setActive(updated.isActive());

        return countryRepository.save(existing);
    }

    @Override
    public void deleteCountry(String id) {
        countryRepository.deleteById(id);
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public List<Country> getActiveCountries() {
        return countryRepository.findByActiveTrue();
    }

    @Override
    public Country getCountryById(String id) {
        return countryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Country not found"));
    }
}

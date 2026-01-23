package com.planadesk.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.planadesk.backend.model.Country;
import com.planadesk.backend.service.CountryService;

@RestController
@RequestMapping("/api/countries")

public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    public Country createCountry(@RequestBody Country country) {
        return countryService.createCountry(country);
    }

    @PutMapping("/{id}")
    public Country updateCountry(@PathVariable String id,
                                 @RequestBody Country country) {
        return countryService.updateCountry(id, country);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable String id) {
        countryService.deleteCountry(id);
    }

    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/active")
    public List<Country> getActiveCountries() {
        return countryService.getActiveCountries();
    }

    @GetMapping("/{id}")
    public Country getCountry(@PathVariable String id) {
        return countryService.getCountryById(id);
    }
}

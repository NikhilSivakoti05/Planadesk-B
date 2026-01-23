package com.planadesk.backend.service;

import java.util.List;
import com.planadesk.backend.model.Country;

public interface CountryService {

    Country createCountry(Country country);

    Country updateCountry(String id, Country country);

    void deleteCountry(String id);

    List<Country> getAllCountries();

    List<Country> getActiveCountries();

    Country getCountryById(String id);
}

package com.app.covidtracker.serviceImpl;

import com.app.covidtracker.domain.Country;
import com.app.covidtracker.domain.frontend.FrontendCountry;
import com.app.covidtracker.domain.frontend.FrontendState;
import com.app.covidtracker.domain.frontend.FrontendDistrict;
import com.app.covidtracker.domain.india.State;
import com.app.covidtracker.domain.india.District;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapperService {

    public List<FrontendCountry> mapToFrontendCountries(List<Country> countries) {
        List<FrontendCountry> list = new ArrayList<>();
        for (Country country: countries) {
            list.add(mapToFrontendCountry(country));
        }
        return list;
    }

    public FrontendCountry mapToFrontendCountry(Country country) {
        return new FrontendCountry(country.getCountryName(), country.getTotal(), country.getTotal().getDate());
    }

    public List<FrontendState> mapToFrontendStates(List<State> states) {
        List<FrontendState> list = new ArrayList<>();
        for(State state: states) {
            list.add(mapToFrontendState(state));
        }
        return list;
    }

    public FrontendState mapToFrontendState(State state) {
        return new FrontendState(state.getStateName(), state.getTotal(), state.getTotal().getDate(), state.getDistricts());
    }

    public FrontendDistrict mapToFrontendDistrict(District district) {
        return new FrontendDistrict(district.getDistrictName(), district.getTotal(), district.getTotal().getDate(), district.getZone());
    }

}

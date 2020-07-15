package com.app.covidtracker.serviceImpl;

import com.app.covidtracker.domain.Country;
import com.app.covidtracker.domain.frontend.FrontendCountry;
import com.app.covidtracker.domain.frontend.FrontendState;
import com.app.covidtracker.domain.india.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapperService {

    public List<FrontendCountry> mapToFrontendCountry(List<Country> countries) {
        List<FrontendCountry> list = new ArrayList<>();
        for (Country country: countries) {
            FrontendCountry frontendCountry = new FrontendCountry(country.getCountryName(), country.getTotal());
            list.add(frontendCountry);
        }
        return list;
    }

    public List<FrontendState> mapToFrontendState(List<State> states) {
        List<FrontendState> list = new ArrayList<>();
        for(State state: states) {
            FrontendState frontendState = new FrontendState(state.getStateName(), state.getTotal());
            list.add(frontendState);
        }
        return list;
    }

}

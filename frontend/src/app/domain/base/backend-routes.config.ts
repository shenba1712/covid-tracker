export const BackendRoutesConfig = {
  baseUrl: 'http://localhost:8080',
  global: {
    baseUrl: '/global',
    latest: '/latest',
    countries: '/countries',
    search: '/search',
    country: '/country/{name}',
    chartInfo: '/chart'
  },
  india: {
    baseUrl: '/indian',
    states: '/states',
    state: '/state/{name}',
    statesSearch: '/states/search',
    district: '/state/{stateName}/district/{districtName}',
    resources: '/resources',
    chartInfo: '/chart'
  }
};

package com.app.covidtracker.domain.india;

public enum StateCode {
    AN("Andaman and Nicobar Islands"),
    AP("Andhra Pradesh"),
    AR("Arunachal Pradesh"),
    AS("Assam"),
    TN("Tamil Nadu"),
    KL("Kerala"),
    KA("Karnataka"),
    MH("Maharashtra"),
    BR("Bihar"),
    CH("Chandigarh"),
    CT("Chhattisgarh"),
    DL("Delhi"),
    DN("Dadra and Nagar Haveli and Daman and Diu"),
    GA("Goa"),
    GJ("Gujarat"),
    HP("Himachal Pradesh"),
    HR("Haryana"),
    JH("Jharkhand"),
    UP("Uttar Pradesh"),
    JK("Jammu and Kashmir"),
    LA("Ladakh"),
    LD("Lakshadweep"),
    ML("Meghalaya"),
    MN("Manipur"),
    MP("Madhya Pradesh"),
    MZ("Mizoram"),
    NL("Nagaland"),
    OR("Odisha"),
    PB("Punjab"),
    PY("Puducherry"),
    RJ("Rajasthan"),
    SK("Sikkim"),
    TG("Telangana"),
    TR("Tripura"),
    UT("Uttarakhand"),
    WB("West Bengal");

    private String name;

    StateCode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package com.iovation.launchkey.sdk.integration.cucumber.converters;

import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;

import java.util.ArrayList;
import java.util.List;

public class DaysListConverter {
    public static List<ServicePolicy.Day> fromString(String daysString) {

        List<ServicePolicy.Day> days = new ArrayList<>();
        for (String day : daysString.split(",")) {
            days.add(ServicePolicy.Day.fromString(day));
        }

        return days;
    }
}

package com.iovation.launchkey.sdk.integration.cucumber.converters;

import com.iovation.launchkey.sdk.integration.entities.ServicePolicyEntity;
import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class TimeFenceListConverter {
    public static List<ServicePolicyEntity.TimeFence> fromDataTable(DataTable dataTable) {
        List<ServicePolicyEntity.TimeFence> timeFences = new ArrayList<>();

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : rows) {
            timeFences.add(new ServicePolicyEntity.TimeFence(
                    map.get("Name"),
                    DaysListConverter.fromString(map.get("Days")),
                    Integer.valueOf(map.get("Start Hour")),
                    Integer.valueOf(map.get("Start Minute")),
                    Integer.valueOf(map.get("End Hour")),
                    Integer.valueOf(map.get("End Minute")),
                    TimeZone.getTimeZone(map.get("Time Zone"))));
        }

        return timeFences;
    }
}

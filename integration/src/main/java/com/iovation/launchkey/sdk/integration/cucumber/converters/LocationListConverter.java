package com.iovation.launchkey.sdk.integration.cucumber.converters;

import com.iovation.launchkey.sdk.integration.entities.ServicePolicyEntity;
import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationListConverter {
    public static List<ServicePolicyEntity.Location> fromDataTable(DataTable dataTable) {

        List<ServicePolicyEntity.Location> locations = new ArrayList<>();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : rows) {
            locations.add(new ServicePolicyEntity.Location(
                    map.get("Name"),
                    Double.valueOf(map.get("Latitude")),
                    Double.valueOf(map.get("Longitude")),
                    Double.valueOf(map.get("Radius"))
            ));
        }

        return locations;
    }
}

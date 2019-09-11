package com.iovation.launchkey.sdk.integration.cucumber.converters;

import com.iovation.launchkey.sdk.domain.policy.GeoCircleFence;
import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeoCircleFenceConverter {
    public static List<GeoCircleFence> fromDataTable(DataTable dataTable) {
        List<GeoCircleFence> fences = new ArrayList<>();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : rows) {
            fences.add(new GeoCircleFence(
                    map.get("name"),
                    Double.parseDouble(map.get("latitude")),
                    Double.parseDouble(map.get("longitude")),
                    Double.parseDouble(map.get("radius"))
            ));
        }
        return fences;
    }
}

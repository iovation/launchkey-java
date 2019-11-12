package com.iovation.launchkey.sdk.integration.cucumber.converters;

import com.iovation.launchkey.sdk.domain.policy.TerritoryFence;
import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TerritoryFenceConverter {
    public static List<TerritoryFence> fromDataTable(DataTable dataTable) {
        List<TerritoryFence> fences = new ArrayList<>();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : rows) {
            fences.add(new TerritoryFence(
                    map.get("name"),
                    map.get("country"),
                    map.get("admin_area"),
                    map.get("postal_code")
            ));
        }
        return fences;
    }
}
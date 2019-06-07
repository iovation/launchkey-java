package com.iovation.launchkey.sdk.integration.cucumber.converters;

import com.iovation.launchkey.sdk.domain.service.AuthMethod;
import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodsListConverter {
    public static List<AuthMethod> fromDataTable(DataTable dataTable) {

        List<AuthMethod> methods = new ArrayList<>();

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : rows) {
            String set = map.get("Set").toLowerCase();
            String active = map.get("Active").toLowerCase();
            String allowed = map.get("Allowed").toLowerCase();
            String supported = map.get("Supported").toLowerCase();
            String userRequired = map.get("User Required").toLowerCase();
            String passed = map.get("Passed").toLowerCase();
            String error = map.get("Error").toLowerCase();
            methods.add(new AuthMethod(
                    AuthMethod.Type.fromString(map.get("Method")),
                    set.equals("null") ? null : Boolean.valueOf(set),
                    active.equals("null") ? null : Boolean.valueOf(active),
                    allowed.equals("null") ? null : Boolean.valueOf(allowed),
                    supported.equals("null") ? null : Boolean.valueOf(supported),
                    userRequired.equals("null") ? null : Boolean.valueOf(userRequired),
                    passed.equals("null") ? null : Boolean.valueOf(passed),
                    error.equals("null") ? null : Boolean.valueOf(error)
            ));
        }

        return methods;
    }
}

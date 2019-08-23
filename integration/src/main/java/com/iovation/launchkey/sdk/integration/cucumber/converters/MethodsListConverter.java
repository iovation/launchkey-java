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
                    set.isEmpty() ? null : Boolean.valueOf(set),
                    active.isEmpty() ? null : Boolean.valueOf(active),
                    allowed.isEmpty() ? null : Boolean.valueOf(allowed),
                    supported.isEmpty() ? null : Boolean.valueOf(supported),
                    userRequired.isEmpty() ? null : Boolean.valueOf(userRequired),
                    passed.isEmpty() ? null : Boolean.valueOf(passed),
                    error.isEmpty() ? null : Boolean.valueOf(error)
            ));
        }

        return methods;
    }
}

package com.iovation.launchkey.sdk.integration.cucumber;

import com.iovation.launchkey.sdk.domain.policy.Factor;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CucumberTypeRegistryConfigurer implements TypeRegistryConfigurer {
    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineParameterType(new ParameterType<>(
                "factors",
                "(Knowledge|Inherence|Possession)(,\\s*\\d+)*",
                List.class,
                new Transformer<List>() {
                    @Override
                    public List<Factor> transform(String s) throws Throwable {
                        String[] factorsAsStrings = s.split("\\s*,\\s*");
                        List<Factor> factors = new ArrayList<>();
                        for (String stringFactor : factorsAsStrings) {
                            factors.add(Factor.valueOf(stringFactor));
                        }
                        return factors;
                    }
                }
        ));
        typeRegistry.defineParameterType(new ParameterType<>(
                "field",
                "(deny_rooted_jailbroken|deny_emulator_simulator)",
                String.class,
                String::intern
        ));

        typeRegistry.defineParameterType(new ParameterType<>(
                "value",
                "(True|False)",
                Boolean.class,
                Boolean::parseBoolean
        ));


    }
}

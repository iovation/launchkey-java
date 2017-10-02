/**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.integration.entities;

import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import cucumber.deps.com.thoughtworks.xstream.converters.Converter;
import cucumber.deps.com.thoughtworks.xstream.converters.MarshallingContext;
import cucumber.deps.com.thoughtworks.xstream.converters.UnmarshallingContext;
import cucumber.deps.com.thoughtworks.xstream.io.HierarchicalStreamReader;
import cucumber.deps.com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.ArrayList;
import java.util.List;

import static gherkin.StringUtils.join;

public class DaysConverter implements Converter {
    @Override
    public boolean canConvert(Class aClass) {
        return List.class.isAssignableFrom(aClass);
    }

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        if (!(o instanceof List)) {
            throw new IllegalArgumentException("Expected object to implement List not " + o.getClass().getName());
        }
        List<String> stringList = new ArrayList<>();
        for (Object obj : (List) o) {
            stringList.add(obj.toString());
        }
        String listString = join(",", stringList);
        hierarchicalStreamWriter.setValue(listString);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        String[] strings = hierarchicalStreamReader.getValue().split(",");
        List<ServicePolicy.Day> days = new ArrayList<>();
        for (String day : strings) {
            days.add(ServicePolicy.Day.fromString(day.trim()));
        }
        return days;
    }
}

package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PolicyTest {

    @Test
    public void getPolicyType() {
        Policy policy = new Policy("type", null,null,null,null, null, null, null, null);
        assertEquals(policy.getPolicyType(),"type");
    }

    @Test
    public void getDenyRootedJailbroken() {
        Boolean expected = true;
        Policy policy = new Policy(null, expected,null,null,null, null, null, null, null);
        assertEquals(policy.getDenyRootedJailbroken(),expected);
    }

    @Test
    public void getDenyEmulatorSimulator() {
        Boolean expected = true;
        Policy policy = new Policy(null, null, expected,null,null, null, null, null, null);
        assertEquals(policy.getDenyEmulatorSimulator(),expected);
    }

    @Test
    public void getFences() {
        List<Fence> expected = new ArrayList<>();
        Fence geoCircleFence = new Fence("a GeoCircle Fence", Fence.TYPE_GEO_CIRCLE, 1.0,1.0,1.0, null, null, null);
        Fence territorialFence = new Fence("a Territory Fence", Fence.TYPE_TERRITORY, null, null, null, "country", "Admin Area", "ABCDE6");
        expected.add(geoCircleFence);
        expected.add(territorialFence);
        Policy policy = new Policy(null, null,null,expected,null,null, null, null, null);
        assertEquals(policy.getFences(),expected);
    }

    @Test
    public void getFactors() {
        ArrayNode expected = new ArrayNode(JsonNodeFactory.instance) {{
            add("INHERENCE");
            add("POSSESSION");
        }};
        Policy policy = new Policy(Policy.TYPE_FACTORS, null, null,null, expected, null,null, null, null);
        assertEquals(policy.getFactors(),expected);
    }

    @Test
    public void getAmount() {
        Policy policy = new Policy(Policy.TYPE_METHOD_AMOUNT, null,null,null,null, 5, null, null, null);
        assertEquals(policy.getAmount(), Integer.valueOf(5));
    }

    @Test
    public void getInPolicy() {
        Policy expected = new Policy(Policy.TYPE_METHOD_AMOUNT, false,false,null, null, 5, null,null, null);
        Policy policy = new Policy(null,null,null, null, null, null, expected,null, null);
        assertEquals(policy.getInPolicy(),expected);
    }

    @Test
    public void getOutPolicy() {
        Policy expected = new Policy(Policy.TYPE_METHOD_AMOUNT, false,false,null, null, 5, null, null, null);
        Policy policy = new Policy(null,null,null, null, null, null,null, expected, null);
        assertEquals(policy.getOutPolicy(),expected);
    }

    @Test
    public void emptyParsesAsExpected() throws Exception {
        assertEquals(new Policy(null, null, null, null, null, null, null, null, null), new ObjectMapper().readValue("{}", Policy.class));
    }

    @Test
    public void emptySerializesAsExpected() throws Exception {
        assertEquals("{}", new ObjectMapper().writeValueAsString(new Policy(null, null, null, null, null, null, null, null, null)));
    }

    @Test
    public void factorsPolicyParsesAsExpected() throws Exception {
        String json = "{\"type\":\"FACTORS\",\"deny_rooted_jailbroken\":false,\"deny_emulator_simulator\":false," +
                "\"fences\":[{\"type\":\"GEO_CIRCLE\",\"name\":\"a GeoCircle Fence\",\"latitude\":1.0," +
                "\"longitude\":1.0,\"radius\":1.0},{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\"," +
                "\"country\":\"country\",\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}]," +
                "\"factors\":[\"INHERENCE\",\"POSSESSION\"]}";
        ArrayNode factors = new ArrayNode(JsonNodeFactory.instance);
        factors.add("INHERENCE");
        factors.add("POSSESSION");

        List<Fence> fences = new ArrayList<>();
        Fence geoCircleFence = new Fence("a GeoCircle Fence", Fence.TYPE_GEO_CIRCLE, 1.0,1.0,1.0, null, null, null);
        Fence territorialFence = new Fence("a Territory Fence", Fence.TYPE_TERRITORY, null, null, null, "country", "Admin Area", "ABCDE6");
        fences.add(geoCircleFence);
        fences.add(territorialFence);
        Policy expected = new Policy(Policy.TYPE_FACTORS, false,false, fences, factors, null, null, null, null);
        Policy actual = new ObjectMapper().readValue(json, Policy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void factorsPolicySerializesAsExpected() throws Exception {
        String expected = "{\"type\":\"FACTORS\",\"deny_rooted_jailbroken\":false,\"deny_emulator_simulator\":false," +
                "\"fences\":[{\"type\":\"GEO_CIRCLE\",\"name\":\"a GeoCircle Fence\",\"latitude\":1.0," +
                "\"longitude\":1.0,\"radius\":1.0},{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\"," +
                "\"country\":\"country\",\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}]," +
                "\"factors\":[\"INHERENCE\",\"POSSESSION\"]}";
        ArrayNode factors = new ArrayNode(JsonNodeFactory.instance);
        factors.add("INHERENCE");
        factors.add("POSSESSION");

        List<Fence> fences = new ArrayList<>();
        Fence geoCircleFence = new Fence("a GeoCircle Fence", Fence.TYPE_GEO_CIRCLE, 1.0,1.0,1.0, null, null, null);
        Fence territorialFence = new Fence("a Territory Fence", Fence.TYPE_TERRITORY, null, null, null, "country", "Admin Area", "ABCDE6");
        fences.add(geoCircleFence);
        fences.add(territorialFence);
        Policy policy = new Policy(Policy.TYPE_FACTORS, false,false, fences, factors, null, null, null, null);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }

    @Test
    public void amountParsesAsExpected() throws Exception {
        String json = "{\"type\":\"METHOD_AMOUNT\",\"deny_rooted_jailbroken\":true," +
                "\"deny_emulator_simulator\":true,\"fences\":[{\"type\":\"GEO_CIRCLE\"," +
                "\"name\":\"a GeoCircle Fence\",\"latitude\":1.0,\"longitude\":1.0,\"radius\":1.0}," +
                "{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\",\"country\":\"country\"," +
                "\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}],\"amount\":5}";

        List<Fence> fences = new ArrayList<>();
        Fence geoCircleFence = new Fence("a GeoCircle Fence", Fence.TYPE_GEO_CIRCLE, 1.0,1.0,1.0, null, null, null);
        Fence territorialFence = new Fence("a Territory Fence", Fence.TYPE_TERRITORY, null, null, null, "country", "Admin Area", "ABCDE6");
        fences.add(geoCircleFence);
        fences.add(territorialFence);
        Policy expected = new Policy(Policy.TYPE_METHOD_AMOUNT, true,true,fences,null, 5, null, null, null);
        Policy actual = new ObjectMapper().readValue(json, Policy.class);
        assertEquals(expected,actual);
    }

    @Test
    public void amountSerializesAsExpected() throws JsonProcessingException {
        String expected = "{\"type\":\"METHOD_AMOUNT\",\"deny_rooted_jailbroken\":true," +
                "\"deny_emulator_simulator\":true,\"fences\":[{\"type\":\"GEO_CIRCLE\"," +
                "\"name\":\"a GeoCircle Fence\",\"latitude\":1.0,\"longitude\":1.0,\"radius\":1.0}," +
                "{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\",\"country\":\"country\"," +
                "\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}],\"amount\":5}";

        List<Fence> fences = new ArrayList<>();
        Fence geoCircleFence = new Fence("a GeoCircle Fence", Fence.TYPE_GEO_CIRCLE, 1.0,1.0,1.0, null, null, null);
        Fence territorialFence = new Fence("a Territory Fence", Fence.TYPE_TERRITORY, null, null, null, "country", "Admin Area", "ABCDE6");
        fences.add(geoCircleFence);
        fences.add(territorialFence);
        Policy policy = new Policy(Policy.TYPE_METHOD_AMOUNT, true,true,fences,null, 5, null, null, null);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected,actual);
    }

    @Test
    public void condGeoParsesAsExpected() throws Exception {
        String json = "{\"type\":\"COND_GEO\",\"deny_rooted_jailbroken\":true,\"deny_emulator_simulator\":true," +
                "\"fences\":[{\"type\":\"GEO_CIRCLE\",\"name\":\"a GeoCircle Fence\",\"latitude\":1.0," +
                "\"longitude\":1.0,\"radius\":1.0},{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\"," +
                "\"country\":\"country\",\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}]," +
                "\"inside\":{\"type\":\"METHOD_AMOUNT\",\"amount\":5}," +
                "\"outside\":{\"type\":\"METHOD_AMOUNT\",\"amount\":10}}";

        List<Fence> fences = new ArrayList<>();
        Fence geoCircleFence = new Fence("a GeoCircle Fence", Fence.TYPE_GEO_CIRCLE, 1.0,1.0,1.0, null, null, null);
        Fence territorialFence = new Fence("a Territory Fence", Fence.TYPE_TERRITORY, null, null, null, "country", "Admin Area", "ABCDE6");
        fences.add(geoCircleFence);
        fences.add(territorialFence);
        Policy inPolicy = new Policy(Policy.TYPE_METHOD_AMOUNT, null,null,null, null, 5, null, null, null);
        Policy outPolicy = new Policy(Policy.TYPE_METHOD_AMOUNT, null,null,null, null, 10, null, null, null);

        Policy expected = new Policy(Policy.TYPE_COND_GEO, true,true,fences, null, null, inPolicy, outPolicy, null);
        Policy actual = new ObjectMapper().readValue(json, Policy.class);
        assertEquals(expected,actual);
    }

    @Test
    public void condGeoSerializesAsExpected() throws Exception {
        String expected = "{\"type\":\"COND_GEO\",\"deny_rooted_jailbroken\":true,\"deny_emulator_simulator\":true," +
                "\"fences\":[{\"type\":\"GEO_CIRCLE\",\"name\":\"a GeoCircle Fence\",\"latitude\":1.0," +
                "\"longitude\":1.0,\"radius\":1.0},{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\"," +
                "\"country\":\"country\",\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}]," +
                "\"inside\":{\"type\":\"METHOD_AMOUNT\",\"amount\":5}," +
                "\"outside\":{\"type\":\"METHOD_AMOUNT\",\"amount\":10}}";

        List<Fence> fences = new ArrayList<>();
        Fence geoCircleFence = new Fence("a GeoCircle Fence", Fence.TYPE_GEO_CIRCLE, 1.0,1.0,1.0, null, null, null);
        Fence territorialFence = new Fence("a Territory Fence", Fence.TYPE_TERRITORY, null, null, null, "country", "Admin Area", "ABCDE6");
        fences.add(geoCircleFence);
        fences.add(territorialFence);
        Policy inPolicy = new Policy(Policy.TYPE_METHOD_AMOUNT, null,null,null, null, 5, null, null, null);
        Policy outPolicy = new Policy(Policy.TYPE_METHOD_AMOUNT, null,null,null, null, 10, null, null, null);

        Policy policy = new Policy(Policy.TYPE_COND_GEO, true,true,fences, null, null, inPolicy, outPolicy, null);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected,actual);
    }

    @Test
    public void legacyParsesAsExpected() throws Exception {
        String json =
                "{" +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"any\":2," +
                        "\"knowledge\":1," +
                        "\"inherence\":1," +
                        "\"possession\":1" +
                        "}]," +
                        "\"factors\":[" +
                        "{" +
                        "\"factor\":\"geofence\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"locations\":[" +
                        "{\"radius\":1.1,\"latitude\":2.1,\"longitude\":3.1}," +
                        "{\"radius\":1.2,\"latitude\":2.2,\"longitude\":3.2}" +
                        "]" +
                        "}" +
                        "},{" +
                        "\"factor\":\"device integrity\"," +
                        "\"attributes\":{" +
                        "\"factor enabled\":\"1\"" +
                        "}" +
                        "},{" +
                        "\"factor\":\"timefence\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"time fences\":[" +
                        "{\"name\":\"tf1\",\"days\":[\"Monday\",\"Tuesday\",\"Wednesday\"],\"start hour\":0,\"end hour\":23,\"start minute\":0,\"end minute\":59,\"timezone\":\"America/Los_Angeles\"}," +
                        "{\"name\":\"tf2\",\"days\":[\"Thursday\",\"Friday\",\"Saturday\",\"Sunday\"],\"start hour\":0,\"end hour\":23,\"start minute\":0,\"end minute\":59,\"timezone\":\"America/Los_Angeles\"}" +
                        "]" +
                        "}" +
                        "}" +
                        "]" +
                        "}";

        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        ArrayNode expectedFactors = new ArrayNode(jnf) {{

            // Add geofence
            ObjectNode geofence = jnf.objectNode();
            geofence.set("factor", new TextNode("geofence"));
            geofence.set("requirement", new TextNode("forced requirement"));
            geofence.set("priority", new IntNode(1));
            ObjectNode geoFenceAttributes = jnf.objectNode();
            ArrayNode locations = new ArrayNode(jnf) {{
                ObjectNode location1 = jnf.objectNode();
                location1.set("radius", new DoubleNode(1.1));
                location1.set("latitude", new DoubleNode(2.1));
                location1.set("longitude", new DoubleNode(3.1));
                add(location1);
                ObjectNode location2 = jnf.objectNode();
                location2.set("radius", new DoubleNode(1.2));
                location2.set("latitude", new DoubleNode(2.2));
                location2.set("longitude", new DoubleNode(3.2));
                add(location2);
            }};
            geoFenceAttributes.set("locations", locations);
            geofence.set("attributes", geoFenceAttributes);
            add(geofence);

            // Add device integrity
            ObjectNode deviceIntegrity = jnf.objectNode();
            deviceIntegrity.set("factor", new TextNode("device integrity"));
            ObjectNode deviceIntegrityAttributes = jnf.objectNode();
            deviceIntegrityAttributes.set("factor enabled", new TextNode("1"));
            deviceIntegrity.set("attributes", deviceIntegrityAttributes);
            add(deviceIntegrity);

            // Add time fence
            ObjectNode timeFence = jnf.objectNode();
            timeFence.set("factor", new TextNode("timefence"));
            timeFence.set("requirement", new TextNode("forced requirement"));
            timeFence.set("priority", new IntNode(1));
            ObjectNode timeFenceAttributes = jnf.objectNode();
            timeFenceAttributes.set("time fences", new ArrayNode(jnf) {{
                ObjectNode fence1 = jnf.objectNode();
                fence1.set("name", new TextNode("tf1"));
                fence1.set("days", new ArrayNode(jnf) {{
                    add(new TextNode("Monday"));
                    add(new TextNode("Tuesday"));
                    add(new TextNode("Wednesday"));
                }});
                fence1.set("start hour", new IntNode(0));
                fence1.set("end hour", new IntNode(23));
                fence1.set("start minute", new IntNode(0));
                fence1.set("end minute", new IntNode(59));
                fence1.set("timezone", new TextNode("America/Los_Angeles"));
                add(fence1);
                ObjectNode fence2 = jnf.objectNode();
                fence2.set("name", new TextNode("tf2"));
                fence2.set("days", new ArrayNode(jnf) {{
                    add(new TextNode("Thursday"));
                    add(new TextNode("Friday"));
                    add(new TextNode("Saturday"));
                    add(new TextNode("Sunday"));
                }});
                fence2.set("start hour", new IntNode(0));
                fence2.set("end hour", new IntNode(23));
                fence2.set("start minute", new IntNode(0));
                fence2.set("end minute", new IntNode(59));
                fence2.set("timezone", new TextNode("America/Los_Angeles"));
                add(fence2);
            }});
            timeFence.set("attributes", timeFenceAttributes);
            add(timeFence);
        }};
        List<Policy.MinimumRequirement> expectedMinimumRequirements = new ArrayList<Policy.MinimumRequirement>(){{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 2, 1, 1, 1));
        }};
        Policy expected = new Policy(null, null, null, null, expectedFactors, null, null, null, expectedMinimumRequirements);
        Policy actual = new ObjectMapper().readValue(json, Policy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void legacySerializesAsExpected() throws Exception {
        String expected =
                "{" +
                       "\"factors\":[" +
                        "{" +
                        "\"factor\":\"geofence\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"locations\":[" +
                        "{\"radius\":1.1,\"latitude\":2.1,\"longitude\":3.1}," +
                        "{\"radius\":1.2,\"latitude\":2.2,\"longitude\":3.2}" +
                        "]" +
                        "}" +
                        "},{" +
                        "\"factor\":\"device integrity\"," +
                        "\"attributes\":{" +
                        "\"factor enabled\":\"1\"" +
                        "}" +
                        "},{" +
                        "\"factor\":\"timefence\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"time fences\":[" +
                        "{\"name\":\"tf1\",\"days\":[\"Monday\",\"Tuesday\",\"Wednesday\"],\"start hour\":0,\"end hour\":23,\"start minute\":0,\"end minute\":59,\"timezone\":\"America/Los_Angeles\"}," +
                        "{\"name\":\"tf2\",\"days\":[\"Thursday\",\"Friday\",\"Saturday\",\"Sunday\"],\"start hour\":0,\"end hour\":23,\"start minute\":0,\"end minute\":59,\"timezone\":\"America/Los_Angeles\"}" +
                        "]" +
                        "}" +
                        "}" +
                        "]," +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"any\":2," +
                        "\"knowledge\":1," +
                        "\"inherence\":1," +
                        "\"possession\":1" +
                        "}]" +
                        "}";

        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        ArrayNode expectedFactors = new ArrayNode(jnf) {{

            // Add geofence
            ObjectNode geofence = jnf.objectNode();
            geofence.set("factor", new TextNode("geofence"));
            geofence.set("requirement", new TextNode("forced requirement"));
            geofence.set("priority", new IntNode(1));
            ObjectNode geoFenceAttributes = jnf.objectNode();
            ArrayNode locations = new ArrayNode(jnf) {{
                ObjectNode location1 = jnf.objectNode();
                location1.set("radius", new DoubleNode(1.1));
                location1.set("latitude", new DoubleNode(2.1));
                location1.set("longitude", new DoubleNode(3.1));
                add(location1);
                ObjectNode location2 = jnf.objectNode();
                location2.set("radius", new DoubleNode(1.2));
                location2.set("latitude", new DoubleNode(2.2));
                location2.set("longitude", new DoubleNode(3.2));
                add(location2);
            }};
            geoFenceAttributes.set("locations", locations);
            geofence.set("attributes", geoFenceAttributes);
            add(geofence);

            // Add device integrity
            ObjectNode deviceIntegrity = jnf.objectNode();
            deviceIntegrity.set("factor", new TextNode("device integrity"));
            ObjectNode deviceIntegrityAttributes = jnf.objectNode();
            deviceIntegrityAttributes.set("factor enabled", new TextNode("1"));
            deviceIntegrity.set("attributes", deviceIntegrityAttributes);
            add(deviceIntegrity);

            // Add time fence
            ObjectNode timeFence = jnf.objectNode();
            timeFence.set("factor", new TextNode("timefence"));
            timeFence.set("requirement", new TextNode("forced requirement"));
            timeFence.set("priority", new IntNode(1));
            ObjectNode timeFenceAttributes = jnf.objectNode();
            timeFenceAttributes.set("time fences", new ArrayNode(jnf) {{
                ObjectNode fence1 = jnf.objectNode();
                fence1.set("name", new TextNode("tf1"));
                fence1.set("days", new ArrayNode(jnf) {{
                    add(new TextNode("Monday"));
                    add(new TextNode("Tuesday"));
                    add(new TextNode("Wednesday"));
                }});
                fence1.set("start hour", new IntNode(0));
                fence1.set("end hour", new IntNode(23));
                fence1.set("start minute", new IntNode(0));
                fence1.set("end minute", new IntNode(59));
                fence1.set("timezone", new TextNode("America/Los_Angeles"));
                add(fence1);
                ObjectNode fence2 = jnf.objectNode();
                fence2.set("name", new TextNode("tf2"));
                fence2.set("days", new ArrayNode(jnf) {{
                    add(new TextNode("Thursday"));
                    add(new TextNode("Friday"));
                    add(new TextNode("Saturday"));
                    add(new TextNode("Sunday"));
                }});
                fence2.set("start hour", new IntNode(0));
                fence2.set("end hour", new IntNode(23));
                fence2.set("start minute", new IntNode(0));
                fence2.set("end minute", new IntNode(59));
                fence2.set("timezone", new TextNode("America/Los_Angeles"));
                add(fence2);
            }});
            timeFence.set("attributes", timeFenceAttributes);
            add(timeFence);
        }};
        List<Policy.MinimumRequirement> expectedMinimumRequirements = new ArrayList<Policy.MinimumRequirement>(){{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 2, 1, 1, 1));
        }};
        Policy policy = new Policy(null, null, null, null, expectedFactors, null, null, null, expectedMinimumRequirements);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }

    @Test
    public void legacyParsesNoMinimumRequirementsWhenThereAreNone() throws Exception {
        String json =
                "{" +
                        "\"minimum_requirements\":[]," +
                        "\"factors\":[" +
                        "{" +
                        "\"factor\":\"device integrity\"," +
                        "\"attributes\":{" +
                        "\"factor enabled\":\"1\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}";
        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        ArrayNode expectedFactors = new ArrayNode(JsonNodeFactory.instance) {{
            ObjectNode deviceIntegrity = jnf.objectNode();
            deviceIntegrity.set("factor", new TextNode("device integrity"));
            ObjectNode attributes = jnf.objectNode();
            attributes.set("factor enabled", new TextNode("1"));
            deviceIntegrity.set("attributes", attributes);
            add(deviceIntegrity);
        }};
        List<Policy.MinimumRequirement> expectedMinimumRequirements = new ArrayList<>();
        Policy expected = new Policy(null, null, null, null, expectedFactors, null, null, null, expectedMinimumRequirements);
        Policy actual = new ObjectMapper().readValue(json, Policy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void legacySerializesEmptyMinimumRequirementsWhenThereAreNone() throws Exception {
        String expected =
                "{" +
                        "\"factors\":[" +
                        "{" +
                        "\"factor\":\"device integrity\"," +
                        "\"attributes\":{" +
                        "\"factor enabled\":\"1\"" +
                        "}" +
                        "}" +
                        "]," +
                        "\"minimum_requirements\":[]" +
                        "}";
        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        ArrayNode expectedFactors = new ArrayNode(JsonNodeFactory.instance) {{
            ObjectNode deviceIntegrity = jnf.objectNode();
            deviceIntegrity.set("factor", new TextNode("device integrity"));
            ObjectNode attributes = jnf.objectNode();
            attributes.set("factor enabled", new TextNode("1"));
            deviceIntegrity.set("attributes", attributes);
            add(deviceIntegrity);
        }};
        List<Policy.MinimumRequirement> expectedMinimumRequirements = new ArrayList<>();
        Policy policy = new Policy(null, null, null, null, expectedFactors, null, null, null, expectedMinimumRequirements);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }

    @Test
    public void legacyParsesMinimumRequirementsAllOnlyWhenOnlySpecified() throws Exception {
        String json =
                "{" +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"any\":2" +
                        "}]," +
                        "\"factors\":[]" +
                        "}";
        ArrayNode expectedFactors = new ArrayNode(JsonNodeFactory.instance);
        List<Policy.MinimumRequirement> expectedMinimumRequirements = new ArrayList<Policy.MinimumRequirement>(){{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 2, null, null, null));
        }};
        Policy expected = new Policy(null, null, null, null, expectedFactors, null, null, null, expectedMinimumRequirements);
        Policy actual = new ObjectMapper().readValue(json, Policy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void legacySerializesMinimumRequirementsAllOnlyWhenOnlySpecified() throws Exception {
        String expected =
                "{" +
                        "\"factors\":[]," +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"any\":2" +
                        "}]" +
                        "}";
        ArrayNode expectedFactors = new ArrayNode(JsonNodeFactory.instance);
        List<Policy.MinimumRequirement> expectedMinimumRequirements = new ArrayList<Policy.MinimumRequirement>(){{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 2, null, null, null));
        }};
        Policy policy = new Policy(null, null, null, null, expectedFactors, null, null, null, expectedMinimumRequirements);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }

    @Test
    public void legacyParsesMinimumRequirementsTypesOnlyWhenOnlySpecified() throws Exception {
        String json =
                "{" +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"knowledge\":1," +
                        "\"inherence\":1," +
                        "\"possession\":1" +
                        "}]," +
                        "\"factors\":[]" +
                        "}";
        ArrayNode expectedFactors = new ArrayNode(JsonNodeFactory.instance);
        List<Policy.MinimumRequirement> expectedMinimumRequirements = new ArrayList<Policy.MinimumRequirement>(){{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, null, 1, 1, 1));
        }};
        Policy expected = new Policy(null, null, null, null, expectedFactors, null, null, null, expectedMinimumRequirements);
        Policy actual = new ObjectMapper().readValue(json, Policy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void legacySerializesMinimumRequirementsTypesOnlyWhenOnlySpecified() throws Exception {
        String expected =
                "{" +
                        "\"factors\":[]," +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"knowledge\":1," +
                        "\"inherence\":1," +
                        "\"possession\":1" +
                        "}]" +
                        "}";
        ArrayNode expectedFactors = new ArrayNode(JsonNodeFactory.instance);
        List<Policy.MinimumRequirement> expectedMinimumRequirements = new ArrayList<Policy.MinimumRequirement>(){{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, null, 1, 1, 1));
        }};
        Policy policy = new Policy(null, null, null, null, expectedFactors, null, null, null, expectedMinimumRequirements);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }
}

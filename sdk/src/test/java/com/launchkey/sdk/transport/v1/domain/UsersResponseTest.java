package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.transport.v1.domain.UsersResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class UsersResponseTest {
    private UsersResponse usersResponse;

    @Before
    public void setUp() throws Exception {
        usersResponse = new UsersResponse(new UsersResponse.UsersResponseResponse(
                "JOjdtzt4KpTHZqoKg7N1NYhazuqW8THrjkZ0J2GEGT9I/aagUZ+w9hYuJpHROwIwJHDS6gLjMxNU\n" +
                        "ZX3hH+hMq70QRvF/9Yu1Ujq8TJO3ETqO74yQ53nHITwvQc4SEEPmtCzaBPon69yUc4vO0x0AvofV\n" +
                        "+QAShU6ZWWDvFcC/czRxPV8CpfrMcgEbojmGjOMN7U+rhrzuw7yhTDe33GXSX7UlAZnYCkDiP7Fd\n" +
                        "QvEHIzve+gxIzZwQK4a+HRJTbw6ms/PxyjcYrw/EhGTewCICLNeKksBjkQfLfIHbpOJiPZ75NCg7\n" +
                        "hjysYgEyHRVDn1jUyw4TtFOLb2tSHDmBM+DWWA==\n",
                "1Umh/uC3oHAgzYSX3TB+4qGT0d1Z1ATczUAYYUiqj7eInHxmrV4cLuRK+La7O4P8ss8NH2TPc1zzQ2o7w6KGA4" +
                        "ulLtTKcQYiifHaZKQhtuT6gvo1I7Jlgdr/Exs/5F0Q"
        ));

    }

    @After
    public void tearDown() throws Exception {
        usersResponse = null;
    }

    @Test
    public void testGetCipher() throws Exception {
        assertEquals(
                "JOjdtzt4KpTHZqoKg7N1NYhazuqW8THrjkZ0J2GEGT9I/aagUZ+w9hYuJpHROwIwJHDS6gLjMxNU\n" +
                        "ZX3hH+hMq70QRvF/9Yu1Ujq8TJO3ETqO74yQ53nHITwvQc4SEEPmtCzaBPon69yUc4vO0x0AvofV\n" +
                        "+QAShU6ZWWDvFcC/czRxPV8CpfrMcgEbojmGjOMN7U+rhrzuw7yhTDe33GXSX7UlAZnYCkDiP7Fd\n" +
                        "QvEHIzve+gxIzZwQK4a+HRJTbw6ms/PxyjcYrw/EhGTewCICLNeKksBjkQfLfIHbpOJiPZ75NCg7\n" +
                        "hjysYgEyHRVDn1jUyw4TtFOLb2tSHDmBM+DWWA==\n",
                usersResponse.getCipher());
    }

    @Test
    public void testGetData() throws Exception {
        assertEquals(
                "1Umh/uC3oHAgzYSX3TB+4qGT0d1Z1ATczUAYYUiqj7eInHxmrV4cLuRK+La7O4P8ss8NH2TPc1zzQ2o7w6KGA4" +
                        "ulLtTKcQYiifHaZKQhtuT6gvo1I7Jlgdr/Exs/5F0Q",
                usersResponse.getData());
    }

    @Test
    public void testJSONParseable() throws Exception {
        String json = "{\"successful\": true, \"status_code\": 201, \"message\": \"\", \"message_code\": 10220," +
                " \"response\": {\"cipher\": \"JOjdtzt4KpTHZqoKg7N1NYhazuqW8THrjkZ0J2GEGT9I/aagUZ+w9hYuJpHROwIwJHDS6gLjMxNU\\n" +
                "ZX3hH+hMq70QRvF/9Yu1Ujq8TJO3ETqO74yQ53nHITwvQc4SEEPmtCzaBPon69yUc4vO0x0AvofV\\n" +
                "+QAShU6ZWWDvFcC/czRxPV8CpfrMcgEbojmGjOMN7U+rhrzuw7yhTDe33GXSX7UlAZnYCkDiP7Fd\\n" +
                "QvEHIzve+gxIzZwQK4a+HRJTbw6ms/PxyjcYrw/EhGTewCICLNeKksBjkQfLfIHbpOJiPZ75NCg7\\n" +
                "hjysYgEyHRVDn1jUyw4TtFOLb2tSHDmBM+DWWA==\\n" +
                "\",\"data\": \"1Umh/uC3oHAgzYSX3TB+4qGT0d1Z1ATczUAYYUiqj7eInHxmrV4cLuRK+La7O4P8" +
                "ss8NH2TPc1zzQ2o7w6KGA4ulLtTKcQYiifHaZKQhtuT6gvo1I7Jlgdr/Exs/5F0Q\"}}";

        ObjectMapper mapper = new ObjectMapper();
        UsersResponse actual = mapper.readValue(json, UsersResponse.class);
        assertEquals(usersResponse, actual);
    }


    @Test
    public void testJSONParseAllowsUnknown() throws Exception {
        String json = "{\"successful\": true, \"status_code\": 201, \"message\": \"\", \"message_code\": 10220," +
                " \"response\": {\"cipher\": \"JOjdtzt4KpTHZqoKg7N1NYhazuqW8THrjkZ0J2GEGT9I/aagUZ+w9hYuJpHROwIwJHDS6gLjMxNU\\n" +
                "ZX3hH+hMq70QRvF/9Yu1Ujq8TJO3ETqO74yQ53nHITwvQc4SEEPmtCzaBPon69yUc4vO0x0AvofV\\n" +
                "+QAShU6ZWWDvFcC/czRxPV8CpfrMcgEbojmGjOMN7U+rhrzuw7yhTDe33GXSX7UlAZnYCkDiP7Fd\\n" +
                "QvEHIzve+gxIzZwQK4a+HRJTbw6ms/PxyjcYrw/EhGTewCICLNeKksBjkQfLfIHbpOJiPZ75NCg7\\n" +
                "hjysYgEyHRVDn1jUyw4TtFOLb2tSHDmBM+DWWA==\\n" +
                "\",\"data\": \"1Umh/uC3oHAgzYSX3TB+4qGT0d1Z1ATczUAYYUiqj7eInHxmrV4cLuRK+La7O4P8" +
                "ss8NH2TPc1zzQ2o7w6KGA4ulLtTKcQYiifHaZKQhtuT6gvo1I7Jlgdr/Exs/5F0Q\", \"unknown\": \"Unknown Value\"}," +
                " \"unknown\": \"Unknown Value\"}";

        ObjectMapper mapper = new ObjectMapper();
        UsersResponse actual = mapper.readValue(json, UsersResponse.class);
        assertEquals(usersResponse, actual);
    }

    @Test
    public void testEqualObjectsReturnTrueForEquals() throws Exception {
        UsersResponse left = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher", "data"));
        UsersResponse right = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher", "data"));
        assertTrue(left.equals(right));
    }

    @Test
    public void testNotEqualObjectsReturnFalseForEquals() throws Exception {
        UsersResponse left = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher", "data"));
        UsersResponse right = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher2", "data"));
        assertFalse(left.equals(right));
    }

    @Test
    public void testEqualObjectsReturnSameHashCode() throws Exception {
        UsersResponse left = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher", "data"));
        UsersResponse right = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher", "data"));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualObjectsReturnDifferentHashCode() throws Exception {
        UsersResponse left = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher", "data"));
        UsersResponse right = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher2", "data"));
        assertNotEquals(left.hashCode(), right.hashCode());
    }


    @Test
    public void testToStringContainsClassName() throws Exception {
        UsersResponse usersResponse = new UsersResponse(new UsersResponse.UsersResponseResponse("cipher", "data"));
        assertThat(usersResponse.toString(), containsString(UsersResponse.class.getSimpleName()));
    }
}
package com.iovation.launchkey.sdk.integration.managers.kobiton.transport.httpurlconnection;

import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.GenericHeader;
import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.GenericRequest;
import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.GenericResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionRequest extends GenericRequest {

    private HttpURLConnectionRequest() {}


    @Override
    public GenericResponse send() {
        GenericResponse response;
        try {
            // url
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // method
            con.setRequestMethod(type.toString());

            // headers
            for (GenericHeader header : headers) {
                con.setRequestProperty(header.key, header.value);
            }

            // body
            if (body != null) {
                con.setDoOutput(true);
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            // file
            if (filename != null) {
                con.setDoOutput(true);
                con.connect();
                try(BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
                    FileInputStream bis = new FileInputStream(new File(filename))) {
                    int i;
                    while ((i = bis.read()) != -1) {
                        bos.write(i);
                    }
                }
            }

            // response
            int responseCode = con.getResponseCode();
            StringBuilder responseBody = new StringBuilder();
            try(BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBody.append(inputLine);
                }
            }
            response = new GenericResponse(responseCode, responseBody.toString());

            if (filename != null) {
                con.disconnect();
            }
        } catch (Exception e) {
            response = null;
        }

        return response;
    }

    public static class Builder extends GenericRequest.Builder {

        public Builder() {
            request = new HttpURLConnectionRequest();
        }
    }

}

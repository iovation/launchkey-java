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

package com.iovation.launchkey.sdk.transport.apachehttp;

import org.apache.http.*;
import org.apache.http.params.HttpParams;

import java.io.*;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class ReplayHttpResponse implements HttpResponse {
    private final HttpResponse httpResponse;
    private HttpEntity entityCache = null;
    private int entityCacheHashCode = 0;

    ReplayHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return httpResponse.getProtocolVersion();
    }

    @Override
    public boolean containsHeader(String name) {
        return httpResponse.containsHeader(name);
    }

    @Override
    public Header[] getHeaders(String name) {
        return httpResponse.getHeaders(name);
    }

    @Override
    public Header getFirstHeader(String name) {
        return httpResponse.getFirstHeader(name);
    }

    @Override
    public Header getLastHeader(String name) {
        return httpResponse.getLastHeader(name);
    }

    @Override
    public Header[] getAllHeaders() {
        return httpResponse.getAllHeaders();
    }

    @Override
    public void addHeader(Header header) {
        httpResponse.addHeader(header);
    }

    @Override
    public void addHeader(String name, String value) {
        httpResponse.addHeader(name, value);
    }

    @Override
    public void setHeader(Header header) {
        httpResponse.setHeader(header);
    }

    @Override
    public void setHeader(String name, String value) {
        httpResponse.setHeader(name, value);
    }

    @Override
    public void setHeaders(Header[] headers) {
        httpResponse.setHeaders(headers);
    }

    @Override
    public void removeHeader(Header header) {
        httpResponse.removeHeader(header);
    }

    @Override
    public void removeHeaders(String name) {
        httpResponse.removeHeaders(name);
    }

    @Override
    public HeaderIterator headerIterator() {
        return httpResponse.headerIterator();
    }

    @Override
    public HeaderIterator headerIterator(String name) {
        return httpResponse.headerIterator(name);
    }

    @Override
    public HttpParams getParams() {
        return httpResponse.getParams();
    }

    @Override
    public void setParams(HttpParams params) {
        httpResponse.setParams(params);
    }

    @Override
    public StatusLine getStatusLine() {
        return httpResponse.getStatusLine();
    }

    @Override
    public void setStatusLine(StatusLine statusline) {
        httpResponse.setStatusLine(statusline);
    }

    @Override
    public void setStatusLine(ProtocolVersion ver, int code) {
        httpResponse.setStatusLine(ver, code);
    }

    @Override
    public void setStatusLine(ProtocolVersion ver, int code, String reason) {
        httpResponse.setStatusLine(ver, code, reason);
    }

    @Override
    public void setStatusCode(int code) throws IllegalStateException {
        httpResponse.setStatusCode(code);
    }

    @Override
    public void setReasonPhrase(String reason) throws IllegalStateException {
        httpResponse.setReasonPhrase(reason);
    }

    @Override
    public synchronized HttpEntity getEntity() {
        if (entityCache == null && httpResponse.getEntity() != null) {
            entityCache = new RepeatingHttpEntity(httpResponse.getEntity());
        }
        return entityCache;
    }

    @Override
    public void setEntity(HttpEntity entity) {
        httpResponse.setEntity(entity);
        entityCache = null;
    }

    @Override
    public Locale getLocale() {
        return httpResponse.getLocale();
    }

    @Override
    public void setLocale(Locale loc) {
        httpResponse.setLocale(loc);
    }

    @SuppressWarnings("deprecation")
    private class RepeatingHttpEntity implements HttpEntity {
        private final HttpEntity httpEntity;
        private byte[] content = new byte[0];

        public RepeatingHttpEntity(HttpEntity httpEntity) {
            this.httpEntity = httpEntity;
        }

        @Override
        public boolean isRepeatable() {
            return true;
        }

        @Override
        public boolean isChunked() {
            return false;
        }

        @Override
        public long getContentLength() {
            return httpEntity.getContentLength();
        }

        @Override
        public Header getContentType() {
            return httpEntity.getContentType();
        }

        @Override
        public Header getContentEncoding() {
            return httpEntity.getContentEncoding();
        }

        @Override
        public InputStream getContent() throws IOException, UnsupportedOperationException {
            generateContent();
            return new ByteArrayInputStream(content);
        }

        @Override
        public void writeTo(OutputStream outstream) throws IOException {
            generateContent();
            outstream.write(content);
        }

        @Override
        public boolean isStreaming() {
            return false;
        }

        @Override
        public void consumeContent() throws IOException {
            httpEntity.consumeContent();
        }

        private void generateContent() throws IOException {
            if (content.length == 0) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                httpEntity.writeTo(out);
                content = out.toByteArray();
            }
        }
    }
}

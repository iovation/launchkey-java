package com.iovation.launchkey.sdk.integration.managers.kobiton.transport;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericRequest {

    public static String CONTENT_TYPE = "Content-Type";
    public static String APPLICATION_JSON = "application/json";
    public static String APPLICATION_OCTET_STREAM = "application/octet-stream";

    public enum RequestType { GET, POST, PUT, DELETE;
        @Override
        public String toString() {
            switch(this) {
                case GET:
                    return "GET";
                case POST:
                    return "POST";
                case PUT:
                    return "PUT";
                case DELETE:
                    return "DELETE";
            }
            return "";
        }
    }

    protected RequestType type;
    protected String url;
    protected List<GenericHeader> headers = new ArrayList<>();
    protected String body;
    protected String filename;

    abstract public GenericResponse send();

    // This is just a goofy thing I wanted to try out.
    // It's a Step Builder class without regard to order
    // of the steps, lots of unfortunate code duplication
    // because return type conflicts. Still kind of cool.
    private static abstract class BuilderImpl<T> {

        protected GenericRequest request;

        public T addContentTypeJSONHeader() {
            request.headers.add(new GenericHeader(CONTENT_TYPE, APPLICATION_JSON));
            return (T) this;
        }

        public T addHeader(GenericHeader header) {
            request.headers.add(header);
            return (T) this;
        }

        public T setBody(String body) {
            request.body = body;
            return (T) this;
        }

        public T setBodyToFile(String filename) {
            request.filename = filename;
            return (T) this;
        }
    }

    public static class Builder extends BuilderImpl<Builder> {

        public RequestTypeSetBuilder setRequestType(RequestType type) {
            request.type = type;
            return new RequestTypeSetBuilder(this);
        }

        public UrlSetBuilder setURL(String url) {
            request.url = url;
            return new UrlSetBuilder(this);
        }
    }

    public static final class UrlSetBuilder extends BuilderImpl<UrlSetBuilder> {

        private UrlSetBuilder(BuilderImpl builder) {
            this.request = builder.request;
        }

        public FinalBuilder setRequestType(RequestType type) {
            request.type = type;
            return new FinalBuilder(this);
        }

        public UrlSetBuilder setURL(String url) {
            request.url = url;
            return this;
        }
    }

    public static final class RequestTypeSetBuilder extends BuilderImpl<RequestTypeSetBuilder> {

        private RequestTypeSetBuilder(BuilderImpl builder) {
            this.request = builder.request;
        }

        public RequestTypeSetBuilder setRequestType(RequestType type) {
            request.type = type;
            return this;
        }

        public FinalBuilder setURL(String url) {
            request.url = url;
            return new FinalBuilder(this);
        }
    }

    public static class FinalBuilder extends BuilderImpl<FinalBuilder> {

        private FinalBuilder(BuilderImpl builder) {
            this.request = builder.request;
        }

        public FinalBuilder setRequestType(RequestType type) {
            request.type = type;
            return this;
        }

        public FinalBuilder setURL(String url) {
            request.url = url;
            return this;
        }

        public GenericRequest build() {
            return this.request;
        }
    }
}

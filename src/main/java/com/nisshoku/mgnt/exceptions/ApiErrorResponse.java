package com.nisshoku.mgnt.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiErrorResponse {

    private Integer errorCode;
    private HttpStatus status;
    private String errorMessage;
    private String uri;

    public static final class ApiErrorResponseBuilder {

        private Integer errorCode;
        private HttpStatus status;
        private String errorMessage;
        private String uri;

        ApiErrorResponseBuilder() {}

        public static ApiErrorResponseBuilder anApiErrorResponse() {
            return new ApiErrorResponseBuilder();
        }

        ApiErrorResponseBuilder withStatus(HttpStatus status) {
            this.status = status;
            this.errorCode = status.value();
            return this;
        }

        ApiErrorResponseBuilder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        ApiErrorResponseBuilder withUri(String uri) {
            this.uri = uri;
            return this;
        }

        public ApiErrorResponse build() {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
            apiErrorResponse.status = this.status;
            apiErrorResponse.errorCode = this.errorCode;
            apiErrorResponse.errorMessage = this.errorMessage;
            apiErrorResponse.uri = this.uri;
            return apiErrorResponse;
        }
    }
}

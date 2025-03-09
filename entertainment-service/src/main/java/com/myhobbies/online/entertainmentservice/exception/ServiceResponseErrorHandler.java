package com.myhobbies.online.entertainmentservice.exception;

import com.myhobbies.online.entertainmentservice.config.ExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class ServiceResponseErrorHandler implements ResponseErrorHandler {

    private final ExternalService externalService;

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().is4xxClientError()
                || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse httpResponse) throws IOException {

        String body = new String(httpResponse.getBody().readAllBytes(), StandardCharsets.UTF_8);
        throw new EntertainmentServiceException(externalService.name() + " API returned error: " + body);
    }
}
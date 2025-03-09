package com.myhobbies.online.booksqueryservice.config.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class OAuthClientCredentialsWebClientFilter implements ExchangeFilterFunction {

    private final OAuth2AuthorizedClientManager clientManager;
    private final ClientRegistration clientRegistration;

    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(clientRegistration.getRegistrationId())
                .principal(clientRegistration.getClientId())
                .build();
        log.info("Getting Bearer token for clientRegistration={} and principal={}",
                clientRegistration.getRegistrationId(), clientRegistration.getClientId());
        if (log.isDebugEnabled()) {
            log.debug(
                    "Getting Bearer token for clientRegistration={} and principal={}",
                    clientRegistration.getRegistrationId(), clientRegistration.getClientId()
            );
        }

        OAuth2AuthorizedClient authorizedClient = clientManager.authorize(oAuth2AuthorizeRequest);

        if (authorizedClient != null &&
            authorizedClient.getAccessToken() != null ) {
            String tokenValue = authorizedClient.getAccessToken().getTokenValue();
            if (log.isDebugEnabled()) {
                log.debug("Bearer token: {}", tokenValue);
            }
            ClientRequest newRequest = ClientRequest.from(request)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                    .build();
            return next.exchange(newRequest);
        } else {
            log.warn("Failed to get OAuth token for '{}'", clientRegistration.getRegistrationId());
            return next.exchange(request);
        }
    }
}

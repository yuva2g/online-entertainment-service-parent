package com.myhobbies.online.booksqueryservice.config.oauth2;

import com.myhobbies.online.booksqueryservice.config.googlebooksapi.GoogleBooksConnectionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import static java.util.Optional.ofNullable;

@Configuration
@RequiredArgsConstructor
public class OAuth2Config {

    private final GoogleBooksConnectionProperties googleBooksConnectionProperties;

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                 OAuth2AuthorizedClientService clientService) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
    }

    @Bean
    public ExchangeFilterFunction oAuthClientCredentialsWebClientFilter(
            OAuth2AuthorizedClientManager authorizedClientManager,
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        ClientRegistration clientRegistration = ofNullable(clientRegistrationRepository.findByRegistrationId(googleBooksConnectionProperties.getClientRegistrationId()))
                .orElseThrow(() -> new IllegalStateException("Client Registration is null for clientRegistrationId: google"));
        return new OAuthClientCredentialsWebClientFilter(authorizedClientManager, clientRegistration);
    }
}

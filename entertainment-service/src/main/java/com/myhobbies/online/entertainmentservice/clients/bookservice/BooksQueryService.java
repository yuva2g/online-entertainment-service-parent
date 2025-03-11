package com.myhobbies.online.entertainmentservice.clients.bookservice;

import com.myhobbies.online.entertainmentservice.clients.bookservice.response.Book;
import com.myhobbies.online.entertainmentservice.clients.bookservice.response.BooksResponseTranslator;
import com.myhobbies.online.entertainmentservice.config.ExternalService;
import com.myhobbies.online.entertainmentservice.config.booksqueryservice.BooksQueryServiceProperties;
import com.myhobbies.online.entertainmentservice.exception.ExternalServiceException;
import com.myhobbies.online.entertainmentservice.models.Entertainment;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.SupplierUtils;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class BooksQueryService {

    private final RestTemplate booksQueryRestTemplate;

    private final CircuitBreaker booksQueryCircuitBreaker;

    private final BooksQueryServiceProperties booksQueryServiceProperties;

    @Timed(value = "http.books-query-service.request", extraTags = {"backend", "books-query-service", "operation", "getBooks"})
    public List<Entertainment> getBooks(String searchText, Integer limit) {

        int limitValue = limit == null ? booksQueryServiceProperties.getDefaultResultLimit() : limit;
        Supplier<List<Entertainment>> responseSupplier = getBooksResultsSupplier(searchText, limitValue);

        return booksQueryCircuitBreaker.executeSupplier(SupplierUtils.recover(responseSupplier, exception -> {
            log.warn("Error while retrieving books from books-query-service {}", exception.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("Error details while retrieving books from books-query-service", exception);
            }
            return Collections.emptyList();
        }));
    }

    private Supplier<List<Entertainment>> getBooksResultsSupplier(String searchText, int limitValue) {

        return CircuitBreaker.decorateSupplier(booksQueryCircuitBreaker, () -> {
            try {
                ResponseEntity<List<Book>> responseEntity =
                        booksQueryRestTemplate.exchange(
                                "/books?searchText=" + searchText + "&limit=" + limitValue,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<>() {
                                }
                        );
                List<Book> book = responseEntity.getBody();
                return BooksResponseTranslator.mapToEntertainments(book);
            } catch (Exception e) {
                throw new ExternalServiceException(e.getMessage(), ExternalService.BOOKS_QUERY_SERVICE, e);
            }
        });
    }
}

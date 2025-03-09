package com.myhobbies.online.entertainmentservice.clients.bookservice;

import com.myhobbies.online.entertainmentservice.clients.bookservice.response.Book;
import com.myhobbies.online.entertainmentservice.models.Entertainment;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class BooksQueryServiceTest {

    private BooksQueryService booksQueryService;
    private RestTemplate restTemplate;
    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        circuitBreaker = Mockito.mock(CircuitBreaker.class);
        booksQueryService = new BooksQueryService(restTemplate, circuitBreaker);
    }

    @Test
    void getBooks_ValidRequest_ReturnsBooks() {
        Book book = new Book("Test Title", List.of("Test Author"));
        List<Book> books = List.of(book);
        ResponseEntity<List<Book>> responseEntity = ResponseEntity.ok(books);

        Mockito.when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenAnswer(invocation -> ((Supplier<?>) invocation.getArgument(0)).get());

        List<Entertainment> result = booksQueryService.getBooks("Test");

        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());
        assertEquals("Test Author", result.get(0).getAuthors().get(0));
    }

    @Test
    void getBooks_EmptyResponse_ReturnsEmptyList() {
        ResponseEntity<List<Book>> responseEntity = ResponseEntity.ok(Collections.emptyList());

        Mockito.when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenAnswer(invocation -> ((Supplier<?>) invocation.getArgument(0)).get());

        List<Entertainment> result = booksQueryService.getBooks("Test");

        assertEquals(0, result.size());
    }

    @Test
    void getBooks_ApiThrowsException_FallbackWithEmptyResult() {
        Mockito.when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Service unavailable"));
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenAnswer(invocation -> ((Supplier<?>) invocation.getArgument(0)).get());

        List<Entertainment> result = booksQueryService.getBooks("Test");
        assertEquals(0, result.size());
    }
}
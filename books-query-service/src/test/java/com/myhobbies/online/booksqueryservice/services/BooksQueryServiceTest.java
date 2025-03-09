package com.myhobbies.online.booksqueryservice.services;

import com.myhobbies.online.booksqueryservice.client.GoogleBooksAPIClient;
import com.myhobbies.online.booksqueryservice.client.models.Volume;
import com.myhobbies.online.booksqueryservice.client.models.VolumeInfo;
import com.myhobbies.online.booksqueryservice.client.models.Volumes;
import com.myhobbies.online.booksqueryservice.exception.GoogleBooksAPIServiceException;
import com.myhobbies.online.booksqueryservice.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BooksQueryServiceTest {

    private BooksQueryService booksQueryService;
    private GoogleBooksAPIClient googleBooksAPIClient;

    @BeforeEach
    void setUp() {
        googleBooksAPIClient = Mockito.mock(GoogleBooksAPIClient.class);
        booksQueryService = new BooksQueryService(googleBooksAPIClient);
    }

    @Test
    void getBooks_ValidRequest_ReturnsBooks() {
        VolumeInfo volumeInfo = new VolumeInfo("Test Title", List.of("Test Author"));
        Volume volume = new Volume(volumeInfo);
        Volumes volumes = new Volumes(List.of(volume));

        when(googleBooksAPIClient.getVolumes(anyString(), anyInt())).thenReturn(volumes);

        List<Book> result = booksQueryService.getBooks("Test", 5);

        assertEquals(1, result.size());
        assertEquals("Test Title", result.getFirst().title());
        assertEquals("Test Author", result.getFirst().authors().getFirst());
    }

    @Test
    void getBooks_EmptyResponse_ReturnsEmptyBooks() {
        Volumes volumes = new Volumes(Collections.emptyList());

        when(googleBooksAPIClient.getVolumes(anyString(), anyInt())).thenReturn(volumes);

        List<Book> result = booksQueryService.getBooks("Test", 5);

        assertEquals(0, result.size());
    }

    @Test
    void getBooks_ApiThrowsException_ThrowsGoogleBooksAPIServiceException() {
        when(googleBooksAPIClient.getVolumes(anyString(), anyInt())).thenThrow(new GoogleBooksAPIServiceException("Google Books API is currently unavailable"));

        assertThrows(GoogleBooksAPIServiceException.class, () -> booksQueryService.getBooks("Test", 5));
    }
}
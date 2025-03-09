package com.myhobbies.online.booksqueryservice.services;

import com.myhobbies.online.booksqueryservice.client.GoogleBooksAPIClient;
import com.myhobbies.online.booksqueryservice.client.models.Volumes;
import com.myhobbies.online.booksqueryservice.models.Book;
import com.myhobbies.online.booksqueryservice.models.BooksMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BooksQueryService {

    @Value("${google.books.default-result-limit}")
    private int defaultResultLimit;

    private final GoogleBooksAPIClient googleBooksAPIClient;

    public List<Book> getBooks(String searchText, Integer limit) {

        Volumes volumes = googleBooksAPIClient.getVolumes(searchText, getLimit(limit));
        return BooksMapper.mapToBooks(volumes.items());
    }

    private int getLimit(Integer limit) {
        return limit != null ? limit : defaultResultLimit;
    }
}

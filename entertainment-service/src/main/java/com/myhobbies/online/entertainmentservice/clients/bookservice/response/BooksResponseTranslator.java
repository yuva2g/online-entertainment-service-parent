package com.myhobbies.online.entertainmentservice.clients.bookservice.response;

import com.myhobbies.online.entertainmentservice.models.Entertainment;
import com.myhobbies.online.entertainmentservice.models.EntertainmentType;

import java.util.List;

public class BooksResponseTranslator {

    public static List<Entertainment> mapToEntertainments(List<Book> books) {
        return books.stream()
                .map(BooksResponseTranslator::mapToEntertainment)
                .toList();
    }
    private static Entertainment mapToEntertainment(Book book) {
        return Entertainment.builder()
                .title(book.title())
                .authors(book.authors())
                .entertainmentType(EntertainmentType.BOOK)
                .build();
    }
}

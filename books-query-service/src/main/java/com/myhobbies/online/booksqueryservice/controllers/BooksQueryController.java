package com.myhobbies.online.booksqueryservice.controllers;

import com.myhobbies.online.booksqueryservice.models.Book;
import com.myhobbies.online.booksqueryservice.services.BooksQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("books")
@Validated
public class BooksQueryController {

    private final BooksQueryService booksQueryService;

    @GetMapping(produces = "application/json")
    public List<Book> getBooks(@Valid @NotNull @NotEmpty
                                @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Invalid characters in search text")
                                @RequestParam String searchText,
                                @Valid @Positive @RequestParam(value = "limit", required = false) Integer limit) {

        return booksQueryService.getBooks(searchText, limit);
    }
}

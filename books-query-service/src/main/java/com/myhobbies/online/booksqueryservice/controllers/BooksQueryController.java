package com.myhobbies.online.booksqueryservice.controllers;

import com.myhobbies.online.booksqueryservice.exception.ErrorResponse;
import com.myhobbies.online.booksqueryservice.models.Book;
import com.myhobbies.online.booksqueryservice.services.BooksQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(description = "Get Books based on search text and optional limit")
    @Parameter(description = "Search text to filter books", required = true, example = "Data Science", name = "searchText")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of Books retrieved successfully", content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Book.class))) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)) })
    })
    public List<Book> getBooks(@Valid @NotNull @NotEmpty
                                @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Invalid characters in search text")
                                @RequestParam String searchText,
                                @Valid @Positive @RequestParam(value = "limit", required = false) Integer limit) {

        return booksQueryService.getBooks(searchText, limit);
    }
}

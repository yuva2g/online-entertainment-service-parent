package com.myhobbies.online.entertainmentservice.controllers;

import com.myhobbies.online.entertainmentservice.exception.ErrorResponse;
import com.myhobbies.online.entertainmentservice.models.Entertainment;
import com.myhobbies.online.entertainmentservice.services.EntertainmentService;
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
@RequestMapping("online-entertainments")
@Validated
public class EntertainmentController {

    private final EntertainmentService entertainmentService;

    @GetMapping(produces = "application/json")
    @Operation(description = "Get entertainment options based on search text")
    @Parameter(description = "Search text to filter entertainments", required = true, example = "The Beatles", name = "searchText")
    @Parameter(description = "Number of results per type", example = "2", name = "resultsPerType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of entertainments successfully", content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Entertainment.class))) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)) })
    })
    public List<Entertainment> getEntertainmentOptions(@Valid @NotNull @NotEmpty
                                                       @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Invalid characters in search text")
                                                       @RequestParam String searchText,
                                                       @Valid @Positive @RequestParam(value = "resultsPerType", required = false) Integer resultsPerType) {
        return entertainmentService.getEntertainmentOptions(searchText, resultsPerType);
    }
}

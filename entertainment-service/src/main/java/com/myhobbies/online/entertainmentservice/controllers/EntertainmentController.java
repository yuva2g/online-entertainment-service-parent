package com.myhobbies.online.entertainmentservice.controllers;

import com.myhobbies.online.entertainmentservice.models.Entertainment;
import com.myhobbies.online.entertainmentservice.services.EntertainmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    public List<Entertainment> getEntertainmentOptions(@Valid @NotNull @NotEmpty
                                                       @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Invalid characters in search text")
                                                       @RequestParam String searchText) {
        return entertainmentService.getEntertainmentOptions(searchText);
    }
}

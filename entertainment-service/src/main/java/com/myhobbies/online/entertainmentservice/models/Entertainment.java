package com.myhobbies.online.entertainmentservice.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Entertainment {

    private final String title;
    private final List<String> authors;
    private final EntertainmentType entertainmentType;
}

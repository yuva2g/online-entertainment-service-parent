package com.myhobbies.online.booksqueryservice.models;

import java.util.List;

public record Book(String title, List<String> authors) {
}

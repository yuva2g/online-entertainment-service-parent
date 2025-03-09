package com.myhobbies.online.booksqueryservice.controllers;

import com.myhobbies.online.booksqueryservice.models.Book;
import com.myhobbies.online.booksqueryservice.services.BooksQueryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BooksQueryController.class)
class BooksQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BooksQueryService booksQueryService;

    @Test
    void getBooks_ValidRequest_ReturnsBooks() throws Exception {
        Book book = new Book("Test Title", List.of("Test Author"));
        List<Book> books = List.of(book);

        Mockito.when(booksQueryService.getBooks(anyString(), anyInt())).thenReturn(books);

        mockMvc.perform(get("/books")
                        .param("searchText", "Test")
                        .param("limit", "5"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'title':'Test Title','authors':['Test Author']}]"));
    }

    @Test
    void getBooks_ValidRequest_ReturnsEmptyBooks() throws Exception {
        List<Book> books = List.of();

        Mockito.when(booksQueryService.getBooks(anyString(), anyInt())).thenReturn(books);

        mockMvc.perform(get("/books")
                        .param("searchText", "Test")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBooks_InvalidRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/books")
                        .param("searchText", "Test")
                        .param("limit", "-5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBooks_InvalidRequest_ReturnsBadRequestWithMessage() throws Exception {
        mockMvc.perform(get("/books")
                        .param("searchText", "Test")
                        .param("limit", "-5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBooks_InvalidRequest_ReturnsBadRequestWithMessageContainingInvalidCharacters() throws Exception {
        mockMvc.perform(get("/books")
                        .param("searchText", "Test!")
                        .param("limit", "5"))
                .andExpect(status().isBadRequest());
    }
}
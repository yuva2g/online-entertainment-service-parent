package com.myhobbies.online.entertainmentservice.controllers;


import com.myhobbies.online.entertainmentservice.models.Entertainment;
import com.myhobbies.online.entertainmentservice.models.EntertainmentType;
import com.myhobbies.online.entertainmentservice.services.EntertainmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EntertainmentController.class)
class EntertainmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EntertainmentService entertainmentService;

    @Test
    public void getEntertainmentOptions_ValidRequest_ReturnsEntertainmentOptions() throws Exception {
        List<Entertainment> entertainmentOptions = List.of(
                Entertainment.builder()
                .title("Test Title")
                .authors(List.of("Test Author"))
                .entertainmentType(EntertainmentType.ALBUM).build(),
                Entertainment.builder()
                        .title("Test Title")
                        .authors(List.of("Test Author"))
                        .entertainmentType(EntertainmentType.BOOK)
                        .build());

        Mockito.when(entertainmentService.getEntertainmentOptions(anyString(), anyInt())).thenReturn(entertainmentOptions);

        mockMvc.perform(get("/online-entertainments")
                        .param("searchText", "Test")
                        .param("resultsPerType", "2"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'title':'Test Title','authors':['Test Author'],'entertainmentType':'ALBUM'},{'title':'Test Title','authors':['Test Author'],'entertainmentType':'BOOK'}]"));
    }

    @Test
    public void getEntertainmentOptions_ValidRequest_ReturnsEmptyEntertainmentOptions() throws Exception {
        List<Entertainment> entertainmentOptions = List.of();

        Mockito.when(entertainmentService.getEntertainmentOptions(anyString(), anyInt())).thenReturn(entertainmentOptions);

        mockMvc.perform(get("/online-entertainments")
                        .param("searchText", "Test")
                        .param("resultsPerType", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void getEntertainmentOptions_InvalidSearchText_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/online-entertainments")
                        .param("searchText", "Test!"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getEntertainmentOptions_InvalidResultsPerType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/online-entertainments")
                        .param("searchText", "Test")
                        .param("resultsPerType", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getEntertainmentOptions_NoSearchText_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/online-entertainments"))
                .andExpect(status().isBadRequest());
    }
}
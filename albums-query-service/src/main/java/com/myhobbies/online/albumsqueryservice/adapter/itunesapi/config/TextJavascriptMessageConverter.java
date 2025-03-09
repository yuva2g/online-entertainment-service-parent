package com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

public class TextJavascriptMessageConverter extends AbstractHttpMessageConverter<Object> {

    private final MappingJackson2HttpMessageConverter jsonConverter;

    public TextJavascriptMessageConverter() {
        super(new MediaType("text", "javascript"));
        this.jsonConverter = new MappingJackson2HttpMessageConverter();
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        return jsonConverter.read(clazz, inputMessage);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException {
        jsonConverter.write(o, MediaType.APPLICATION_JSON, outputMessage);
    }
}

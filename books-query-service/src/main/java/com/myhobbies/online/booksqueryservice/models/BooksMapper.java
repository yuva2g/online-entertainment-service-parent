package com.myhobbies.online.booksqueryservice.models;

import com.myhobbies.online.booksqueryservice.client.models.Volume;
import com.myhobbies.online.booksqueryservice.client.models.VolumeInfo;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class BooksMapper {

    private static Book mapToBook(VolumeInfo volumeInfo) {
        return new Book(volumeInfo.title(), volumeInfo.authors());
    }

    public static List<Book> mapToBooks(List<Volume> volumes) {
        return volumes.stream()
                .map(volume -> mapToBook(volume.volumeInfo()))
                .toList();
    }
}

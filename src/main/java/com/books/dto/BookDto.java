package com.books.dto;

import lombok.Data;

@Data
public class BookDto
{
    private long id;

    private String title;

    private Integer numberOfPages;

    private Double cost;

    private long[] authors;
}

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

    public BookDto() {}

    public BookDto(long id, String title, Integer numberOfPages, Double cost, long[] authors)
    {
        this.id = id;
        this.title = title;
        this.numberOfPages = numberOfPages;
        this.cost = cost;
        this.authors = authors;
    }

    public BookDto(long id, String title, Integer numberOfPages, Double cost)
    {
        this.id = id;
        this.title = title;
        this.numberOfPages = numberOfPages;
        this.cost = cost;
    }
}

package com.books.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    private Integer numberOfPages;

    private Double cost;

    private String author;

    public Book()
    {
    }

    public Book(String title, Integer numberOfPages, Double cost, String author)
    {
        this.title = title;
        this.numberOfPages = numberOfPages;
        this.cost = cost;
        this.author = author;
    }
}

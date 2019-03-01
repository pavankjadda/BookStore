package com.books.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book")
@Data
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "author")
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

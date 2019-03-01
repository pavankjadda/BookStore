package com.books.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "author_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id",referencedColumnName = "id"))
    private List<Author> authors;


    public Book()
    {
    }
}

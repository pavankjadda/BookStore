package com.books.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "author")
@Data
public class Author
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;


    @OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "author_address",
            joinColumns = @JoinColumn(name = "author_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "address_id",referencedColumnName = "id"))
    private List<Address> addresses;


    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private List<Book> books;

}

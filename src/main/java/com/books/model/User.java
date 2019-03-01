package com.books.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User
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
    @JoinTable(name = "employee_address",
            joinColumns = @JoinColumn(name = "employee_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "address_id",referencedColumnName = "id"))
    private List<Address> addresses;
}

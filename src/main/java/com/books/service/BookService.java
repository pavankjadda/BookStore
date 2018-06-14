package com.books.service;

import com.books.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService
{
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book saveAndFlush(Book book);

    void deleteBook(Book book);
}

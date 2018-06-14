package com.books.web;

import com.books.model.Book;
import com.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController
{
    private BookService bookService;

    @Autowired
    public BookController(BookService   bookService)
    {
        this.bookService=bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks()
    {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Optional<Book>> getBookById(@PathVariable Long id)
    {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping("/books/save_book")
    public ResponseEntity<Book> updateBook(@RequestBody Book book)
    {
        return ResponseEntity.ok(bookService.saveAndFlush(book));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Book book)
    {
        return ResponseEntity.ok("Book Deleted");
    }

}

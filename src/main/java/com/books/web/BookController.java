package com.books.web;

import com.books.model.Book;
import com.books.service.BookService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController
{
    private BookService bookService;

    @Autowired
    public BookController(BookService   bookService)
    {
        this.bookService=bookService;
    }

    @GetMapping("/book/list")
    public ResponseEntity<List<Book>> getBooks()
    {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Optional<Book>> getBookById(@PathVariable Long id)
    {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping("/book/save_book2")
    public ResponseEntity<Book> updateBook(@RequestBody Book book)
    {
        return ResponseEntity.ok(bookService.saveAndFlush(book));
    }

    @PostMapping("/book/save_book")
    public ResponseEntity<Book> createBook(@RequestBody Object bookDataObject) throws IOException
    {
        System.out.println("bookDataObject: "+bookDataObject.toString());
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser((String) bookDataObject);
        JsonNode jsonNode = mapper.readTree(parser);

        //return ResponseEntity.ok(bookService.saveAndFlush(book));
        return null;
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id)
    {
        Optional<Book> book=bookService.findById(id);
        if(book.isPresent())
        {
            bookService.deleteBook(book.get());
            return ResponseEntity.ok("Book Deleted");
        }
        return ResponseEntity.ok("Book Not found");
    }
}

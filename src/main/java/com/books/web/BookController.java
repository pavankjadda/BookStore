package com.books.web;

import com.books.dto.BookDto;
import com.books.model.Author;
import com.books.model.Book;
import com.books.repo.AuthorRepository;
import com.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController
{
    private BookService bookService;

    private final AuthorRepository authorRepository;

    @Autowired
    public BookController(BookService bookService, AuthorRepository authorRepository)
    {
        this.bookService=bookService;
        this.authorRepository = authorRepository;
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

    @PostMapping("/book/save_book")
    public ResponseEntity<Book> createBook(@RequestBody BookDto bookDto)
    {
        Book book=new Book();
        book.setId(bookDto.getId());
        book.setCost(bookDto.getCost());
        book.setNumberOfPages(bookDto.getNumberOfPages());
        book.setTitle(bookDto.getTitle());
        book.setAuthors(getAuthorObjects(bookDto.getAuthors()));

        return ResponseEntity.ok(bookService.saveAndFlush(book));
    }

    private List<Author> getAuthorObjects(long[] authors)
    {
        List<Author> authorList=new ArrayList<>();
        for (int i=authors.length;i>0;i--)
        {
            Optional<Author> authorOptional=authorRepository.findById(authors[i-1]);
            authorOptional.ifPresent(authorList::add);
        }
        return authorList;
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

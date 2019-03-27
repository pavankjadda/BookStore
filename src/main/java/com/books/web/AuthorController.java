package com.books.web;

import com.books.model.Author;
import com.books.repo.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthorController
{
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository)
    {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/author/list")
    public List<Author> getAuthorsList()
    {
        return authorRepository.findAll();
    }

    @GetMapping("/author/{id}")
    public Author getAuthor(@PathVariable Long id)
    {
        return authorRepository.findById(id).orElse(null);
    }

    @PostMapping("/author/create")
    public Author createAuthor(@RequestBody Author author)
    {
        return authorRepository.saveAndFlush(author);
    }
}

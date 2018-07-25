package com.books.repo;

import com.books.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>
{
    List<Book> findAll();

    Book findBookById(long id);

    Optional<Book> findById(Long id);

    <S extends Book> List<S> saveAll(Iterable<S> var1);

    <S extends Book> S saveAndFlush(S var1);


    void flush();

}

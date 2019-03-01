package com.books.repo;

import com.books.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutherRepository extends JpaRepository<Author,Long>
{
}

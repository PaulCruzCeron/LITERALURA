package com.paulcruz.literalura.repository;

import com.paulcruz.literalura.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = "authors")
    @Query("SELECT b FROM Book b")
    List<Book> findAllWithAuthors();

    Book findByTitleIgnoreCase(String title);

    List<Book> findByLanguagesContains(String language);
}
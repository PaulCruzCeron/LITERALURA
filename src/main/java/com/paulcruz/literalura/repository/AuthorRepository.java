package com.paulcruz.literalura.repository;

import com.paulcruz.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFullName(String name);
    @Query("SELECT a FROM Author a WHERE :year >= a.birthDate AND (:year <= a.dateOfDeath OR a.dateOfDeath IS NULL)")
    List<Author> authorForYear(int year);
}

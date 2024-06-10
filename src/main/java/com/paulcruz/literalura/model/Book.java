package com.paulcruz.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;
    private String languages;
    private Double numberOfDownloads;


    public Book() {
    }

    public Book(DataBook dataBook, List<Author> authors) {
        this.title = dataBook.title();
        this.authors = authors;
        this.languages = getFirstLanguage(dataBook);
        this.numberOfDownloads = dataBook.numberOfDownloads();

    }
//    public String getFirstLanguage() {
//        return languages != null && !languages.isEmpty() ? languages.get(0) : null;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public Double getNumberOfDownloads() {
        return numberOfDownloads;
    }

    public void setNumberOfDownloads(Double numberOfDownloads) {
        this.numberOfDownloads = numberOfDownloads;
    }
    public String getFirstLanguage(DataBook dataBook){
        String idioma = dataBook.languages().toString();
        return idioma;
    }



    @Override
    public String toString() {
        String authorsNames = authors.stream()
                .map(Author::getFullName)
                .collect(Collectors.joining(", "));
        return "---- Libro ---- \n" +
                "Titulo= " + title + "\n" +
                "Autor/es= " + authorsNames + "\n" +
                "Lenguaje= " + languages + "\n" +
                "Numero de descargas= " + numberOfDownloads + "\n" +
                "----------------------- \n";
    }
}
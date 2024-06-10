package com.paulcruz.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private int birthDate;
    private int dateOfDeath;

    @ManyToMany(mappedBy = "authors",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<com.paulcruz.literalura.model.Book> books;


    public Author() {
    }
    public Author(DataAuthor dataAuthor) {
        this.fullName = dataAuthor.nameSurname();
        this.birthDate = Integer.valueOf(dataAuthor.birthDate()) ;
        this.dateOfDeath = Integer.valueOf(dataAuthor.dateOfDeath()) ;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    public int getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(int dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        String bookList = books.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(", "));
        return "----- Autor -----\n" +
                "Nombre: " + fullName +"\n"+
                "Año nacimiento: " + birthDate +"\n"+
                "Año fallecimiento: " + dateOfDeath +"\n"+
                "libros: " + bookList;

    }
}
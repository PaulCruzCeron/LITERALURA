package com.paulcruz.literalura.main;

import com.paulcruz.literalura.model.AllData;
import com.paulcruz.literalura.model.Author;
import com.paulcruz.literalura.model.Book;
import com.paulcruz.literalura.model.DataBook;
import com.paulcruz.literalura.repository.AuthorRepository;
import com.paulcruz.literalura.repository.BookRepository;
import com.paulcruz.literalura.service.APIComsuption;
import com.paulcruz.literalura.service.ConvertData;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private APIComsuption apiConsumption = new APIComsuption();
    private ConvertData converter = new ConvertData();
    private final String URL_BASE = "https://gutendex.com/books/";
    private Scanner scanner = new Scanner(System.in);
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public Principal(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        int option = -1;
        while (option != 0) {
            System.out.println("""
                    1 - Buscar libro
                    2 - Mostrar libros buscados
                    3 - Mostrar autores buscados
                    4 - Mostrar autores vivos
                    5 - Mostrar libros por idioma
                    6 - Buscar autor por nombre
                    7 - Top 10 libros más descargados
                    0 - Salir
                    """);

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> searchBookForTitle();
                case 2 -> showBooksSearched();
                case 3 -> showAuthors();
                case 4 -> findAuthorsForYear();
                case 5 -> showBooksForLanguage();
                case 6 -> searchAuthorByName();
                case 7 -> top10MostDownloadedBooks();
                case 0 -> {
                    System.out.println("Cerrando aplicación");
                    System.exit(0);
                }
                default -> System.out.println("Opción invalida");
            }
        }
    }

    private DataBook getDataBook() {
        System.out.println("Ingresa el título del libro a buscar: ");
        String bookTitle = scanner.nextLine();
        var json = apiConsumption.getData(URL_BASE + "?search=" + bookTitle.replace(" ", "+"));
        AllData allData = converter.getDates(json, AllData.class);
        if (allData.books().isEmpty()) {
            System.out.println("No se encontraron libros con este título.");
            return null;
        }
        return allData.books().get(0);
    }

    public void searchBookForTitle() {
        DataBook dataBook = getDataBook();
        if (dataBook == null) {
            return;
        }

        Book presentBook = bookRepository.findByTitleIgnoreCase(dataBook.title());
        if (presentBook != null) {
            System.out.println("No se puede registrar el libro, ya existe.");
            return;
        }

        List<Author> authorList = dataBook.authors().stream()
                .map(dataAuthor -> authorRepository.findByFullName(dataAuthor.nameSurname())
                        .orElseGet(() -> authorRepository.save(new Author(dataAuthor))))
                .collect(Collectors.toList());

        Book newBook = new Book(dataBook, authorList);
        newBook.setAuthors(authorList);
        bookRepository.save(newBook);
        System.out.println(newBook);
    }

    private void showBooksSearched() {
        bookRepository.findAllWithAuthors().stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void showAuthors() {
        authorRepository.findAll().stream()
                .sorted(Comparator.comparing(Author::getFullName))
                .forEach(System.out::println);
    }

    private void findAuthorsForYear() {
        System.out.println("Ingresa un año para saber que autores están vivos actualmente");
        int yearSearch = scanner.nextInt();
        scanner.nextLine();  // Clear the buffer
        List<Author> authors = authorRepository.authorForYear(yearSearch);
        if (authors.isEmpty()) {
            System.out.println("No se encontraron autores vivos para ese año");
        } else {
            System.out.println("Autores vivos\n");
            authors.forEach(System.out::println);
        }
    }

    public void showBooksForLanguage() {
        System.out.println("Ingresa el idioma en el que deseas buscar libros:\n" +
                "es - Español\n" +
                "en - Inglés\n" +
                "fr - Francés\n" +
                "pt - Portugués");
        String language = scanner.nextLine().trim().toLowerCase();

        List<String> validLanguages = Arrays.asList("es", "en", "fr", "pt");
        if (!validLanguages.contains(language)) {
            System.out.println("Opción de idioma inválida. Por favor, elije una de las opciones proporcionadas.");
            return;
        }

        List<Book> books = bookRepository.findByLanguagesContains(language);
        if (books.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma '" + language + "'.");
        } else {
            System.out.println("Libros en el idioma '" + language + "':");
            books.forEach(System.out::println);
        }
    }

    public void searchAuthorByName() {
        System.out.println("Ingresa el nombre del autor a buscar: ");
        String name = scanner.nextLine().toLowerCase();
        List<Author> authors = authorRepository.findAll();
        Optional<Author> authorOptional = authors.stream()
                .filter(a -> a.getFullName().toLowerCase().contains(name))
                .findFirst();
        if (authorOptional.isPresent()) {
            System.out.println("Autor encontrado");
            System.out.println(authorOptional.get());
        } else {
            System.out.println("No se encontró ningún autor con ese nombre");
        }
    }

    public void top10MostDownloadedBooks() {
        bookRepository.findAll().stream()
                .sorted(Comparator.comparing(Book::getNumberOfDownloads).reversed())
                .limit(10)
                .forEach(System.out::println);
    }
}

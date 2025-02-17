package Assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Library class represents a collection of books and authors in a library system.
 * It provides methods to add, remove, and modify books and authors, as well as retrieve them by different criteria.
 */
public class Library {
    private List<Book> books;
    private List<Author> authors;

    /**
     * Constructs a Library object and loads the books and authors from the database.
     */
    public Library() {
        books = BookDatabaseManager.GetAllBooksAndAuthors();

        authors = new ArrayList<>();

        for (Book book : books) {
            for (Author author : book.getAuthors()) {
                if (!authors.contains(author)) {
                    authors.add(author);
                }
            }
        }
    }

    /**
     * Retrieves all books in the library.
     *
     * @return A list of all books in the library.
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * Retrieves a book by its title.
     *
     * @param title The title of the book to retrieve.
     * @return The book if found, otherwise null.
     */
    public Book getBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Retrieves all authors in the library.
     *
     * @return A list of all authors in the library.
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * Adds a new book to the library if it doesn't already exist.
     *
     * @param book The book to be added.
     */
    public void addBook(Book book) {
        for (Author author : book.getAuthors()) {
            // Ensure that we add only existing authors
            addAuthor(author);
        }
        if (!books.contains(book)) {
            BookDatabaseManager.AddBook(book);
            books.add(book);
        }
    }


    /**
     * Adds a new author to the library if they don't already exist.
     *
     * @param author The author to be added.
     */
    public void addAuthor(Author author) {
        // Ensure that we only add unique authors
        Author existingAuthor = null;
        for (Author a : authors) {
            if (a.getFirstName().equalsIgnoreCase(author.getFirstName()) &&
                    a.getLastName().equalsIgnoreCase(author.getLastName())) {
                existingAuthor = a;
                break;
            }
        }

        if (existingAuthor == null) {
            // If author does not exist, add a new one
            BookDatabaseManager.AddAuthor(author);
            authors.add(author);
        }
    }


    /**
     * Prints all books by a specific author.
     *
     * @param authorFullName The full name of the author (first and last name).
     */
    public void printBooksByAuthor(String authorFullName) {
        String[] nameParts = authorFullName.split(" ");
        if (nameParts.length == 2) {
            String firstName = nameParts[0];
            String lastName = nameParts[1];
            Author foundAuthor = null;
            for (Author author : authors) {
                if (author.getFirstName().equalsIgnoreCase(firstName) &&
                        author.getLastName().equalsIgnoreCase(lastName)) {
                    foundAuthor = author;
                    break;
                }
            }
            if (foundAuthor != null) {
                System.out.println("Books by " + firstName + " " + lastName + ":");
                for (Book book : foundAuthor.getTitles()) {
                    System.out.println(" - " + book.getTitle() + " (ISBN: " + book.getISBN() + ")");
                }
            } else {
                System.out.println("Author not found.");
            }
        } else {
            System.out.println("Please provide both first and last name of the author.");
        }
    }

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return The book if found, otherwise null.
     */
    public Book getBookByISBN(String isbn) {
        for (Book book : books) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Adds a new book and/or author to the library.
     * If the book or author already exists, updates the relation between them.
     *
     * @param newBookTitle       The title of the new book.
     * @param newISBN           The ISBN of the new book.
     * @param newEdition        The edition number of the new book.
     * @param newCopyrightYear  The copyright year of the new book.
     * @param newAuthorFirstName The first name of the new author.
     * @param newAuthorLastName The last name of the new author.
     */
    public void addNewBookAndAuthor(String newBookTitle, String newISBN, int newEdition, int newCopyrightYear,
                                    String newAuthorFirstName, String newAuthorLastName) {
        Author existingAuthor = null;
        for (Author a : authors) {
            if (a.getFirstName().equalsIgnoreCase(newAuthorFirstName) && a.getLastName().equalsIgnoreCase(newAuthorLastName)) {
                existingAuthor = a;
                break;
            }
        }

        Book existingBook = getBookByISBN(newISBN);

        if (existingBook != null && existingAuthor != null) {
            if (!existingBook.getAuthors().contains(existingAuthor)) {
                existingBook.addAuthor(existingAuthor);
                existingAuthor.addTitle(existingBook);
                BookDatabaseManager.AddRelation(existingBook, existingAuthor);
                System.out.println("Existing book updated with new author.");
            } else {
                System.out.println("This book and author combination already exists.");
            }
        } else if (existingBook == null && existingAuthor != null) {
            Book newBook = new Book(newISBN, newBookTitle, newEdition, newCopyrightYear);
            newBook.addAuthor(existingAuthor);
            existingAuthor.addTitle(newBook);
            addBook(newBook);
            BookDatabaseManager.AddRelation(newBook, existingAuthor);
            System.out.println("New book added for existing author.");
        } else if (existingBook != null && existingAuthor == null) {
            Author newAuthor = new Author(newAuthorFirstName, newAuthorLastName);
            existingBook.addAuthor(newAuthor);
            newAuthor.addTitle(existingBook);
            addAuthor(newAuthor);
            BookDatabaseManager.AddRelation(existingBook, newAuthor);
            System.out.println("Existing book updated with new author.");
        } else {
            Book newBook = new Book(newISBN, newBookTitle, newEdition, newCopyrightYear);
            Author newAuthor = new Author(newAuthorFirstName, newAuthorLastName);
            newBook.addAuthor(newAuthor);
            newAuthor.addTitle(newBook);
            addBook(newBook);
            addAuthor(newAuthor);
            BookDatabaseManager.AddRelation(newBook, newAuthor);
            System.out.println("New book and author added successfully.");
        }
    }

    /**
     * Removes a book from the library by its title.
     *
     * @param removeTitle The title of the book to remove.
     */
    public void removeBookByTitle(String removeTitle) {
        Book bookToRemove = getBook(removeTitle);
        if (bookToRemove != null) {
            System.out.println("Please enter the author's name for the book:");
            Scanner input = new Scanner(System.in);
            String removeAuthorSearch = input.nextLine();

            String[] removeAuthorNameParts = removeAuthorSearch.split(" ");
            if (removeAuthorNameParts.length == 2) {
                String removeFirstName = removeAuthorNameParts[0];
                String removeLastName = removeAuthorNameParts[1];

                Author removeAuthor = null;
                for (Author a : authors) {
                    if (a.getFirstName().equalsIgnoreCase(removeFirstName) &&
                            a.getLastName().equalsIgnoreCase(removeLastName)) {
                        removeAuthor = a;
                        break;
                    }
                }

                if (removeAuthor != null) {
                    if (removeAuthor.getTitles().contains(bookToRemove)) {
                        removeAuthor.getTitles().remove(bookToRemove);
                        BookDatabaseManager.DeleteBook(bookToRemove);
                        books.remove(bookToRemove);
                        System.out.println(removeTitle + " successfully removed from the library!");
                    } else {
                        System.out.println("The book is not associated with the author " + removeFirstName + " " + removeLastName);
                    }
                } else {
                    System.out.println("Author not found.");
                }
            } else {
                System.out.println("Please enter both first and last name of the author.");
            }
        } else {
            System.out.println("This book does not exist in the library.");
        }
    }

    /**
     * Edits the details of a book (title, edition, copyright year) based on ISBN.
     *
     * @param isbn The ISBN of the book to edit.
     * @param input A Scanner object to read user input for new details.
     */
    public void editBookDetails(String isbn, Scanner input) {
        Book bookToEdit = getBookByISBN(isbn);
        if (bookToEdit != null) {
            System.out.println("Enter new title (leave blank to keep the current title):");
            String newTitle = input.nextLine().trim();
            System.out.println("Enter new edition number (leave blank to keep the current edition):");
            String updatedEdition = input.nextLine().trim();
            System.out.println("Enter new copyright year (leave blank to keep the current year):");
            String newCopyright = input.nextLine().trim();

            boolean updated = false;
            if (!newTitle.isEmpty() && !newTitle.equals(bookToEdit.getTitle())) {
                bookToEdit.setTitle(newTitle);
                updated = true;
            }
            if (!updatedEdition.isEmpty()) {
                try {
                    int edition = Integer.parseInt(updatedEdition);
                    if (edition != bookToEdit.getEdition()) {
                        bookToEdit.setEdition(edition);
                        updated = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid edition number format.");
                }
            }
            if (!newCopyright.isEmpty()) {
                try {
                    int copyright = Integer.parseInt(newCopyright);
                    if (copyright != bookToEdit.getCopyright()) {
                        bookToEdit.setCopyright(copyright);
                        updated = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid copyright year format.");
                }
            }

            if (updated) {
                BookDatabaseManager.UpdateBook(bookToEdit);
                System.out.println("Book details updated!");
            } else {
                System.out.println("No changes made to the book.");
            }
        } else {
            System.out.println("Book with the specified ISBN not found.");
        }
    }

    /**
     * Edits the details of an author (first and last name).
     *
     * @param authorFullName The full name of the author to edit.
     * @param input A Scanner object to read user input for new details.
     */
    public void editAuthorDetails(String authorFullName, Scanner input) {
        authorFullName = authorFullName.trim();
        String[] nameParts = authorFullName.split(" ");
        if (nameParts.length == 2) {
            String firstName = nameParts[0].trim();
            String lastName = nameParts[1].trim();

            Author authorToEdit = null;
            for (Author author : authors) {
                if (author.getFirstName().equalsIgnoreCase(firstName) &&
                        author.getLastName().equalsIgnoreCase(lastName)) {
                    authorToEdit = author;
                    break;
                }
            }

            if (authorToEdit != null) {
                System.out.println("Enter new first name (leave blank to keep the current first name):");
                String newFirstName = input.nextLine().trim();
                System.out.println("Enter new last name (leave blank to keep the current last name):");
                String newLastName = input.nextLine().trim();

                boolean updated = false;
                if (!newFirstName.isEmpty() && !newFirstName.equalsIgnoreCase(authorToEdit.getFirstName())) {
                    authorToEdit.setFirstName(newFirstName);
                    updated = true;
                }
                if (!newLastName.isEmpty() && !newLastName.equalsIgnoreCase(authorToEdit.getLastName())) {
                    authorToEdit.setLastName(newLastName);
                    updated = true;
                }

                if (updated) {
                    for (Book book : books) {
                        if (book.getAuthors().contains(authorToEdit)) {
                            for (Author author : book.getAuthors()) {
                                if (author.equals(authorToEdit)) {
                                    author.setFirstName(newFirstName);
                                    author.setLastName(newLastName);
                                }
                            }
                        }
                    }

                    boolean updateSuccess = BookDatabaseManager.UpdateAuthor(authorToEdit, firstName, lastName);
                    if (updateSuccess) {
                        refreshLists();
                        System.out.println("Author details updated!");
                    } else {
                        System.out.println("Failed to update author details in the database.");
                    }
                } else {
                    System.out.println("No changes made.");
                }
            } else {
                System.out.println("Author not found. Please check the spelling and try again.");
            }
        } else {
            System.out.println("Please provide both first and last name.");
        }
    }


    /**
     * Refreshes the internal lists of books and authors from the database.
     */
    private void refreshLists() {
        books = BookDatabaseManager.GetAllBooksAndAuthors();

        authors = new ArrayList<>();

        for (Book book : books) {
            for (Author author : book.getAuthors()) {
                if (!authors.contains(author)) {
                    authors.add(author);
                }
            }
        }
    }

    /**
     * Prints all books in the library with their authors.
     */
    public void printAllBooks() {
        for (Book book : books) {
            System.out.println("\n" + book.getTitle() + " by:");
            List<Author> authors = book.getAuthors();
            if (authors.isEmpty()) {
                System.out.println("No authors listed.");
            } else {
                for (Author author : authors) {
                    System.out.println(" - " + author.getFirstName() + " " + author.getLastName());
                }
            }
        }
    }

    /**
     * Prints all authors in the library with their titles.
     */
    public void printAllAuthors() {
        for (Author author : authors) {
            System.out.println("\n" + author.getFirstName() + " " + author.getLastName() + ":");
            List<Book> books = author.getTitles();
            if (books.isEmpty()) {
                System.out.println("No books listed.");
            } else {
                for (Book book : books) {
                    System.out.println(" - " + book.getTitle());
                }
            }
        }
    }
}

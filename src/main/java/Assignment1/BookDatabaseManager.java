package Assignment1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods for managing books and authors in a database, including operations
 * like adding, deleting, updating, and retrieving books and authors, as well as creating relationships
 * between books and authors.
 */
public class BookDatabaseManager {
    public static final String DB_NAME = "/books";

    /**
     * Adds a new book to the database if it does not already exist.
     *
     * @param book The Book object to be added to the database.
     */
    public static void AddBook(Book book) {
        try {
            Connection conn = DriverManager.getConnection(
                    Assignment1DBProperties.DATABASE_URL + DB_NAME,
                    Assignment1DBProperties.DATABASE_USER,
                    Assignment1DBProperties.DATABASE_PASSWORD);

            Statement stmt = conn.createStatement();

            String checkBookExists = "SELECT COUNT(*) FROM titles WHERE isbn = '" + book.getISBN() + "'";
            ResultSet rs = stmt.executeQuery(checkBookExists);
            rs.next();
            int bookCount = rs.getInt(1);

            if (bookCount > 0) {
                System.out.println("Book already exists in the database.");
            } else {
                String insertBook = "INSERT INTO titles (isbn, title, editionNumber, copyright) " +
                        "VALUES ('" + book.getISBN() + "', '" + book.getTitle() + "', " +
                        book.getEdition() + ", " + book.getCopyright() + ")";
                stmt.executeUpdate(insertBook);
                System.out.println("Book added successfully.");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new author to the database.
     *
     * @param author The Author object to be added to the database.
     */
    public static void AddAuthor(Author author) {
        try {
            Connection conn = DriverManager.getConnection(
                    Assignment1DBProperties.DATABASE_URL + DB_NAME,
                    Assignment1DBProperties.DATABASE_USER,
                    Assignment1DBProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            final String INSERT_STATEMENT = "INSERT INTO authors (firstName, lastName) VALUES ('" + author.getFirstName() + "', '" + author.getLastName() + "')";
            stmt.executeUpdate(INSERT_STATEMENT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a book and its associated relations from the database.
     *
     * @param book The Book object to be deleted.
     */
    public static void DeleteBook(Book book) {
        try {
            Connection conn = DriverManager.getConnection(
                    Assignment1DBProperties.DATABASE_URL + DB_NAME,
                    Assignment1DBProperties.DATABASE_USER,
                    Assignment1DBProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();

            final String DELETE_RELATION_STATEMENT = "DELETE FROM authorisbn WHERE isbn = '" + book.getISBN() + "'";
            stmt.executeUpdate(DELETE_RELATION_STATEMENT);

            final String DELETE_BOOK_STATEMENT = "DELETE FROM titles WHERE isbn = '" + book.getISBN() + "'";
            stmt.executeUpdate(DELETE_BOOK_STATEMENT);

            stmt.close();
            conn.close();

            System.out.println("Book and its relations have been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all books from the database and returns them as a list.
     *
     * @return A list of all books in the database.
     */
    public static List<Book> GetAllBooksAndAuthors() {
        List<Book> bookList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                Assignment1DBProperties.DATABASE_URL + DB_NAME,
                Assignment1DBProperties.DATABASE_USER,
                Assignment1DBProperties.DATABASE_PASSWORD)) {

            String query = "SELECT b.isbn, b.title, b.editionNumber, b.copyright, " +
                    "a.authorID, a.firstName, a.lastName " +
                    "FROM titles b " +
                    "LEFT JOIN authorisbn ai ON b.isbn = ai.isbn " +
                    "LEFT JOIN authors a ON ai.authorID = a.authorID";

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String isbn = rs.getString("isbn");
                    String bookTitle = rs.getString("title");
                    int edition = rs.getInt("editionNumber");
                    int copyright = rs.getInt("copyright");
                    String authorFirstName = rs.getString("firstName");
                    String authorLastName = rs.getString("lastName");

                    Book book = findBookByIsbn(bookList, isbn);
                    if (book == null) {
                        book = new Book(isbn, bookTitle, edition, copyright);
                        bookList.add(book);
                    }

                    Author author = null;
                    for (Book b : bookList) {
                        for (Author existingAuthor : b.getAuthors()) {
                            if (existingAuthor.getFirstName().equals(authorFirstName) && existingAuthor.getLastName().equals(authorLastName)) {
                                author = existingAuthor;
                                break;
                            }
                        }
                        if (author != null) {
                            break;
                        }
                    }

                    if (author == null) {
                        author = new Author(authorFirstName, authorLastName);
                    }

                    book.addAuthor(author);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    private static Book findBookByIsbn(List<Book> bookList, String isbn) {
        for (Book book : bookList) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }
        return null;
    }


    /**
     * Creates a relationship between a book and an author in the database.
     *
     * @param book   The Book object.
     * @param author The Author object.
     */
    public static void AddRelation(Book book, Author author) {
        try {
            Connection conn = DriverManager.getConnection(
                    Assignment1DBProperties.DATABASE_URL + DB_NAME,
                    Assignment1DBProperties.DATABASE_USER,
                    Assignment1DBProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();

            String checkAuthorExists = "SELECT authorID FROM authors WHERE firstName = '" + author.getFirstName() +
                    "' AND lastName = '" + author.getLastName() + "'";
            ResultSet authorRs = stmt.executeQuery(checkAuthorExists);

            if (!authorRs.next()) {
                System.out.println("Author " + author.getFirstName() + " " + author.getLastName() + " does not exist. Cannot create relationship.");
                return;
            }
            int authorId = authorRs.getInt("authorID");

            String checkBookExists = "SELECT isbn FROM titles WHERE isbn = '" + book.getISBN() + "'";
            ResultSet bookRs = stmt.executeQuery(checkBookExists);

            if (!bookRs.next()) {
                System.out.println("Book with ISBN " + book.getISBN() + " does not exist. Cannot create relationship.");
                return;
            }

            String insertRelation = "INSERT INTO authorisbn (authorID, isbn) VALUES (" + authorId + ", '" + book.getISBN() + "')";
            stmt.executeUpdate(insertRelation);
            System.out.println("Relationship between book " + book.getTitle() + " and author " + author.getFirstName() + " " + author.getLastName() + " created successfully.");

            authorRs.close();
            bookRs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing book's details in the database.
     *
     * @param book The Book object containing updated details.
     */
    public static void UpdateBook(Book book) {
        try {
            Connection conn = DriverManager.getConnection(
                    Assignment1DBProperties.DATABASE_URL + DB_NAME,
                    Assignment1DBProperties.DATABASE_USER,
                    Assignment1DBProperties.DATABASE_PASSWORD);

            String checkBookExists = "SELECT COUNT(*) FROM titles WHERE isbn = '" + book.getISBN() + "'";
            Statement stmt1 = conn.createStatement();
            ResultSet rs = stmt1.executeQuery(checkBookExists);
            rs.next();
            int bookCount = rs.getInt(1);

            if (bookCount > 0) {
                String updateBook = "UPDATE titles SET title = '" + book.getTitle() + "', editionNumber = " + book.getEdition() +
                        ", copyright = " + book.getCopyright() + " WHERE isbn = '" + book.getISBN() + "'";
                Statement stmt2 = conn.createStatement();
                stmt2.executeUpdate(updateBook);
                System.out.println("Book with ISBN " + book.getISBN() + " successfully updated.");
            } else {
                System.out.println("Book with ISBN " + book.getISBN() + " does not exist. Cannot update.");
            }
            rs.close();
            stmt1.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an author's details in the database.
     *
     * @param author           The Author object containing updated details.
     * @param originalFirstName The original first name of the author.
     * @param originalLastName  The original last name of the author.
     * @return True if the author is successfully updated, false otherwise.
     */
    public static boolean UpdateAuthor(Author author, String originalFirstName, String originalLastName) {
        try {
            Connection conn = DriverManager.getConnection(
                    Assignment1DBProperties.DATABASE_URL + DB_NAME,
                    Assignment1DBProperties.DATABASE_USER,
                    Assignment1DBProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();

            String checkAuthorExists = "SELECT authorID FROM authors WHERE firstName = '" + originalFirstName +
                    "' AND lastName = '" + originalLastName + "'";
            ResultSet rs = stmt.executeQuery(checkAuthorExists);

            if (rs.next()) {
                int authorId = rs.getInt("authorID");
                String updateAuthor = "UPDATE authors SET firstName = '" + author.getFirstName() +
                        "', lastName = '" + author.getLastName() + "' WHERE authorID = " + authorId;
                int rowsUpdated = stmt.executeUpdate(updateAuthor);
                if (rowsUpdated > 0) {
                    System.out.println("Author " + author.getFirstName() + " " + author.getLastName() + " successfully updated.");
                    return true;
                } else {
                    System.out.println("No rows updated. Author may not exist.");
                    return false;
                }
            } else {
                System.out.println("Author " + originalFirstName + " " + originalLastName + " does not exist. Cannot update.");
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package Assignment1;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Book with an ISBN, title, edition, copyright year, and a list of authors.
 */
public class Book {

    private String ISBN;
    private String title;
    private int edition;
    private int copyright;
    private List<Author> authors;

    /**
     * Constructs a new Book instance with the given ISBN, title, edition, and copyright year.
     *
     * @param ISBN      The ISBN of the book.
     * @param title     The title of the book.
     * @param edition   The edition number of the book.
     * @param copyright The copyright year of the book.
     */
    public Book(String ISBN, String title, int edition, int copyright) {
        this.ISBN = ISBN;
        this.title = title;
        this.edition = edition;
        this.copyright = copyright;
        this.authors = new ArrayList<>();
    }

    /**
     * Returns the ISBN of the book.
     *
     * @return The ISBN of the book.
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param ISBN The ISBN to set.
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Returns the title of the book.
     *
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the edition number of the book.
     *
     * @return The edition number of the book.
     */
    public int getEdition() {
        return edition;
    }

    /**
     * Sets the edition number of the book.
     *
     * @param edition The edition number to set.
     */
    public void setEdition(int edition) {
        this.edition = edition;
    }

    /**
     * Returns the copyright year of the book.
     *
     * @return The copyright year of the book.
     */
    public int getCopyright() {
        return copyright;
    }

    /**
     * Sets the copyright year of the book.
     *
     * @param copyright The copyright year to set.
     */
    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    /**
     * Adds an author to the list of authors for this book.
     * If the author is not already in the list, they are added and this book is associated with the author.
     *
     * @param author The author to add to the book.
     */
    public void addAuthor(Author author) {
        if (!authors.contains(author)) {
            authors.add(author);
            author.addTitle(this);
        }
    }

    /**
     * Returns a list of authors for this book.
     *
     * @return The list of authors for this book.
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * Compares this book with another object for equality. Two books are considered equal if they have the same ISBN.
     *
     * @param o The object to compare this book with.
     * @return True if the books are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return ISBN != null && ISBN.equals(book.ISBN);
    }

    /**
     * Returns a hash code value for this book based on its ISBN.
     *
     * @return The hash code value for this book.
     */
    @Override
    public int hashCode() {
        return ISBN != null ? ISBN.hashCode() : 0;
    }
}

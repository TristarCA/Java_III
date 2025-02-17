package Assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Author with a first name, last name, and a list of books they have written.
 */
public class Author {

    private String firstName;
    private String lastName;
    private List<Book> titles;

    /**
     * Constructs a new Author with the specified first name and last name.
     *
     * @param firstName The first name of the author.
     * @param lastName  The last name of the author.
     */
    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.titles = new ArrayList<>();
    }

    /**
     * Returns the first name of the author.
     *
     * @return The first name of the author.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the author.
     *
     * @param firstName The first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the author.
     *
     * @return The last name of the author.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the author.
     *
     * @param lastName The last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns a list of books written by the author.
     *
     * @return The list of books written by the author.
     */
    public List<Book> getTitles() {
        return titles;
    }

    /**
     * Adds a book to the list of titles written by the author.
     * If the book is not already in the list, it is added.
     *
     * @param book The book to add to the author's list of titles.
     */
    public void addTitle(Book book) {
        if (!titles.contains(book)) {
            titles.add(book);
        }
    }

    /**
     * Compares this author with another object for equality.
     * Two authors are considered equal if they have the same first name and last name (ignoring case and leading/trailing spaces).
     *
     * @param o The object to compare this author with.
     * @return True if the authors are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(firstName.toLowerCase().trim(), author.firstName.toLowerCase().trim()) &&
                Objects.equals(lastName.toLowerCase().trim(), author.lastName.toLowerCase().trim());
    }

    /**
     * Returns a hash code value for this author based on their first and last name (ignoring case and leading/trailing spaces).
     *
     * @return The hash code value for this author.
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName.toLowerCase().trim(), lastName.toLowerCase().trim());
    }
}

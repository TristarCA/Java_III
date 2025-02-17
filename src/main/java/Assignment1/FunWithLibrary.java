package Assignment1;

public class FunWithLibrary {
    public static void main(String[] args) {
        Book book1 = new Book("0136151579", "The Book of Shit", 4, 2025);
        Book book2 = new Book("9780345816023", "The Book of Fucking Shit", 1, 2023);
        Book book3 = new Book("9780201863561", "Your Mom is a Hoe", 2, 2024);

        // Create authors
        Author author1 = new Author("John", "Doe");
        Author author2 = new Author("Jane", "Smith");
        Author author3 = new Author("Michael", "Johnson");

        // Assign authors to books (testing duplicate handling)
        book1.addAuthor(author1);  // "The Book of Shit" by John Doe
        book2.addAuthor(author1);  // "Java Programming Basics" by John Doe
        book2.addAuthor(author2);  // "Java Programming Basics" by Jane Smith
        book3.addAuthor(author2);  // "Advanced Java Concepts" by Jane Smith
        book3.addAuthor(author3);  // "Advanced Java Concepts" by Michael Johnson

        // Trying to add the same author again to a book
        book1.addAuthor(author1);  // Attempting to add John Doe to "The Book of Shit" again (duplicate)
        book2.addAuthor(author2);  // Attempting to add Jane Smith to "Java Programming Basics" again (duplicate)

        // Display book details
        System.out.println("Book 1: " + book1.getTitle() + ", ISBN: " + book1.getISBN());
        System.out.print("Authors: ");
        book1.getAuthors();

        System.out.println("\nBook 2: " + book2.getTitle() + ", ISBN: " + book2.getISBN());
        System.out.print("Authors: ");
        book2.getAuthors();

        System.out.println("\nBook 3: " + book3.getTitle() + ", ISBN: " + book3.getISBN());
        System.out.print("Authors: ");
        book3.getAuthors();

        System.out.println("\nAuthor Information:");

        System.out.println(author1.getFirstName() + " " + author1.getLastName());
        author1.getTitles();  // John Doe's books
        System.out.println(author2.getFirstName() + " " + author2.getLastName());
        author2.getTitles();  // Jane Smith's books
        System.out.println(author3.getFirstName() + " " + author3.getLastName());
        author3.getTitles();  // Michael Johnson's books
    }
}

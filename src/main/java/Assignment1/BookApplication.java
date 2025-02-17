package Assignment1;

import java.util.Scanner;

/**
 * This class represents the main entry point for the book application.
 * It provides a user interface for interacting with the library system,
 * allowing the user to perform various actions like adding, removing,
 * and editing books and authors, as well as viewing details of the books and authors.
 */
public class BookApplication {

    /**
     * The main method that serves as the entry point of the Book Application.
     * It displays a menu and allows the user to choose from a set of options,
     * including printing all books, authors, adding/removing books, and editing book/author details.
     * The program will continue running until the user chooses to exit.
     *
     * @param args Command line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        Library lib = new Library();
        Scanner input = new Scanner(System.in);

        int selection;

        do {
            System.out.println("\nWelcome to the Library\n");
            System.out.println("Please select from the options below");
            System.out.println("1. Print all available books");
            System.out.println("2. Print all authors");
            System.out.println("3. Print all books by author");
            System.out.println("4. Add a book to the library");
            System.out.println("5. Remove a book from the system");
            System.out.println("6. Edit a book’s or author’s attributes");
            System.out.println("0. Exit");
            selection = input.nextInt();
            System.out.println();

            switch (selection) {
                case 1:
                    System.out.println("All available books:");
                    lib.printAllBooks();
                    break;

                case 2:
                    System.out.println("All available authors:");
                    lib.printAllAuthors();
                    break;

                case 3:
                    input.nextLine();
                    System.out.println("Please enter the name of the author you would like a list of books for: ");
                    String authorSearch = input.nextLine();
                    lib.printBooksByAuthor(authorSearch);
                    break;

                case 4:
                    input.nextLine();
                    System.out.println("Please enter the name of the book you would like to add:");
                    String newBookTitle = input.nextLine();

                    String newISBN;
                    do {
                        System.out.println("Please enter the isbn (should be exactly 10 digits long) of the book being added:");
                        newISBN = input.nextLine();
                    } while (newISBN.length() != 10 || !newISBN.matches("\\d{10}"));

                    String newEdition;
                    do {
                        System.out.println("Please enter the edition of the book being added:");
                        newEdition = input.nextLine();
                    } while (!newEdition.matches("\\d"));

                    String newCopyrightYear;
                    do {
                        System.out.println("Please enter the copyright year (4 digits) of the book being added:");
                        newCopyrightYear = input.nextLine();
                    } while (!newCopyrightYear.matches("\\d{4}"));

                    System.out.println("Please type the author's First Name:");
                    String newAuthorFirstName = input.nextLine();
                    System.out.println("Please type the author's Last Name:");
                    String newAuthorLastName = input.nextLine();

                    lib.addNewBookAndAuthor(newBookTitle, newISBN, Integer.parseInt(newEdition), Integer.parseInt(newCopyrightYear), newAuthorFirstName, newAuthorLastName);
                    break;

                case 5:
                    input.nextLine();
                    System.out.println("What is the name of the book to be removed?");
                    String removeTitle = input.nextLine();
                    lib.removeBookByTitle(removeTitle);
                    break;

                case 6:
                    System.out.println("Would you like to edit a book (1) or an author (2)?");
                    int editChoice = input.nextInt();
                    input.nextLine();

                    if (editChoice == 1) {
                        String editISBN;
                        do {
                            System.out.println("Please enter the isbn (should be exactly 10 digits long) of the book being edited:");
                            editISBN = input.nextLine();
                        } while (editISBN.length() != 10 || !editISBN.matches("\\d{10}"));

                        lib.editBookDetails(editISBN, input);
                    } else if (editChoice == 2) {
                        System.out.println("Enter the full name of the author you want to edit:");
                        String authorFullName = input.nextLine();
                        lib.editAuthorDetails(authorFullName, input);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid selection. Please try again.");
            }

        } while (selection != 0);

        input.close();
    }
}

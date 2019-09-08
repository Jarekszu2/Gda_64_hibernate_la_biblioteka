package biblioteka.packUtil;

import biblioteka.packDao.EntityDao;
import biblioteka.packModel.Author;
import biblioteka.packModel.Book;
import biblioteka.packModel.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ScannerWork {
    Scanner scanner = new Scanner(System.in);
    EntityDao entityDao = new EntityDao();

    public char getChar() {
        char znak = 'a';
        znak = scanner.nextLine().charAt(0);
        return znak;
    }

    public String getString() {
        String text = scanner.nextLine();
        return text;
    }

    public char getCharABC() {
        boolean flag = false;
        char znak = 'z';
        do {
            System.out.println();
            System.out.println("Choose: a,b,c / ?");
            znak = scanner.nextLine().charAt(0);
            switch (znak) {
                case 'a':
                    flag = true;
                    break;
                case 'b':
                    flag = true;
                    break;
                case 'c':
                    flag = true;
                    break;
            }
        } while (!flag);
        return znak;
    }

    public char getCharABCD() {
        boolean flag = false;
        char znak = 'z';
        do {
            System.out.println();
            System.out.println("Choose: a,b,c / ?");
            znak = scanner.nextLine().charAt(0);
            switch (znak) {
                case 'a':
                    flag = true;
                    break;
                case 'b':
                    flag = true;
                    break;
                case 'c':
                    flag = true;
                    break;
                case 'd':
                    flag = true;
                    break;
            }
        } while (!flag);
        return znak;
    }

    public Long getId() {
        System.out.println("Get id number:");
        Long id = Long.valueOf(scanner.nextLine());
        return id;
    }

    public Author createAuthor() {
        Author author = new Author();

        System.out.println("Get authors name:");
        String name = scanner.nextLine();
        author.setName(name);

        System.out.println("Get authors surname:");
        String surname = scanner.nextLine();
        author.setSurname(surname);

        System.out.println("Get authors date of birth(yyyy-mm-dd)");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
        author.setDateOfBirth(dateOfBirth);

        return author;
    }

    public void modifyAuthor(Long authorId) {
        Optional<Author> optionalAuthor = entityDao.getById(Author.class, authorId);
        if (optionalAuthor.isPresent()) {
            Author authorToModify = optionalAuthor.get();

            boolean flag = false;
            char znak = 'z';
            do {
                System.out.println();
                System.out.println("Choose:\n a) update name\n b) update surname\n c) update date of birth");
                znak = getCharABC();
                switch (znak) {
                    case 'a':
                        System.out.println("Get new name:");
                        String newName = scanner.nextLine();
                        authorToModify.setName(newName);

                        entityDao.saveOrUpdate(authorToModify);
                        flag = true;
                        break;
                    case 'b':
                        System.out.println("Get new surname:");
                        String newSurname = scanner.nextLine();
                        authorToModify.setSurname(newSurname);

                        entityDao.saveOrUpdate(authorToModify);
                        flag = true;
                        break;
                    case 'c':
                        System.out.println("Get new dat of birth(yyyy-mm-dd):");
                        LocalDate localDate = LocalDate.parse(scanner.nextLine());
                        authorToModify.setDateOfBirth(localDate);

                        entityDao.saveOrUpdate(authorToModify);
                        flag = true;
                        break;
                }
            } while (!flag);
//            return authorToModify;
        }
        else {
            try {
                throw new NoSuchItemInDatabaseException("No such Author in the database.");
            } catch (NoSuchItemInDatabaseException e) {
                System.out.println(e.getMessage());

            }
        }
    }

    public Book createBook() {
        Book book = new Book();

        System.out.println("Get books title:");
        String title = scanner.nextLine();
        book.setTitle(title);

        System.out.println("Get books year written:");
        int yearWritten = Integer.parseInt(scanner.nextLine());
        book.setYearWritten(yearWritten);

        System.out.println("Get books number of pages:");
        int nrOfPages = Integer.parseInt(scanner.nextLine());
        book.setNumberOfPages(nrOfPages);

        System.out.println("Get books number of available copies:");
        int nrCopies = Integer.parseInt(scanner.nextLine());
        book.setNumberOfAvailableCopies(nrCopies);

        return book;
    }

    public void modifyBook(Long authorId) {
        Optional<Book> optionalBook = entityDao.getById(Book.class, authorId);
        if (optionalBook.isPresent()) {
            Book bookToModify = optionalBook.get();

            boolean flag = false;
            char znak = 'z';
            do {
                System.out.println();
                System.out.println("Choose:\n a) update title\n b) update year written\n c) update number of pages" +
                        "\n d) update number of available copies");
                znak = getCharABCD();
                switch (znak) {
                    case 'a':
                        System.out.println("Get new title:");
                        String newTitle = scanner.nextLine();
                        bookToModify.setTitle(newTitle);

                        entityDao.saveOrUpdate(bookToModify);
                        flag = true;
                        break;
                    case 'b':
                        System.out.println("Get new year written:");
                        int newYearWritten = Integer.parseInt(scanner.nextLine());
                        bookToModify.setYearWritten(newYearWritten);

                        entityDao.saveOrUpdate(bookToModify);
                        flag = true;
                        break;
                    case 'c':
                        System.out.println("Get new number of pages:");
                        int newNrOfPages = Integer.parseInt(scanner.nextLine());
                        bookToModify.setNumberOfPages(newNrOfPages);

                        entityDao.saveOrUpdate(bookToModify);
                        flag = true;
                        break;
                    case 'd':
                        System.out.println("Get new number of available copies:");
                        int newNrOfCopies = Integer.parseInt(scanner.nextLine());
                        bookToModify.setNumberOfPages(newNrOfCopies);

                        entityDao.saveOrUpdate(bookToModify);
                        flag = true;
                        break;
                }
            } while (!flag);
//            return authorToModify;
        } else {
            try {
                throw new NoSuchBookException("No such Book in the database.");
            } catch (NoSuchBookException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Client createClient() {
        Client client = new Client();

        System.out.println("Get clients name:");
        String name = scanner.nextLine();
        client.setName(name);

        System.out.println("Get clients surname:");
        String surname = scanner.nextLine();
        client.setSurname(surname);

        System.out.println("Get clients idNumber:");
        String idNumber = scanner.nextLine();
        client.setIdNumber(idNumber);

        return client;
    }

    public void modifyClient(Long clientId) {
        Optional<Client> optionalClient = entityDao.getById(Client.class, clientId);
        if (optionalClient.isPresent()) {
            Client clientToModify = optionalClient.get();

            boolean flag = false;
            char znak = 'z';
            do {
                System.out.println();
                System.out.println("Choose:\n a) update name\n b) update surname\n c) update idNumber");
                znak = getCharABC();
                switch (znak) {
                    case 'a':
                        System.out.println("Get new clients name:");
                        String name = scanner.nextLine();
                        clientToModify.setName(name);

                        entityDao.saveOrUpdate(clientToModify);
                        flag = true;
                        break;
                    case 'b':
                        System.out.println("Get new clients surname:");
                        String newSurname = scanner.nextLine();
                        clientToModify.setSurname(newSurname);

                        entityDao.saveOrUpdate(clientToModify);
                        flag = true;
                        break;
                    case 'c':
                        System.out.println("Get new clients idNumber:");
                        String idNumber = scanner.nextLine();
                        clientToModify.setIdNumber(idNumber);

                        entityDao.saveOrUpdate(clientToModify);
                        flag = true;
                        break;
                }
            } while (!flag);
//            return authorToModify;
        }
        else {
            try {
                throw new NoSuchItemInDatabaseException("No such Client in the database.");
            } catch (NoSuchItemInDatabaseException e) {
                System.out.println(e.getMessage());

            }
        }
    }

    public Client test(Long clientId) throws NoSuchItemInDatabaseException {
        Optional<Client> optionalClient = entityDao.getById(Client.class, clientId);
        if (optionalClient.isPresent()) {
            Client clientToModify = optionalClient.get();

            System.out.println("Get new clients surname:");
            String newSurname = scanner.nextLine();
            clientToModify.setSurname(newSurname);

            return clientToModify;
        } else {
            throw new NoSuchItemInDatabaseException("No such Client in database.");
        }
    }

    public void printMessageY(List<Book> bookList) {
        if (bookList.size() > 0) {
            bookList.forEach(System.err::println);
        } else {
            System.err.println("All books available.");
        }
    }

    public void printMessageZ(List<Book> bookList, Long numberOfDays) {
        if (bookList.size() > 0) {
            bookList.forEach(System.err::println);
        } else {
            System.err.println("No books have been lent in last " + numberOfDays + " days.");
        }
    }

    public void printMessage3(Client client) {
        if (client != null) {
            System.err.println(client);
        } else {
            System.err.println("No one have lent books.");
        }
    }
}

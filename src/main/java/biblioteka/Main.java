package biblioteka;

/*
Każdy model ma id.
Author:
- name,
- surname,
- date of birth,
- number of books (formula)
- books,
Book:
- title,
- year written,
- how old(formula),
- number of pages,
- number of available copies
- number of borrowed copies(formula)
- authors,
- current lents (set <BookLent>) (obecnie wypożyczenia)
BookLent:
- client,
- book,
- date lent,
- date returned,
Client:
- name,
- surname,
- id number,
- set of lent books,
*** PublishingHouse: (wydawnictwo)
- name
- set<Book>
Metody:
- dodawanie autorów,
- listowanie autorów
- usuwanie autorów
- modyfikacja imienia/nazwiska lub roku urodzeznia autora
- dodawanie książek,
- listowanie ksiazek
- usuwanie ksiazek
- modyfikacja tytulu/roku napisania/ilosci stron w ksiazce
- dodawanie klientów,
- listowanie klientow
- usuwanie klientow
- modyfikacja imienia/nazwiska lub numeru id klienta


Polecenia (poza crud):
Lajtowe:
- dodawanie powiązania między książką a autorem (ten autor napisał daną książkę)
- dodawanie wypożyczeń (BookLent) danemu klientowi na daną książkę
- znajdowanie autorów(liczba mnoga) po nazwisku
- znajdowanie klientów po nazwisku
- znajdowanie klientA po id number,
Praktyczne
- listowanie książek wypożyczonych przez klienta
- listowanie książek nie zwróconych przez klienta
- listowanie książek których SĄ jeszcze kopie
- listowanie książek których nie ma już kopii
- listowanie książek które nie zostały zwrócone
- listowanie książek które zostały zwrócone w ciągu ostatnich N godzin.
- listowanie książek które zostały wypożyczone w ciągu ostatnich 24 h
- listowanie najczęściej wypożyczanych książek
- znalezienie najbardziej aktywnego klienta (takiego który najczęściej wypożycza)
 */

import biblioteka.packDao.*;
import biblioteka.packModel.Author;
import biblioteka.packModel.Book;
import biblioteka.packModel.BookLent;
import biblioteka.packModel.Client;
import biblioteka.packUtil.HibernateUtil;
import biblioteka.packUtil.NoSuchItemInDatabaseException;
import biblioteka.packUtil.ScannerWork;

import java.util.List;
import java.util.Scanner;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession().close();
        EntityDao entityDao = new EntityDao();
        ScannerWork scannerWork = new ScannerWork();
        AuthorDao authorDao = new AuthorDao();
        BookLentDao bookLentDao = new BookLentDao();
        ClientDao clientDao = new ClientDao();
        BookDao bookDao = new BookDao();


        boolean flag = false;
        do {
            System.out.println();
            System.out.println("Choose:\n a) insert author\n b) print all authors\n c) delete author by id" +
                    "\n d) modify author\n e) insert book\n f) print all books\n g) delete book by id" +
                    "\n h) modify book\n i) insert client\n j) print all clients\n k) delete client by id" +
                    "\n l) modify client\n m) add connection author_book\n n) add BookLent(Client, Book)" +
                    "\n o) print all BookLents\n O) print open book lents\n p) find all authors by surname\n r) find all clients by surname" +
                    "\n s) find client by id\n t) print books lent by client\n u) print books ids lent by client" +
                    "\n v) print books do not get back by client\n w) get book back\n x) print books they are not lent" +
                    "\n y) print books they are not available\n z) books lent in last N days\n 2) print the most lent book" +
                    "\n 3) get the most lent client\n q) quit");
            char znak = scannerWork.getChar();
            switch (znak) {
                case 'a':
                    Author authorA = scannerWork.createAuthor();
                    entityDao.saveOrUpdate(authorA);
                    System.err.println("Author added.");
                    break;
                case 'b':
                    authorDao.printAllAuthors();
                    break;
                case 'c':
                    Long idC = scannerWork.getId();
                    entityDao.delete(Author.class, idC);
                    System.err.println("Author deleted.");
                    break;
                case 'd':
                    Long idD = scannerWork.getId();
                    scannerWork.modifyAuthor(idD);
                    System.err.println("Author modified.");
                    break;
                case 'e':
                    Book bookE = scannerWork.createBook();
                    entityDao.saveOrUpdate(bookE);
                    System.err.println("Book added.");
                    break;
                case 'f':
    // dlaczego nie wypisyje Set<Author>
                    List<Book> bookListF = entityDao.getAll(Book.class);
                    bookListF.forEach(System.err::println);
                    break;
                case 'g':
                    Long idG = scannerWork.getId();
                    entityDao.delete(Book.class, idG);
                    System.err.println("Book deleted.");
                    break;
                case 'h':
                    Long idH = scannerWork.getId();
                    scannerWork.modifyBook(idH);
                    System.err.println("Book modified.");
                    break;
                case 'i':
                    Client clientI = scannerWork.createClient();
                    entityDao.saveOrUpdate(clientI);
                    System.err.println("Client added.");
                    break;
                case 'j':
                    List<Client> clientListJ = entityDao.getAll(Client.class);
                    clientListJ.forEach(System.err::println);
                    break;
                case 'k':
                    Long idK = scannerWork.getId();
                    entityDao.delete(Client.class, idK);
                    System.err.println("Client deleted.");
                    break;
                case 'l':
                    Long idL = scannerWork.getId();
                    Client clientL = null;
                    try {
                        clientL = scannerWork.test(idL);
                        entityDao.saveOrUpdate(clientL);
                        System.err.println("Client modified.");
                    } catch (NoSuchItemInDatabaseException e) {
//                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                    break;
                case 'm':
                    System.out.println("Get authorId");
                    Long authorId = scannerWork.getId();
                    System.out.println("Get bookId");
                    Long bookId = scannerWork.getId();
                    authorDao.addConnectionAuthor_Book(authorId, bookId);
                    System.err.println("Connection added.");
                    break;
                case 'n':
                    System.out.println("Get clientId");
                    Long clientIdN = scannerWork.getId();
                    System.out.println("Get bookId");
                    Long bookIdN = scannerWork.getId();
                    try {
                        bookLentDao.createBookLent(bookIdN, clientIdN);
                        System.err.println("BookLent created.");
                    } catch (NoSuchItemInDatabaseException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case 'o':
                    List<BookLent> bookLentListO = entityDao.getAll(BookLent.class);
                    bookLentListO.forEach(System.err::println);
                    break;
                case 'O':
                    bookLentDao.getOpenBookLents();
                    break;
                case 'p':
                    System.out.println("Get authors surname:");
                    String surnameP = scannerWork.getString();
                    authorDao.findAuthorsBySurname(surnameP);
                    break;
                case 'r':
                    System.out.println("Get clients surname:");
                    String surnameR = scannerWork.getString();
                    List<Client> clientListR = clientDao.findClientsBySurname(surnameR);
                    clientListR.forEach(System.err::println);
                    break;
                case 's':
                    System.out.println("Get clientId");
                    Long clientIdS = scannerWork.getId();
                    try {
                        Client clientS = clientDao.findClientById(clientIdS);
                        System.err.println(clientS);
                    } catch (NoSuchItemInDatabaseException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case 't':
    // dlaczego listuje numberOfBorrowedCopies = 0, dlaczego w metodzie joinClient ma klasę Book (zmienimy na Client i nic się nie dzieje)
                    System.out.println("Get clientId");
                    Long clientIdT = scannerWork.getId();
                    bookLentDao.getBooksLentByClient(clientIdT);
                    break;
                case 'u':
                    System.out.println("Get clientId");
                    Long clientIdU = scannerWork.getId();
                    try {
                        List<Long> longListU = bookLentDao.getBookIdsLentByClient(clientIdU);
                        longListU.forEach(System.err::println);
                    } catch (NoSuchItemInDatabaseException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'v':
                    System.out.println("Get clientId");
                    Long clientIdV = scannerWork.getId();
                    try {
                        List<Book> bookListV = bookLentDao.getBooksDoNotGetBackByClient(clientIdV);
                        bookListV.forEach(System.err::println);
                    } catch (NoSuchItemInDatabaseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 'w':
                    System.out.println("Get bookId");
                    Long bookIdW = scannerWork.getId();
                    bookLentDao.getBookBack(bookIdW);
                    System.err.println("The book got back.");
                    break;
                case 'x':
                    bookDao.printBooksWhereAvailableCopiesMoreThanZero();
                    break;
                case 'y':
                    List<Book> bookListY = bookDao.printBooksWhereAvailableCopiesAreZero();
                    scannerWork.printMessageY(bookListY);
                    break;
                case 'z':
                    System.out.println("Get number of days, to check namber of books where lent:");
                    Long bookIdZ = scannerWork.getId();
                    List<Book> bookListZ = bookLentDao.getBooksLentInLastNDays(bookIdZ);
                    scannerWork.printMessageZ(bookListZ, bookIdZ);
                    break;
                case '2':
                    bookLentDao.printTheMostLentBook();
                    break;
                case '3':
                    Client theMostLentClient = bookLentDao.getTheMostLentClient();
                    scannerWork.printMessage3(theMostLentClient);
                    break;
                case 'q':
                    flag = true;
                    break;
            }
        } while (!flag);
    }
}

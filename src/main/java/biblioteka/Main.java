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

import biblioteka.packDao.AuthorDao;
import biblioteka.packDao.EntityDao;
import biblioteka.packModel.Author;
import biblioteka.packModel.Book;
import biblioteka.packModel.Client;
import biblioteka.packUtil.HibernateUtil;
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


        boolean flag = false;
        do {
            System.out.println();
            System.out.println("Choose:\n a) insert author\n b) print all authors\n c) delete author by id" +
                    "\n d) modify author\n e) insert book\n f) print all books\n g) delete book by id" +
                    "\n h) modify book\n i) insert client\n j) print all clients\n q) quit");
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
    // jak w skanerze zrobić metodę nie voidową, ma zwracać Author - modifyAuthor(id)
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
                case 'q':
                    flag = true;
                    break;
            }
        } while (!flag);
    }
}

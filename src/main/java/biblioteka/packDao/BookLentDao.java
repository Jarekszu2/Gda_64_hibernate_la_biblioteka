package biblioteka.packDao;

import biblioteka.packModel.Book;
import biblioteka.packModel.BookLent;
import biblioteka.packModel.Client;
import biblioteka.packUtil.HibernateUtil;
import biblioteka.packUtil.NoSuchItemInDatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookLentDao {
    EntityDao entityDao = new EntityDao();
//    public List<Book> getBooksLentByClient(Long clientId) {
//        // select * from BookLent bl join Book b on bl.book_id = b.id where bl.client_id = ?
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
//            Root<BookLent> blRoot = criteriaQuery.from(BookLent.class);
//            Join<BookLent, Book> join = blRoot.join("book", JoinType.LEFT);
//            Join<BookLent, Book> joinClient = blRoot.join("book", JoinType.LEFT);
//
//        }
//    }

    public void createBookLent(Long bookId, Long clientId) throws NoSuchItemInDatabaseException {
        Optional<Book> optionalBook = entityDao.getById(Book.class, bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            // sprawdzam, czy są dostępne egzemplarze książki
            if (book.getNumberOfAvailableCopies() > 0) {
                book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() - 1);

                Optional<Client> optionalClient = entityDao.getById(Client.class, clientId);
                if (optionalClient.isPresent()) {
                    Client client = optionalClient.get();

                    BookLent bookLent = new BookLent();
                    bookLent.setBook(book);
                    bookLent.setClient(client);

                    entityDao.saveOrUpdate(bookLent);
                    entityDao.saveOrUpdate(book);
                }
            } else {
                throw new NoSuchItemInDatabaseException("All books lent.");
            }
        }
    }

    // listowanie tylko czynnych wypożyczeń
    public void getOpenBookLents() {
        List<BookLent> bookLentList = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<BookLent> bookLentCriteriaQuery = criteriaBuilder.createQuery(BookLent.class);
            Root<BookLent> bookLentRoot = bookLentCriteriaQuery.from(BookLent.class);

            LocalDate localDate = LocalDate.parse("2019-09-05");
            LocalDate localDate1 = null;

            bookLentCriteriaQuery.select(bookLentRoot)
                    .where(criteriaBuilder.isNull(bookLentRoot.get("dateReturned")));
//                    .where(criteriaBuilder.equal(bookLentRoot.get("dateReturned"), localDate));

//            bookLentList.addAll(session.createQuery(bookLentCriteriaQuery).list());
//
//            bookLentList.forEach(System.err::println);

            session.createQuery(bookLentCriteriaQuery).list().forEach(System.err::println);
        }
    }

    // - listowanie książek wypożyczonych przez klienta
    public void getBooksLentByClient(Long clientId) {
        // select * from BookLent bl join Book b on bl.book_id = b.id where bl.client_id = ?
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = cb.createQuery(Book.class);
            Root<BookLent> blRoot = criteriaQuery.from(BookLent.class);
            Join<BookLent, Book> joinBook = blRoot.join("book", JoinType.LEFT);
            Join<BookLent, Book> joinClient = blRoot.join("client", JoinType.LEFT);
            criteriaQuery.select(cb.construct(Book.class,
                    joinBook.get("id"),
                    joinBook.get("title"),
                    joinBook.get("yearWritten"),
                    joinBook.get("numberOfPages"),
                    joinBook.get("numberOfAvailableCopies")
            )).where(
                    cb.equal(joinClient.get("id"), clientId)
            );
            session.createQuery(criteriaQuery).list().forEach(System.err::println);
        }
    }

    // - listowanie książek wypożyczonych przez klienta (tylko id)
    public List<Long> getBookIdsLentByClient(Long clientId) throws NoSuchItemInDatabaseException {
        Optional<Client> optionalClient = entityDao.getById(Client.class, clientId);
        if (optionalClient.isPresent()) {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            try (Session session = sessionFactory.openSession()) {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<Long> longCriteriaQuery = criteriaBuilder.createQuery(Long.class);
                Root<BookLent> bookLentRoot = longCriteriaQuery.from(BookLent.class);

                longCriteriaQuery.select(bookLentRoot.get("book").get("id"))
                        .where(criteriaBuilder.equal(bookLentRoot.get("client").get("id"), clientId));

                return session.createQuery(longCriteriaQuery).list();
            }
        } else {
            throw new NoSuchItemInDatabaseException("No such client (clientId = " + clientId + ") in the database.");
        }
    }

    // - listowanie książek nie zwróconych przez klienta
    public List<Book> getBooksDoNotGetBackByClient(Long clientId) throws NoSuchItemInDatabaseException {
        Optional<Client> optionalClient = entityDao.getById(Client.class, clientId);
        if (optionalClient.isPresent()) {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            try (Session session = sessionFactory.openSession()) {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Book> criteriaQuery = cb.createQuery(Book.class);
                Root<BookLent> blRoot = criteriaQuery.from(BookLent.class);
                Join<BookLent, Book> joinBook = blRoot.join("book", JoinType.LEFT);
                Join<BookLent, Client> joinClient = blRoot.join("client", JoinType.INNER);

                LocalDate localDate = null;

                criteriaQuery.select(cb.construct(Book.class,
                        joinBook.get("id"),
                        joinBook.get("title"),
                        joinBook.get("yearWritten"),
                        joinBook.get("numberOfPages"),
                        joinBook.get("numberOfAvailableCopies")
                )).where(
                        cb.equal(blRoot.get("dateReturned"), localDate),
                        cb.equal(joinClient.get("id"), clientId)
                );


//                criteriaQuery.select(cb.construct(Book.class,
//                        joinBook.get("id"),
//                        joinBook.get("title"),
//                        joinBook.get("yearWritten"),
//                        joinBook.get("numberOfPages"),
//                        joinBook.get("numberOfAvailableCopies")
//                )).where(
//                        cb.equal(blRoot.get("dateReturned"), null),
//                        cb.equal(joinClient.get("id"), clientId)
//                );

//                session.createQuery(criteriaQuery).list().forEach(System.err::println);
                return session.createQuery(criteriaQuery).getResultList();
            }
        } else {
            throw new NoSuchItemInDatabaseException("No such client (clientId = " + clientId + ") in the database.");
        }
    }

    public BookLent getBookLentByBook(Long bookId) throws NoSuchItemInDatabaseException {
        Optional<Book> optionalBook = entityDao.getById(Book.class, bookId);
        if (optionalBook.isPresent()) {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            try (Session session = sessionFactory.openSession()) {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<BookLent> bookLentCriteriaQuery = criteriaBuilder.createQuery(BookLent.class);
                Root<BookLent> bookLentRoot = bookLentCriteriaQuery.from(BookLent.class);

                bookLentCriteriaQuery.select(bookLentRoot)
                        .where(criteriaBuilder.equal(bookLentRoot.get("book").get("id"), bookId));

                return session.createQuery(bookLentCriteriaQuery).getSingleResult();
            }
        } else {
            throw new NoSuchItemInDatabaseException("No such book in the database.");
        }
    }

    public void getBookBack(Long bookId) {
        try {
            BookLent bookLent = getBookLentByBook(bookId);
            bookLent.setDateReturned(LocalDate.now());
            entityDao.saveOrUpdate(bookLent);

            Book book = bookLent.getBook();
            book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() + 1);
            entityDao.saveOrUpdate(book);

        } catch (NoSuchItemInDatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    // - listowanie książek które zostały zwrócone w ciągu ostatnich N godzin.
    // - listowanie książek które zostały wypożyczone w ciągu ostatnich 24 h
    public List<Book> getBooksLentInLastNDays(Long numberOfDays) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> bookCriteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<BookLent> bookLentRoot = bookCriteriaQuery.from(BookLent.class);

            bookCriteriaQuery.select(bookLentRoot.get("book"))
                    .where(criteriaBuilder
                            .between(bookLentRoot.get("dateLent"), LocalDate.now().minusDays(numberOfDays), LocalDate.now()));

            return session.createQuery(bookCriteriaQuery).getResultList();
        }
    }

    // - listowanie najczęściej wypożyczanych książek
    public void printTheMostLentBook() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> bookCriteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<BookLent> bookLentRoot = bookCriteriaQuery.from(BookLent.class);

            bookCriteriaQuery.select(bookLentRoot.get("book"))
                    .groupBy(bookLentRoot.get("book"))
                    .orderBy(criteriaBuilder.desc(criteriaBuilder.count(bookLentRoot.get("book"))));

            session.createQuery(bookCriteriaQuery).setMaxResults(1).getResultList().forEach(System.err::println);
        }
    }

    // - znalezienie najbardziej aktywnego klienta (takiego który najczęściej wypożycza)
    public Client getTheMostLentClient() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Client> clientCriteriaQuery = criteriaBuilder.createQuery(Client.class);
            Root<BookLent> bookLentRoot = clientCriteriaQuery.from(BookLent.class);

            clientCriteriaQuery.select(bookLentRoot.get("client"))
                    .groupBy(bookLentRoot.get("client"))
                    .orderBy(criteriaBuilder.desc(criteriaBuilder.count(bookLentRoot.get("client"))));

            return session.createQuery(clientCriteriaQuery).setMaxResults(1).getSingleResult();
        }
    }
}

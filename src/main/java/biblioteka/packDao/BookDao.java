package biblioteka.packDao;

import biblioteka.packModel.Book;
import biblioteka.packUtil.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class BookDao {

    // - listowanie książek których SĄ jeszcze kopie
    public void printBooksWhereAvailableCopiesMoreThanZero() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> bookCriteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> bookRoot = bookCriteriaQuery.from(Book.class);

            bookCriteriaQuery.select(bookRoot)
                    .where(criteriaBuilder.greaterThan(bookRoot.get("numberOfAvailableCopies"), 0));

//            bookCriteriaQuery.select(bookRoot).where(criteriaBuilder.)

            session.createQuery(bookCriteriaQuery).getResultList().forEach(System.err::println);
        }
    }

    // - listowanie książek których nie ma już kopii
    public List<Book> printBooksWhereAvailableCopiesAreZero() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> bookCriteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> bookRoot = bookCriteriaQuery.from(Book.class);

            bookCriteriaQuery.select(bookRoot)
                    .where(criteriaBuilder.equal(bookRoot.get("numberOfAvailableCopies"), 0));

            return session.createQuery(bookCriteriaQuery).getResultList();
        }
    }
}

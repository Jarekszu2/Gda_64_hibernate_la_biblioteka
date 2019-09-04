package biblioteka.packDao;

import biblioteka.packUtil.HibernateUtil;
import biblioteka.packModel.Author;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao {
    private EntityDao entityDao = new EntityDao();

    public void printAllAuthors2(){
        List<Author> list = entityDao.getAll(Author.class);
        list.forEach(System.err::println);
    }

    public void printAllAuthors() {
        List<Author> authorList = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {

            // Narzędzie do kreowania zapytania, do tworzenia query i budowania klauzuli 'where'
            CriteriaBuilder cb = session.getCriteriaBuilder();

            // Obiekt reprezentujący zapytanie o <typ generyczny>
            CriteriaQuery<Author> criteriaQuery = cb.createQuery(Author.class);

            // reprezentuje tabelę 'Student' i tworzymy tą instancję żeby powiedzieć
            // "do jakiej tabeli chcemy wykonać zapytanie"
            Root<Author> rootTable = criteriaQuery.from(Author.class);

            // wykonanie select'a z tabeli
            criteriaQuery.select(rootTable);

            // wywołujemy zapytanie, wyniki zbieramy do listy
            authorList.addAll(session.createQuery(criteriaQuery).list());

            authorList.forEach(System.err::println);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public void deleteAuthor() {

    }
}

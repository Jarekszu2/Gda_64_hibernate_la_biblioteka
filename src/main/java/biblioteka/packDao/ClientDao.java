package biblioteka.packDao;

import biblioteka.packModel.Client;
import biblioteka.packUtil.HibernateUtil;
import biblioteka.packUtil.NoSuchItemInDatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class ClientDao {
    EntityDao entityDao = new EntityDao();

    public List<Client> findClientsBySurname(String surname) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session =sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);
            Root<Client> clientRoot = criteriaQuery.from(Client.class);

            criteriaQuery.select(clientRoot).where(criteriaBuilder.equal(clientRoot.get("surname"), surname));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public Client findClientById(Long clientId) throws NoSuchItemInDatabaseException {
        Optional<Client> optionalClient = entityDao.getById(Client.class, clientId);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            return client;
        } else {
            throw new NoSuchItemInDatabaseException("No such clients id.");
        }
    }
}

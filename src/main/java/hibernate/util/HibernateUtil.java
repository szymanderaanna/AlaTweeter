package hibernate.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
    private static HibernateUtil instance;

    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("myDatabase");
    private final EntityManager entityManager = factory.createEntityManager();

    private HibernateUtil() {
    }

    public static HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }

    public void save(Object o) {
        entityManager.getTransaction().begin();
        if (entityManager.contains(o)) {
            entityManager.persist(o);
            entityManager.flush();
        }
        entityManager.getTransaction().commit();
    }
    public void delete(Class clazz, Long id) {
        entityManager.getTransaction().begin();
        Object toRemove = entityManager.find(clazz, id);
        entityManager.remove(toRemove);
        entityManager.getTransaction().commit();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}




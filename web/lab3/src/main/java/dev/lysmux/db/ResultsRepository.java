package dev.lysmux.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


import java.util.List;

@ApplicationScoped
public class ResultsRepository {
    @PersistenceContext(name = "pg")
    private EntityManager em;

    @Transactional
    public void add(Result result) {
        em.persist(result);
    }

    @Transactional
    public void clear() {
        em.createQuery("DELETE FROM Result").executeUpdate();
    }

    public List<Result> getAll() {
        return em.createQuery("SELECT r FROM Result r ORDER BY r.id", Result.class)
                .getResultList();
    }
}

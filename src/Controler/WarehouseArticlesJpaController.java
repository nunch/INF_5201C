/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Controler.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Article;
import Entity.WarehouseArticles;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class WarehouseArticlesJpaController implements Serializable {

    public WarehouseArticlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WarehouseArticles warehouseArticles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article articleId = warehouseArticles.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getId());
                warehouseArticles.setArticleId(articleId);
            }
            em.persist(warehouseArticles);
            if (articleId != null) {
                articleId.getWarehouseArticlesList().add(warehouseArticles);
                articleId = em.merge(articleId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WarehouseArticles warehouseArticles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WarehouseArticles persistentWarehouseArticles = em.find(WarehouseArticles.class, warehouseArticles.getId());
            Article articleIdOld = persistentWarehouseArticles.getArticleId();
            Article articleIdNew = warehouseArticles.getArticleId();
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getId());
                warehouseArticles.setArticleId(articleIdNew);
            }
            warehouseArticles = em.merge(warehouseArticles);
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getWarehouseArticlesList().remove(warehouseArticles);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getWarehouseArticlesList().add(warehouseArticles);
                articleIdNew = em.merge(articleIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = warehouseArticles.getId();
                if (findWarehouseArticles(id) == null) {
                    throw new NonexistentEntityException("The warehouseArticles with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WarehouseArticles warehouseArticles;
            try {
                warehouseArticles = em.getReference(WarehouseArticles.class, id);
                warehouseArticles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The warehouseArticles with id " + id + " no longer exists.", enfe);
            }
            Article articleId = warehouseArticles.getArticleId();
            if (articleId != null) {
                articleId.getWarehouseArticlesList().remove(warehouseArticles);
                articleId = em.merge(articleId);
            }
            em.remove(warehouseArticles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WarehouseArticles> findWarehouseArticlesEntities() {
        return findWarehouseArticlesEntities(true, -1, -1);
    }

    public List<WarehouseArticles> findWarehouseArticlesEntities(int maxResults, int firstResult) {
        return findWarehouseArticlesEntities(false, maxResults, firstResult);
    }

    private List<WarehouseArticles> findWarehouseArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WarehouseArticles.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public WarehouseArticles findWarehouseArticles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WarehouseArticles.class, id);
        } finally {
            em.close();
        }
    }

    public int getWarehouseArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WarehouseArticles> rt = cq.from(WarehouseArticles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

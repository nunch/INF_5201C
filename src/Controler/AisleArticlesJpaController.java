/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Controler.exceptions.NonexistentEntityException;
import Entity.AisleArticles;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Article;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class AisleArticlesJpaController implements Serializable {

    public AisleArticlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AisleArticles aisleArticles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article articleId = aisleArticles.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getId());
                aisleArticles.setArticleId(articleId);
            }
            em.persist(aisleArticles);
            if (articleId != null) {
                articleId.getAisleArticlesList().add(aisleArticles);
                articleId = em.merge(articleId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AisleArticles aisleArticles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AisleArticles persistentAisleArticles = em.find(AisleArticles.class, aisleArticles.getId());
            Article articleIdOld = persistentAisleArticles.getArticleId();
            Article articleIdNew = aisleArticles.getArticleId();
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getId());
                aisleArticles.setArticleId(articleIdNew);
            }
            aisleArticles = em.merge(aisleArticles);
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getAisleArticlesList().remove(aisleArticles);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getAisleArticlesList().add(aisleArticles);
                articleIdNew = em.merge(articleIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = aisleArticles.getId();
                if (findAisleArticles(id) == null) {
                    throw new NonexistentEntityException("The aisleArticles with id " + id + " no longer exists.");
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
            AisleArticles aisleArticles;
            try {
                aisleArticles = em.getReference(AisleArticles.class, id);
                aisleArticles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aisleArticles with id " + id + " no longer exists.", enfe);
            }
            Article articleId = aisleArticles.getArticleId();
            if (articleId != null) {
                articleId.getAisleArticlesList().remove(aisleArticles);
                articleId = em.merge(articleId);
            }
            em.remove(aisleArticles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AisleArticles> findAisleArticlesEntities() {
        return findAisleArticlesEntities(true, -1, -1);
    }

    public List<AisleArticles> findAisleArticlesEntities(int maxResults, int firstResult) {
        return findAisleArticlesEntities(false, maxResults, firstResult);
    }

    private List<AisleArticles> findAisleArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AisleArticles.class));
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

    public AisleArticles findAisleArticles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AisleArticles.class, id);
        } finally {
            em.close();
        }
    }

    public int getAisleArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AisleArticles> rt = cq.from(AisleArticles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

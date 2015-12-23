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
import Entity.Command;
import Entity.Article;
import Entity.CommandedArticles;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class CommandedArticlesJpaController implements Serializable {

    public CommandedArticlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CommandedArticles commandedArticles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Command commandId = commandedArticles.getCommandId();
            if (commandId != null) {
                commandId = em.getReference(commandId.getClass(), commandId.getId());
                commandedArticles.setCommandId(commandId);
            }
            Article articleId = commandedArticles.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getId());
                commandedArticles.setArticleId(articleId);
            }
            em.persist(commandedArticles);
            if (commandId != null) {
                commandId.getCommandedArticlesList().add(commandedArticles);
                commandId = em.merge(commandId);
            }
            if (articleId != null) {
                articleId.getCommandedArticlesList().add(commandedArticles);
                articleId = em.merge(articleId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CommandedArticles commandedArticles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CommandedArticles persistentCommandedArticles = em.find(CommandedArticles.class, commandedArticles.getId());
            Command commandIdOld = persistentCommandedArticles.getCommandId();
            Command commandIdNew = commandedArticles.getCommandId();
            Article articleIdOld = persistentCommandedArticles.getArticleId();
            Article articleIdNew = commandedArticles.getArticleId();
            if (commandIdNew != null) {
                commandIdNew = em.getReference(commandIdNew.getClass(), commandIdNew.getId());
                commandedArticles.setCommandId(commandIdNew);
            }
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getId());
                commandedArticles.setArticleId(articleIdNew);
            }
            commandedArticles = em.merge(commandedArticles);
            if (commandIdOld != null && !commandIdOld.equals(commandIdNew)) {
                commandIdOld.getCommandedArticlesList().remove(commandedArticles);
                commandIdOld = em.merge(commandIdOld);
            }
            if (commandIdNew != null && !commandIdNew.equals(commandIdOld)) {
                commandIdNew.getCommandedArticlesList().add(commandedArticles);
                commandIdNew = em.merge(commandIdNew);
            }
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getCommandedArticlesList().remove(commandedArticles);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getCommandedArticlesList().add(commandedArticles);
                articleIdNew = em.merge(articleIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = commandedArticles.getId();
                if (findCommandedArticles(id) == null) {
                    throw new NonexistentEntityException("The commandedArticles with id " + id + " no longer exists.");
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
            CommandedArticles commandedArticles;
            try {
                commandedArticles = em.getReference(CommandedArticles.class, id);
                commandedArticles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commandedArticles with id " + id + " no longer exists.", enfe);
            }
            Command commandId = commandedArticles.getCommandId();
            if (commandId != null) {
                commandId.getCommandedArticlesList().remove(commandedArticles);
                commandId = em.merge(commandId);
            }
            Article articleId = commandedArticles.getArticleId();
            if (articleId != null) {
                articleId.getCommandedArticlesList().remove(commandedArticles);
                articleId = em.merge(articleId);
            }
            em.remove(commandedArticles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CommandedArticles> findCommandedArticlesEntities() {
        return findCommandedArticlesEntities(true, -1, -1);
    }

    public List<CommandedArticles> findCommandedArticlesEntities(int maxResults, int firstResult) {
        return findCommandedArticlesEntities(false, maxResults, firstResult);
    }

    private List<CommandedArticles> findCommandedArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CommandedArticles.class));
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

    public CommandedArticles findCommandedArticles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CommandedArticles.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommandedArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CommandedArticles> rt = cq.from(CommandedArticles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

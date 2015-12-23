/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Controler.exceptions.IllegalOrphanException;
import Controler.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Provider;
import Entity.CommandedArticles;
import java.util.ArrayList;
import java.util.List;
import Entity.WarehouseArticles;
import Entity.AisleArticles;
import Entity.Article;
import Entity.TransactionArticles;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class ArticleJpaController implements Serializable {

    public ArticleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Article article) {
        if (article.getCommandedArticlesList() == null) {
            article.setCommandedArticlesList(new ArrayList<CommandedArticles>());
        }
        if (article.getWarehouseArticlesList() == null) {
            article.setWarehouseArticlesList(new ArrayList<WarehouseArticles>());
        }
        if (article.getAisleArticlesList() == null) {
            article.setAisleArticlesList(new ArrayList<AisleArticles>());
        }
        if (article.getTransactionArticlesList() == null) {
            article.setTransactionArticlesList(new ArrayList<TransactionArticles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provider providerId = article.getProviderId();
            if (providerId != null) {
                providerId = em.getReference(providerId.getClass(), providerId.getId());
                article.setProviderId(providerId);
            }
            List<CommandedArticles> attachedCommandedArticlesList = new ArrayList<CommandedArticles>();
            for (CommandedArticles commandedArticlesListCommandedArticlesToAttach : article.getCommandedArticlesList()) {
                commandedArticlesListCommandedArticlesToAttach = em.getReference(commandedArticlesListCommandedArticlesToAttach.getClass(), commandedArticlesListCommandedArticlesToAttach.getId());
                attachedCommandedArticlesList.add(commandedArticlesListCommandedArticlesToAttach);
            }
            article.setCommandedArticlesList(attachedCommandedArticlesList);
            List<WarehouseArticles> attachedWarehouseArticlesList = new ArrayList<WarehouseArticles>();
            for (WarehouseArticles warehouseArticlesListWarehouseArticlesToAttach : article.getWarehouseArticlesList()) {
                warehouseArticlesListWarehouseArticlesToAttach = em.getReference(warehouseArticlesListWarehouseArticlesToAttach.getClass(), warehouseArticlesListWarehouseArticlesToAttach.getId());
                attachedWarehouseArticlesList.add(warehouseArticlesListWarehouseArticlesToAttach);
            }
            article.setWarehouseArticlesList(attachedWarehouseArticlesList);
            List<AisleArticles> attachedAisleArticlesList = new ArrayList<AisleArticles>();
            for (AisleArticles aisleArticlesListAisleArticlesToAttach : article.getAisleArticlesList()) {
                aisleArticlesListAisleArticlesToAttach = em.getReference(aisleArticlesListAisleArticlesToAttach.getClass(), aisleArticlesListAisleArticlesToAttach.getId());
                attachedAisleArticlesList.add(aisleArticlesListAisleArticlesToAttach);
            }
            article.setAisleArticlesList(attachedAisleArticlesList);
            List<TransactionArticles> attachedTransactionArticlesList = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesListTransactionArticlesToAttach : article.getTransactionArticlesList()) {
                transactionArticlesListTransactionArticlesToAttach = em.getReference(transactionArticlesListTransactionArticlesToAttach.getClass(), transactionArticlesListTransactionArticlesToAttach.getId());
                attachedTransactionArticlesList.add(transactionArticlesListTransactionArticlesToAttach);
            }
            article.setTransactionArticlesList(attachedTransactionArticlesList);
            em.persist(article);
            if (providerId != null) {
                providerId.getArticleList().add(article);
                providerId = em.merge(providerId);
            }
            for (CommandedArticles commandedArticlesListCommandedArticles : article.getCommandedArticlesList()) {
                Article oldArticleIdOfCommandedArticlesListCommandedArticles = commandedArticlesListCommandedArticles.getArticleId();
                commandedArticlesListCommandedArticles.setArticleId(article);
                commandedArticlesListCommandedArticles = em.merge(commandedArticlesListCommandedArticles);
                if (oldArticleIdOfCommandedArticlesListCommandedArticles != null) {
                    oldArticleIdOfCommandedArticlesListCommandedArticles.getCommandedArticlesList().remove(commandedArticlesListCommandedArticles);
                    oldArticleIdOfCommandedArticlesListCommandedArticles = em.merge(oldArticleIdOfCommandedArticlesListCommandedArticles);
                }
            }
            for (WarehouseArticles warehouseArticlesListWarehouseArticles : article.getWarehouseArticlesList()) {
                Article oldArticleIdOfWarehouseArticlesListWarehouseArticles = warehouseArticlesListWarehouseArticles.getArticleId();
                warehouseArticlesListWarehouseArticles.setArticleId(article);
                warehouseArticlesListWarehouseArticles = em.merge(warehouseArticlesListWarehouseArticles);
                if (oldArticleIdOfWarehouseArticlesListWarehouseArticles != null) {
                    oldArticleIdOfWarehouseArticlesListWarehouseArticles.getWarehouseArticlesList().remove(warehouseArticlesListWarehouseArticles);
                    oldArticleIdOfWarehouseArticlesListWarehouseArticles = em.merge(oldArticleIdOfWarehouseArticlesListWarehouseArticles);
                }
            }
            for (AisleArticles aisleArticlesListAisleArticles : article.getAisleArticlesList()) {
                Article oldArticleIdOfAisleArticlesListAisleArticles = aisleArticlesListAisleArticles.getArticleId();
                aisleArticlesListAisleArticles.setArticleId(article);
                aisleArticlesListAisleArticles = em.merge(aisleArticlesListAisleArticles);
                if (oldArticleIdOfAisleArticlesListAisleArticles != null) {
                    oldArticleIdOfAisleArticlesListAisleArticles.getAisleArticlesList().remove(aisleArticlesListAisleArticles);
                    oldArticleIdOfAisleArticlesListAisleArticles = em.merge(oldArticleIdOfAisleArticlesListAisleArticles);
                }
            }
            for (TransactionArticles transactionArticlesListTransactionArticles : article.getTransactionArticlesList()) {
                Article oldArticleIdOfTransactionArticlesListTransactionArticles = transactionArticlesListTransactionArticles.getArticleId();
                transactionArticlesListTransactionArticles.setArticleId(article);
                transactionArticlesListTransactionArticles = em.merge(transactionArticlesListTransactionArticles);
                if (oldArticleIdOfTransactionArticlesListTransactionArticles != null) {
                    oldArticleIdOfTransactionArticlesListTransactionArticles.getTransactionArticlesList().remove(transactionArticlesListTransactionArticles);
                    oldArticleIdOfTransactionArticlesListTransactionArticles = em.merge(oldArticleIdOfTransactionArticlesListTransactionArticles);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Article article) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article persistentArticle = em.find(Article.class, article.getId());
            Provider providerIdOld = persistentArticle.getProviderId();
            Provider providerIdNew = article.getProviderId();
            List<CommandedArticles> commandedArticlesListOld = persistentArticle.getCommandedArticlesList();
            List<CommandedArticles> commandedArticlesListNew = article.getCommandedArticlesList();
            List<WarehouseArticles> warehouseArticlesListOld = persistentArticle.getWarehouseArticlesList();
            List<WarehouseArticles> warehouseArticlesListNew = article.getWarehouseArticlesList();
            List<AisleArticles> aisleArticlesListOld = persistentArticle.getAisleArticlesList();
            List<AisleArticles> aisleArticlesListNew = article.getAisleArticlesList();
            List<TransactionArticles> transactionArticlesListOld = persistentArticle.getTransactionArticlesList();
            List<TransactionArticles> transactionArticlesListNew = article.getTransactionArticlesList();
            List<String> illegalOrphanMessages = null;
            for (CommandedArticles commandedArticlesListOldCommandedArticles : commandedArticlesListOld) {
                if (!commandedArticlesListNew.contains(commandedArticlesListOldCommandedArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CommandedArticles " + commandedArticlesListOldCommandedArticles + " since its articleId field is not nullable.");
                }
            }
            for (WarehouseArticles warehouseArticlesListOldWarehouseArticles : warehouseArticlesListOld) {
                if (!warehouseArticlesListNew.contains(warehouseArticlesListOldWarehouseArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain WarehouseArticles " + warehouseArticlesListOldWarehouseArticles + " since its articleId field is not nullable.");
                }
            }
            for (AisleArticles aisleArticlesListOldAisleArticles : aisleArticlesListOld) {
                if (!aisleArticlesListNew.contains(aisleArticlesListOldAisleArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AisleArticles " + aisleArticlesListOldAisleArticles + " since its articleId field is not nullable.");
                }
            }
            for (TransactionArticles transactionArticlesListOldTransactionArticles : transactionArticlesListOld) {
                if (!transactionArticlesListNew.contains(transactionArticlesListOldTransactionArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TransactionArticles " + transactionArticlesListOldTransactionArticles + " since its articleId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (providerIdNew != null) {
                providerIdNew = em.getReference(providerIdNew.getClass(), providerIdNew.getId());
                article.setProviderId(providerIdNew);
            }
            List<CommandedArticles> attachedCommandedArticlesListNew = new ArrayList<CommandedArticles>();
            for (CommandedArticles commandedArticlesListNewCommandedArticlesToAttach : commandedArticlesListNew) {
                commandedArticlesListNewCommandedArticlesToAttach = em.getReference(commandedArticlesListNewCommandedArticlesToAttach.getClass(), commandedArticlesListNewCommandedArticlesToAttach.getId());
                attachedCommandedArticlesListNew.add(commandedArticlesListNewCommandedArticlesToAttach);
            }
            commandedArticlesListNew = attachedCommandedArticlesListNew;
            article.setCommandedArticlesList(commandedArticlesListNew);
            List<WarehouseArticles> attachedWarehouseArticlesListNew = new ArrayList<WarehouseArticles>();
            for (WarehouseArticles warehouseArticlesListNewWarehouseArticlesToAttach : warehouseArticlesListNew) {
                warehouseArticlesListNewWarehouseArticlesToAttach = em.getReference(warehouseArticlesListNewWarehouseArticlesToAttach.getClass(), warehouseArticlesListNewWarehouseArticlesToAttach.getId());
                attachedWarehouseArticlesListNew.add(warehouseArticlesListNewWarehouseArticlesToAttach);
            }
            warehouseArticlesListNew = attachedWarehouseArticlesListNew;
            article.setWarehouseArticlesList(warehouseArticlesListNew);
            List<AisleArticles> attachedAisleArticlesListNew = new ArrayList<AisleArticles>();
            for (AisleArticles aisleArticlesListNewAisleArticlesToAttach : aisleArticlesListNew) {
                aisleArticlesListNewAisleArticlesToAttach = em.getReference(aisleArticlesListNewAisleArticlesToAttach.getClass(), aisleArticlesListNewAisleArticlesToAttach.getId());
                attachedAisleArticlesListNew.add(aisleArticlesListNewAisleArticlesToAttach);
            }
            aisleArticlesListNew = attachedAisleArticlesListNew;
            article.setAisleArticlesList(aisleArticlesListNew);
            List<TransactionArticles> attachedTransactionArticlesListNew = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesListNewTransactionArticlesToAttach : transactionArticlesListNew) {
                transactionArticlesListNewTransactionArticlesToAttach = em.getReference(transactionArticlesListNewTransactionArticlesToAttach.getClass(), transactionArticlesListNewTransactionArticlesToAttach.getId());
                attachedTransactionArticlesListNew.add(transactionArticlesListNewTransactionArticlesToAttach);
            }
            transactionArticlesListNew = attachedTransactionArticlesListNew;
            article.setTransactionArticlesList(transactionArticlesListNew);
            article = em.merge(article);
            if (providerIdOld != null && !providerIdOld.equals(providerIdNew)) {
                providerIdOld.getArticleList().remove(article);
                providerIdOld = em.merge(providerIdOld);
            }
            if (providerIdNew != null && !providerIdNew.equals(providerIdOld)) {
                providerIdNew.getArticleList().add(article);
                providerIdNew = em.merge(providerIdNew);
            }
            for (CommandedArticles commandedArticlesListNewCommandedArticles : commandedArticlesListNew) {
                if (!commandedArticlesListOld.contains(commandedArticlesListNewCommandedArticles)) {
                    Article oldArticleIdOfCommandedArticlesListNewCommandedArticles = commandedArticlesListNewCommandedArticles.getArticleId();
                    commandedArticlesListNewCommandedArticles.setArticleId(article);
                    commandedArticlesListNewCommandedArticles = em.merge(commandedArticlesListNewCommandedArticles);
                    if (oldArticleIdOfCommandedArticlesListNewCommandedArticles != null && !oldArticleIdOfCommandedArticlesListNewCommandedArticles.equals(article)) {
                        oldArticleIdOfCommandedArticlesListNewCommandedArticles.getCommandedArticlesList().remove(commandedArticlesListNewCommandedArticles);
                        oldArticleIdOfCommandedArticlesListNewCommandedArticles = em.merge(oldArticleIdOfCommandedArticlesListNewCommandedArticles);
                    }
                }
            }
            for (WarehouseArticles warehouseArticlesListNewWarehouseArticles : warehouseArticlesListNew) {
                if (!warehouseArticlesListOld.contains(warehouseArticlesListNewWarehouseArticles)) {
                    Article oldArticleIdOfWarehouseArticlesListNewWarehouseArticles = warehouseArticlesListNewWarehouseArticles.getArticleId();
                    warehouseArticlesListNewWarehouseArticles.setArticleId(article);
                    warehouseArticlesListNewWarehouseArticles = em.merge(warehouseArticlesListNewWarehouseArticles);
                    if (oldArticleIdOfWarehouseArticlesListNewWarehouseArticles != null && !oldArticleIdOfWarehouseArticlesListNewWarehouseArticles.equals(article)) {
                        oldArticleIdOfWarehouseArticlesListNewWarehouseArticles.getWarehouseArticlesList().remove(warehouseArticlesListNewWarehouseArticles);
                        oldArticleIdOfWarehouseArticlesListNewWarehouseArticles = em.merge(oldArticleIdOfWarehouseArticlesListNewWarehouseArticles);
                    }
                }
            }
            for (AisleArticles aisleArticlesListNewAisleArticles : aisleArticlesListNew) {
                if (!aisleArticlesListOld.contains(aisleArticlesListNewAisleArticles)) {
                    Article oldArticleIdOfAisleArticlesListNewAisleArticles = aisleArticlesListNewAisleArticles.getArticleId();
                    aisleArticlesListNewAisleArticles.setArticleId(article);
                    aisleArticlesListNewAisleArticles = em.merge(aisleArticlesListNewAisleArticles);
                    if (oldArticleIdOfAisleArticlesListNewAisleArticles != null && !oldArticleIdOfAisleArticlesListNewAisleArticles.equals(article)) {
                        oldArticleIdOfAisleArticlesListNewAisleArticles.getAisleArticlesList().remove(aisleArticlesListNewAisleArticles);
                        oldArticleIdOfAisleArticlesListNewAisleArticles = em.merge(oldArticleIdOfAisleArticlesListNewAisleArticles);
                    }
                }
            }
            for (TransactionArticles transactionArticlesListNewTransactionArticles : transactionArticlesListNew) {
                if (!transactionArticlesListOld.contains(transactionArticlesListNewTransactionArticles)) {
                    Article oldArticleIdOfTransactionArticlesListNewTransactionArticles = transactionArticlesListNewTransactionArticles.getArticleId();
                    transactionArticlesListNewTransactionArticles.setArticleId(article);
                    transactionArticlesListNewTransactionArticles = em.merge(transactionArticlesListNewTransactionArticles);
                    if (oldArticleIdOfTransactionArticlesListNewTransactionArticles != null && !oldArticleIdOfTransactionArticlesListNewTransactionArticles.equals(article)) {
                        oldArticleIdOfTransactionArticlesListNewTransactionArticles.getTransactionArticlesList().remove(transactionArticlesListNewTransactionArticles);
                        oldArticleIdOfTransactionArticlesListNewTransactionArticles = em.merge(oldArticleIdOfTransactionArticlesListNewTransactionArticles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = article.getId();
                if (findArticle(id) == null) {
                    throw new NonexistentEntityException("The article with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article article;
            try {
                article = em.getReference(Article.class, id);
                article.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The article with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CommandedArticles> commandedArticlesListOrphanCheck = article.getCommandedArticlesList();
            for (CommandedArticles commandedArticlesListOrphanCheckCommandedArticles : commandedArticlesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the CommandedArticles " + commandedArticlesListOrphanCheckCommandedArticles + " in its commandedArticlesList field has a non-nullable articleId field.");
            }
            List<WarehouseArticles> warehouseArticlesListOrphanCheck = article.getWarehouseArticlesList();
            for (WarehouseArticles warehouseArticlesListOrphanCheckWarehouseArticles : warehouseArticlesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the WarehouseArticles " + warehouseArticlesListOrphanCheckWarehouseArticles + " in its warehouseArticlesList field has a non-nullable articleId field.");
            }
            List<AisleArticles> aisleArticlesListOrphanCheck = article.getAisleArticlesList();
            for (AisleArticles aisleArticlesListOrphanCheckAisleArticles : aisleArticlesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the AisleArticles " + aisleArticlesListOrphanCheckAisleArticles + " in its aisleArticlesList field has a non-nullable articleId field.");
            }
            List<TransactionArticles> transactionArticlesListOrphanCheck = article.getTransactionArticlesList();
            for (TransactionArticles transactionArticlesListOrphanCheckTransactionArticles : transactionArticlesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the TransactionArticles " + transactionArticlesListOrphanCheckTransactionArticles + " in its transactionArticlesList field has a non-nullable articleId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Provider providerId = article.getProviderId();
            if (providerId != null) {
                providerId.getArticleList().remove(article);
                providerId = em.merge(providerId);
            }
            em.remove(article);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Article> findArticleEntities() {
        return findArticleEntities(true, -1, -1);
    }

    public List<Article> findArticleEntities(int maxResults, int firstResult) {
        return findArticleEntities(false, maxResults, firstResult);
    }

    private List<Article> findArticleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Article.class));
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

    public Article findArticle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Article.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Article> rt = cq.from(Article.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

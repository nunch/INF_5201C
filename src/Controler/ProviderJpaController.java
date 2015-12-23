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
import Entity.Article;
import java.util.ArrayList;
import java.util.List;
import Entity.Command;
import Entity.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class ProviderJpaController implements Serializable {

    public ProviderJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Provider provider) {
        if (provider.getArticleList() == null) {
            provider.setArticleList(new ArrayList<Article>());
        }
        if (provider.getCommandList() == null) {
            provider.setCommandList(new ArrayList<Command>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Article> attachedArticleList = new ArrayList<Article>();
            for (Article articleListArticleToAttach : provider.getArticleList()) {
                articleListArticleToAttach = em.getReference(articleListArticleToAttach.getClass(), articleListArticleToAttach.getId());
                attachedArticleList.add(articleListArticleToAttach);
            }
            provider.setArticleList(attachedArticleList);
            List<Command> attachedCommandList = new ArrayList<Command>();
            for (Command commandListCommandToAttach : provider.getCommandList()) {
                commandListCommandToAttach = em.getReference(commandListCommandToAttach.getClass(), commandListCommandToAttach.getId());
                attachedCommandList.add(commandListCommandToAttach);
            }
            provider.setCommandList(attachedCommandList);
            em.persist(provider);
            for (Article articleListArticle : provider.getArticleList()) {
                Provider oldProviderIdOfArticleListArticle = articleListArticle.getProviderId();
                articleListArticle.setProviderId(provider);
                articleListArticle = em.merge(articleListArticle);
                if (oldProviderIdOfArticleListArticle != null) {
                    oldProviderIdOfArticleListArticle.getArticleList().remove(articleListArticle);
                    oldProviderIdOfArticleListArticle = em.merge(oldProviderIdOfArticleListArticle);
                }
            }
            for (Command commandListCommand : provider.getCommandList()) {
                Provider oldProviderIdOfCommandListCommand = commandListCommand.getProviderId();
                commandListCommand.setProviderId(provider);
                commandListCommand = em.merge(commandListCommand);
                if (oldProviderIdOfCommandListCommand != null) {
                    oldProviderIdOfCommandListCommand.getCommandList().remove(commandListCommand);
                    oldProviderIdOfCommandListCommand = em.merge(oldProviderIdOfCommandListCommand);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Provider provider) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provider persistentProvider = em.find(Provider.class, provider.getId());
            List<Article> articleListOld = persistentProvider.getArticleList();
            List<Article> articleListNew = provider.getArticleList();
            List<Command> commandListOld = persistentProvider.getCommandList();
            List<Command> commandListNew = provider.getCommandList();
            List<String> illegalOrphanMessages = null;
            for (Article articleListOldArticle : articleListOld) {
                if (!articleListNew.contains(articleListOldArticle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Article " + articleListOldArticle + " since its providerId field is not nullable.");
                }
            }
            for (Command commandListOldCommand : commandListOld) {
                if (!commandListNew.contains(commandListOldCommand)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Command " + commandListOldCommand + " since its providerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Article> attachedArticleListNew = new ArrayList<Article>();
            for (Article articleListNewArticleToAttach : articleListNew) {
                articleListNewArticleToAttach = em.getReference(articleListNewArticleToAttach.getClass(), articleListNewArticleToAttach.getId());
                attachedArticleListNew.add(articleListNewArticleToAttach);
            }
            articleListNew = attachedArticleListNew;
            provider.setArticleList(articleListNew);
            List<Command> attachedCommandListNew = new ArrayList<Command>();
            for (Command commandListNewCommandToAttach : commandListNew) {
                commandListNewCommandToAttach = em.getReference(commandListNewCommandToAttach.getClass(), commandListNewCommandToAttach.getId());
                attachedCommandListNew.add(commandListNewCommandToAttach);
            }
            commandListNew = attachedCommandListNew;
            provider.setCommandList(commandListNew);
            provider = em.merge(provider);
            for (Article articleListNewArticle : articleListNew) {
                if (!articleListOld.contains(articleListNewArticle)) {
                    Provider oldProviderIdOfArticleListNewArticle = articleListNewArticle.getProviderId();
                    articleListNewArticle.setProviderId(provider);
                    articleListNewArticle = em.merge(articleListNewArticle);
                    if (oldProviderIdOfArticleListNewArticle != null && !oldProviderIdOfArticleListNewArticle.equals(provider)) {
                        oldProviderIdOfArticleListNewArticle.getArticleList().remove(articleListNewArticle);
                        oldProviderIdOfArticleListNewArticle = em.merge(oldProviderIdOfArticleListNewArticle);
                    }
                }
            }
            for (Command commandListNewCommand : commandListNew) {
                if (!commandListOld.contains(commandListNewCommand)) {
                    Provider oldProviderIdOfCommandListNewCommand = commandListNewCommand.getProviderId();
                    commandListNewCommand.setProviderId(provider);
                    commandListNewCommand = em.merge(commandListNewCommand);
                    if (oldProviderIdOfCommandListNewCommand != null && !oldProviderIdOfCommandListNewCommand.equals(provider)) {
                        oldProviderIdOfCommandListNewCommand.getCommandList().remove(commandListNewCommand);
                        oldProviderIdOfCommandListNewCommand = em.merge(oldProviderIdOfCommandListNewCommand);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = provider.getId();
                if (findProvider(id) == null) {
                    throw new NonexistentEntityException("The provider with id " + id + " no longer exists.");
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
            Provider provider;
            try {
                provider = em.getReference(Provider.class, id);
                provider.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The provider with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Article> articleListOrphanCheck = provider.getArticleList();
            for (Article articleListOrphanCheckArticle : articleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Provider (" + provider + ") cannot be destroyed since the Article " + articleListOrphanCheckArticle + " in its articleList field has a non-nullable providerId field.");
            }
            List<Command> commandListOrphanCheck = provider.getCommandList();
            for (Command commandListOrphanCheckCommand : commandListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Provider (" + provider + ") cannot be destroyed since the Command " + commandListOrphanCheckCommand + " in its commandList field has a non-nullable providerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(provider);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Provider> findProviderEntities() {
        return findProviderEntities(true, -1, -1);
    }

    public List<Provider> findProviderEntities(int maxResults, int firstResult) {
        return findProviderEntities(false, maxResults, firstResult);
    }

    private List<Provider> findProviderEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Provider.class));
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

    public Provider findProvider(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Provider.class, id);
        } finally {
            em.close();
        }
    }

    public int getProviderCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Provider> rt = cq.from(Provider.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

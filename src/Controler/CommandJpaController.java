/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Controler.exceptions.IllegalOrphanException;
import Controler.exceptions.NonexistentEntityException;
import Entity.Command;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Provider;
import Entity.CommandedArticles;
import java.util.ArrayList;
import java.util.List;
import Entity.RetailerCommands;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class CommandJpaController implements Serializable {

    public CommandJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Command command) {
        if (command.getCommandedArticlesList() == null) {
            command.setCommandedArticlesList(new ArrayList<CommandedArticles>());
        }
        if (command.getRetailerCommandsList() == null) {
            command.setRetailerCommandsList(new ArrayList<RetailerCommands>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provider providerId = command.getProviderId();
            if (providerId != null) {
                providerId = em.getReference(providerId.getClass(), providerId.getId());
                command.setProviderId(providerId);
            }
            List<CommandedArticles> attachedCommandedArticlesList = new ArrayList<CommandedArticles>();
            for (CommandedArticles commandedArticlesListCommandedArticlesToAttach : command.getCommandedArticlesList()) {
                commandedArticlesListCommandedArticlesToAttach = em.getReference(commandedArticlesListCommandedArticlesToAttach.getClass(), commandedArticlesListCommandedArticlesToAttach.getId());
                attachedCommandedArticlesList.add(commandedArticlesListCommandedArticlesToAttach);
            }
            command.setCommandedArticlesList(attachedCommandedArticlesList);
            List<RetailerCommands> attachedRetailerCommandsList = new ArrayList<RetailerCommands>();
            for (RetailerCommands retailerCommandsListRetailerCommandsToAttach : command.getRetailerCommandsList()) {
                retailerCommandsListRetailerCommandsToAttach = em.getReference(retailerCommandsListRetailerCommandsToAttach.getClass(), retailerCommandsListRetailerCommandsToAttach.getId());
                attachedRetailerCommandsList.add(retailerCommandsListRetailerCommandsToAttach);
            }
            command.setRetailerCommandsList(attachedRetailerCommandsList);
            em.persist(command);
            if (providerId != null) {
                providerId.getCommandList().add(command);
                providerId = em.merge(providerId);
            }
            for (CommandedArticles commandedArticlesListCommandedArticles : command.getCommandedArticlesList()) {
                Command oldCommandIdOfCommandedArticlesListCommandedArticles = commandedArticlesListCommandedArticles.getCommandId();
                commandedArticlesListCommandedArticles.setCommandId(command);
                commandedArticlesListCommandedArticles = em.merge(commandedArticlesListCommandedArticles);
                if (oldCommandIdOfCommandedArticlesListCommandedArticles != null) {
                    oldCommandIdOfCommandedArticlesListCommandedArticles.getCommandedArticlesList().remove(commandedArticlesListCommandedArticles);
                    oldCommandIdOfCommandedArticlesListCommandedArticles = em.merge(oldCommandIdOfCommandedArticlesListCommandedArticles);
                }
            }
            for (RetailerCommands retailerCommandsListRetailerCommands : command.getRetailerCommandsList()) {
                Command oldCommandIdOfRetailerCommandsListRetailerCommands = retailerCommandsListRetailerCommands.getCommandId();
                retailerCommandsListRetailerCommands.setCommandId(command);
                retailerCommandsListRetailerCommands = em.merge(retailerCommandsListRetailerCommands);
                if (oldCommandIdOfRetailerCommandsListRetailerCommands != null) {
                    oldCommandIdOfRetailerCommandsListRetailerCommands.getRetailerCommandsList().remove(retailerCommandsListRetailerCommands);
                    oldCommandIdOfRetailerCommandsListRetailerCommands = em.merge(oldCommandIdOfRetailerCommandsListRetailerCommands);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Command command) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Command persistentCommand = em.find(Command.class, command.getId());
            Provider providerIdOld = persistentCommand.getProviderId();
            Provider providerIdNew = command.getProviderId();
            List<CommandedArticles> commandedArticlesListOld = persistentCommand.getCommandedArticlesList();
            List<CommandedArticles> commandedArticlesListNew = command.getCommandedArticlesList();
            List<RetailerCommands> retailerCommandsListOld = persistentCommand.getRetailerCommandsList();
            List<RetailerCommands> retailerCommandsListNew = command.getRetailerCommandsList();
            List<String> illegalOrphanMessages = null;
            for (CommandedArticles commandedArticlesListOldCommandedArticles : commandedArticlesListOld) {
                if (!commandedArticlesListNew.contains(commandedArticlesListOldCommandedArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CommandedArticles " + commandedArticlesListOldCommandedArticles + " since its commandId field is not nullable.");
                }
            }
            for (RetailerCommands retailerCommandsListOldRetailerCommands : retailerCommandsListOld) {
                if (!retailerCommandsListNew.contains(retailerCommandsListOldRetailerCommands)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RetailerCommands " + retailerCommandsListOldRetailerCommands + " since its commandId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (providerIdNew != null) {
                providerIdNew = em.getReference(providerIdNew.getClass(), providerIdNew.getId());
                command.setProviderId(providerIdNew);
            }
            List<CommandedArticles> attachedCommandedArticlesListNew = new ArrayList<CommandedArticles>();
            for (CommandedArticles commandedArticlesListNewCommandedArticlesToAttach : commandedArticlesListNew) {
                commandedArticlesListNewCommandedArticlesToAttach = em.getReference(commandedArticlesListNewCommandedArticlesToAttach.getClass(), commandedArticlesListNewCommandedArticlesToAttach.getId());
                attachedCommandedArticlesListNew.add(commandedArticlesListNewCommandedArticlesToAttach);
            }
            commandedArticlesListNew = attachedCommandedArticlesListNew;
            command.setCommandedArticlesList(commandedArticlesListNew);
            List<RetailerCommands> attachedRetailerCommandsListNew = new ArrayList<RetailerCommands>();
            for (RetailerCommands retailerCommandsListNewRetailerCommandsToAttach : retailerCommandsListNew) {
                retailerCommandsListNewRetailerCommandsToAttach = em.getReference(retailerCommandsListNewRetailerCommandsToAttach.getClass(), retailerCommandsListNewRetailerCommandsToAttach.getId());
                attachedRetailerCommandsListNew.add(retailerCommandsListNewRetailerCommandsToAttach);
            }
            retailerCommandsListNew = attachedRetailerCommandsListNew;
            command.setRetailerCommandsList(retailerCommandsListNew);
            command = em.merge(command);
            if (providerIdOld != null && !providerIdOld.equals(providerIdNew)) {
                providerIdOld.getCommandList().remove(command);
                providerIdOld = em.merge(providerIdOld);
            }
            if (providerIdNew != null && !providerIdNew.equals(providerIdOld)) {
                providerIdNew.getCommandList().add(command);
                providerIdNew = em.merge(providerIdNew);
            }
            for (CommandedArticles commandedArticlesListNewCommandedArticles : commandedArticlesListNew) {
                if (!commandedArticlesListOld.contains(commandedArticlesListNewCommandedArticles)) {
                    Command oldCommandIdOfCommandedArticlesListNewCommandedArticles = commandedArticlesListNewCommandedArticles.getCommandId();
                    commandedArticlesListNewCommandedArticles.setCommandId(command);
                    commandedArticlesListNewCommandedArticles = em.merge(commandedArticlesListNewCommandedArticles);
                    if (oldCommandIdOfCommandedArticlesListNewCommandedArticles != null && !oldCommandIdOfCommandedArticlesListNewCommandedArticles.equals(command)) {
                        oldCommandIdOfCommandedArticlesListNewCommandedArticles.getCommandedArticlesList().remove(commandedArticlesListNewCommandedArticles);
                        oldCommandIdOfCommandedArticlesListNewCommandedArticles = em.merge(oldCommandIdOfCommandedArticlesListNewCommandedArticles);
                    }
                }
            }
            for (RetailerCommands retailerCommandsListNewRetailerCommands : retailerCommandsListNew) {
                if (!retailerCommandsListOld.contains(retailerCommandsListNewRetailerCommands)) {
                    Command oldCommandIdOfRetailerCommandsListNewRetailerCommands = retailerCommandsListNewRetailerCommands.getCommandId();
                    retailerCommandsListNewRetailerCommands.setCommandId(command);
                    retailerCommandsListNewRetailerCommands = em.merge(retailerCommandsListNewRetailerCommands);
                    if (oldCommandIdOfRetailerCommandsListNewRetailerCommands != null && !oldCommandIdOfRetailerCommandsListNewRetailerCommands.equals(command)) {
                        oldCommandIdOfRetailerCommandsListNewRetailerCommands.getRetailerCommandsList().remove(retailerCommandsListNewRetailerCommands);
                        oldCommandIdOfRetailerCommandsListNewRetailerCommands = em.merge(oldCommandIdOfRetailerCommandsListNewRetailerCommands);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = command.getId();
                if (findCommand(id) == null) {
                    throw new NonexistentEntityException("The command with id " + id + " no longer exists.");
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
            Command command;
            try {
                command = em.getReference(Command.class, id);
                command.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The command with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CommandedArticles> commandedArticlesListOrphanCheck = command.getCommandedArticlesList();
            for (CommandedArticles commandedArticlesListOrphanCheckCommandedArticles : commandedArticlesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Command (" + command + ") cannot be destroyed since the CommandedArticles " + commandedArticlesListOrphanCheckCommandedArticles + " in its commandedArticlesList field has a non-nullable commandId field.");
            }
            List<RetailerCommands> retailerCommandsListOrphanCheck = command.getRetailerCommandsList();
            for (RetailerCommands retailerCommandsListOrphanCheckRetailerCommands : retailerCommandsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Command (" + command + ") cannot be destroyed since the RetailerCommands " + retailerCommandsListOrphanCheckRetailerCommands + " in its retailerCommandsList field has a non-nullable commandId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Provider providerId = command.getProviderId();
            if (providerId != null) {
                providerId.getCommandList().remove(command);
                providerId = em.merge(providerId);
            }
            em.remove(command);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Command> findCommandEntities() {
        return findCommandEntities(true, -1, -1);
    }

    public List<Command> findCommandEntities(int maxResults, int firstResult) {
        return findCommandEntities(false, maxResults, firstResult);
    }

    private List<Command> findCommandEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Command.class));
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

    public Command findCommand(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Command.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommandCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Command> rt = cq.from(Command.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

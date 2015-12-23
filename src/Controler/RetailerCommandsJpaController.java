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
import Entity.Retailer;
import Entity.Command;
import Entity.RetailerCommands;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class RetailerCommandsJpaController implements Serializable {

    public RetailerCommandsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RetailerCommands retailerCommands) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Retailer retailerId = retailerCommands.getRetailerId();
            if (retailerId != null) {
                retailerId = em.getReference(retailerId.getClass(), retailerId.getId());
                retailerCommands.setRetailerId(retailerId);
            }
            Command commandId = retailerCommands.getCommandId();
            if (commandId != null) {
                commandId = em.getReference(commandId.getClass(), commandId.getId());
                retailerCommands.setCommandId(commandId);
            }
            em.persist(retailerCommands);
            if (retailerId != null) {
                retailerId.getRetailerCommandsList().add(retailerCommands);
                retailerId = em.merge(retailerId);
            }
            if (commandId != null) {
                commandId.getRetailerCommandsList().add(retailerCommands);
                commandId = em.merge(commandId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RetailerCommands retailerCommands) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RetailerCommands persistentRetailerCommands = em.find(RetailerCommands.class, retailerCommands.getId());
            Retailer retailerIdOld = persistentRetailerCommands.getRetailerId();
            Retailer retailerIdNew = retailerCommands.getRetailerId();
            Command commandIdOld = persistentRetailerCommands.getCommandId();
            Command commandIdNew = retailerCommands.getCommandId();
            if (retailerIdNew != null) {
                retailerIdNew = em.getReference(retailerIdNew.getClass(), retailerIdNew.getId());
                retailerCommands.setRetailerId(retailerIdNew);
            }
            if (commandIdNew != null) {
                commandIdNew = em.getReference(commandIdNew.getClass(), commandIdNew.getId());
                retailerCommands.setCommandId(commandIdNew);
            }
            retailerCommands = em.merge(retailerCommands);
            if (retailerIdOld != null && !retailerIdOld.equals(retailerIdNew)) {
                retailerIdOld.getRetailerCommandsList().remove(retailerCommands);
                retailerIdOld = em.merge(retailerIdOld);
            }
            if (retailerIdNew != null && !retailerIdNew.equals(retailerIdOld)) {
                retailerIdNew.getRetailerCommandsList().add(retailerCommands);
                retailerIdNew = em.merge(retailerIdNew);
            }
            if (commandIdOld != null && !commandIdOld.equals(commandIdNew)) {
                commandIdOld.getRetailerCommandsList().remove(retailerCommands);
                commandIdOld = em.merge(commandIdOld);
            }
            if (commandIdNew != null && !commandIdNew.equals(commandIdOld)) {
                commandIdNew.getRetailerCommandsList().add(retailerCommands);
                commandIdNew = em.merge(commandIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = retailerCommands.getId();
                if (findRetailerCommands(id) == null) {
                    throw new NonexistentEntityException("The retailerCommands with id " + id + " no longer exists.");
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
            RetailerCommands retailerCommands;
            try {
                retailerCommands = em.getReference(RetailerCommands.class, id);
                retailerCommands.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The retailerCommands with id " + id + " no longer exists.", enfe);
            }
            Retailer retailerId = retailerCommands.getRetailerId();
            if (retailerId != null) {
                retailerId.getRetailerCommandsList().remove(retailerCommands);
                retailerId = em.merge(retailerId);
            }
            Command commandId = retailerCommands.getCommandId();
            if (commandId != null) {
                commandId.getRetailerCommandsList().remove(retailerCommands);
                commandId = em.merge(commandId);
            }
            em.remove(retailerCommands);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RetailerCommands> findRetailerCommandsEntities() {
        return findRetailerCommandsEntities(true, -1, -1);
    }

    public List<RetailerCommands> findRetailerCommandsEntities(int maxResults, int firstResult) {
        return findRetailerCommandsEntities(false, maxResults, firstResult);
    }

    private List<RetailerCommands> findRetailerCommandsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RetailerCommands.class));
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

    public RetailerCommands findRetailerCommands(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RetailerCommands.class, id);
        } finally {
            em.close();
        }
    }

    public int getRetailerCommandsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RetailerCommands> rt = cq.from(RetailerCommands.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

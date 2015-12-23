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
import Entity.Employee;
import Entity.Retailer;
import Entity.RetailerCommands;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class RetailerJpaController implements Serializable {

    public RetailerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Retailer retailer) {
        if (retailer.getRetailerCommandsList() == null) {
            retailer.setRetailerCommandsList(new ArrayList<RetailerCommands>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employeeId = retailer.getEmployeeId();
            if (employeeId != null) {
                employeeId = em.getReference(employeeId.getClass(), employeeId.getId());
                retailer.setEmployeeId(employeeId);
            }
            List<RetailerCommands> attachedRetailerCommandsList = new ArrayList<RetailerCommands>();
            for (RetailerCommands retailerCommandsListRetailerCommandsToAttach : retailer.getRetailerCommandsList()) {
                retailerCommandsListRetailerCommandsToAttach = em.getReference(retailerCommandsListRetailerCommandsToAttach.getClass(), retailerCommandsListRetailerCommandsToAttach.getId());
                attachedRetailerCommandsList.add(retailerCommandsListRetailerCommandsToAttach);
            }
            retailer.setRetailerCommandsList(attachedRetailerCommandsList);
            em.persist(retailer);
            if (employeeId != null) {
                employeeId.getRetailerList().add(retailer);
                employeeId = em.merge(employeeId);
            }
            for (RetailerCommands retailerCommandsListRetailerCommands : retailer.getRetailerCommandsList()) {
                Retailer oldRetailerIdOfRetailerCommandsListRetailerCommands = retailerCommandsListRetailerCommands.getRetailerId();
                retailerCommandsListRetailerCommands.setRetailerId(retailer);
                retailerCommandsListRetailerCommands = em.merge(retailerCommandsListRetailerCommands);
                if (oldRetailerIdOfRetailerCommandsListRetailerCommands != null) {
                    oldRetailerIdOfRetailerCommandsListRetailerCommands.getRetailerCommandsList().remove(retailerCommandsListRetailerCommands);
                    oldRetailerIdOfRetailerCommandsListRetailerCommands = em.merge(oldRetailerIdOfRetailerCommandsListRetailerCommands);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Retailer retailer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Retailer persistentRetailer = em.find(Retailer.class, retailer.getId());
            Employee employeeIdOld = persistentRetailer.getEmployeeId();
            Employee employeeIdNew = retailer.getEmployeeId();
            List<RetailerCommands> retailerCommandsListOld = persistentRetailer.getRetailerCommandsList();
            List<RetailerCommands> retailerCommandsListNew = retailer.getRetailerCommandsList();
            List<String> illegalOrphanMessages = null;
            for (RetailerCommands retailerCommandsListOldRetailerCommands : retailerCommandsListOld) {
                if (!retailerCommandsListNew.contains(retailerCommandsListOldRetailerCommands)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RetailerCommands " + retailerCommandsListOldRetailerCommands + " since its retailerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (employeeIdNew != null) {
                employeeIdNew = em.getReference(employeeIdNew.getClass(), employeeIdNew.getId());
                retailer.setEmployeeId(employeeIdNew);
            }
            List<RetailerCommands> attachedRetailerCommandsListNew = new ArrayList<RetailerCommands>();
            for (RetailerCommands retailerCommandsListNewRetailerCommandsToAttach : retailerCommandsListNew) {
                retailerCommandsListNewRetailerCommandsToAttach = em.getReference(retailerCommandsListNewRetailerCommandsToAttach.getClass(), retailerCommandsListNewRetailerCommandsToAttach.getId());
                attachedRetailerCommandsListNew.add(retailerCommandsListNewRetailerCommandsToAttach);
            }
            retailerCommandsListNew = attachedRetailerCommandsListNew;
            retailer.setRetailerCommandsList(retailerCommandsListNew);
            retailer = em.merge(retailer);
            if (employeeIdOld != null && !employeeIdOld.equals(employeeIdNew)) {
                employeeIdOld.getRetailerList().remove(retailer);
                employeeIdOld = em.merge(employeeIdOld);
            }
            if (employeeIdNew != null && !employeeIdNew.equals(employeeIdOld)) {
                employeeIdNew.getRetailerList().add(retailer);
                employeeIdNew = em.merge(employeeIdNew);
            }
            for (RetailerCommands retailerCommandsListNewRetailerCommands : retailerCommandsListNew) {
                if (!retailerCommandsListOld.contains(retailerCommandsListNewRetailerCommands)) {
                    Retailer oldRetailerIdOfRetailerCommandsListNewRetailerCommands = retailerCommandsListNewRetailerCommands.getRetailerId();
                    retailerCommandsListNewRetailerCommands.setRetailerId(retailer);
                    retailerCommandsListNewRetailerCommands = em.merge(retailerCommandsListNewRetailerCommands);
                    if (oldRetailerIdOfRetailerCommandsListNewRetailerCommands != null && !oldRetailerIdOfRetailerCommandsListNewRetailerCommands.equals(retailer)) {
                        oldRetailerIdOfRetailerCommandsListNewRetailerCommands.getRetailerCommandsList().remove(retailerCommandsListNewRetailerCommands);
                        oldRetailerIdOfRetailerCommandsListNewRetailerCommands = em.merge(oldRetailerIdOfRetailerCommandsListNewRetailerCommands);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = retailer.getId();
                if (findRetailer(id) == null) {
                    throw new NonexistentEntityException("The retailer with id " + id + " no longer exists.");
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
            Retailer retailer;
            try {
                retailer = em.getReference(Retailer.class, id);
                retailer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The retailer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RetailerCommands> retailerCommandsListOrphanCheck = retailer.getRetailerCommandsList();
            for (RetailerCommands retailerCommandsListOrphanCheckRetailerCommands : retailerCommandsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Retailer (" + retailer + ") cannot be destroyed since the RetailerCommands " + retailerCommandsListOrphanCheckRetailerCommands + " in its retailerCommandsList field has a non-nullable retailerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Employee employeeId = retailer.getEmployeeId();
            if (employeeId != null) {
                employeeId.getRetailerList().remove(retailer);
                employeeId = em.merge(employeeId);
            }
            em.remove(retailer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Retailer> findRetailerEntities() {
        return findRetailerEntities(true, -1, -1);
    }

    public List<Retailer> findRetailerEntities(int maxResults, int firstResult) {
        return findRetailerEntities(false, maxResults, firstResult);
    }

    private List<Retailer> findRetailerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Retailer.class));
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

    public Retailer findRetailer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Retailer.class, id);
        } finally {
            em.close();
        }
    }

    public int getRetailerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Retailer> rt = cq.from(Retailer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

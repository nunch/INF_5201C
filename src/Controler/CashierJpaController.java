/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Controler.exceptions.NonexistentEntityException;
import Entity.Cashier;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Employee;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class CashierJpaController implements Serializable {

    public CashierJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cashier cashier) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employeeId = cashier.getEmployeeId();
            if (employeeId != null) {
                employeeId = em.getReference(employeeId.getClass(), employeeId.getId());
                cashier.setEmployeeId(employeeId);
            }
            em.persist(cashier);
            if (employeeId != null) {
                employeeId.getCashierList().add(cashier);
                employeeId = em.merge(employeeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cashier cashier) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cashier persistentCashier = em.find(Cashier.class, cashier.getId());
            Employee employeeIdOld = persistentCashier.getEmployeeId();
            Employee employeeIdNew = cashier.getEmployeeId();
            if (employeeIdNew != null) {
                employeeIdNew = em.getReference(employeeIdNew.getClass(), employeeIdNew.getId());
                cashier.setEmployeeId(employeeIdNew);
            }
            cashier = em.merge(cashier);
            if (employeeIdOld != null && !employeeIdOld.equals(employeeIdNew)) {
                employeeIdOld.getCashierList().remove(cashier);
                employeeIdOld = em.merge(employeeIdOld);
            }
            if (employeeIdNew != null && !employeeIdNew.equals(employeeIdOld)) {
                employeeIdNew.getCashierList().add(cashier);
                employeeIdNew = em.merge(employeeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cashier.getId();
                if (findCashier(id) == null) {
                    throw new NonexistentEntityException("The cashier with id " + id + " no longer exists.");
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
            Cashier cashier;
            try {
                cashier = em.getReference(Cashier.class, id);
                cashier.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cashier with id " + id + " no longer exists.", enfe);
            }
            Employee employeeId = cashier.getEmployeeId();
            if (employeeId != null) {
                employeeId.getCashierList().remove(cashier);
                employeeId = em.merge(employeeId);
            }
            em.remove(cashier);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cashier> findCashierEntities() {
        return findCashierEntities(true, -1, -1);
    }

    public List<Cashier> findCashierEntities(int maxResults, int firstResult) {
        return findCashierEntities(false, maxResults, firstResult);
    }

    private List<Cashier> findCashierEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cashier.class));
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

    public Cashier findCashier(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cashier.class, id);
        } finally {
            em.close();
        }
    }

    public int getCashierCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cashier> rt = cq.from(Cashier.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

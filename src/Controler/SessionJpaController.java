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
import Entity.CashRegister;
import Entity.Session;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class SessionJpaController implements Serializable {

    public SessionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Session session) {
        if (session.getCashRegisterList() == null) {
            session.setCashRegisterList(new ArrayList<CashRegister>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CashRegister> attachedCashRegisterList = new ArrayList<CashRegister>();
            for (CashRegister cashRegisterListCashRegisterToAttach : session.getCashRegisterList()) {
                cashRegisterListCashRegisterToAttach = em.getReference(cashRegisterListCashRegisterToAttach.getClass(), cashRegisterListCashRegisterToAttach.getId());
                attachedCashRegisterList.add(cashRegisterListCashRegisterToAttach);
            }
            session.setCashRegisterList(attachedCashRegisterList);
            em.persist(session);
            for (CashRegister cashRegisterListCashRegister : session.getCashRegisterList()) {
                Session oldSessionIdOfCashRegisterListCashRegister = cashRegisterListCashRegister.getSessionId();
                cashRegisterListCashRegister.setSessionId(session);
                cashRegisterListCashRegister = em.merge(cashRegisterListCashRegister);
                if (oldSessionIdOfCashRegisterListCashRegister != null) {
                    oldSessionIdOfCashRegisterListCashRegister.getCashRegisterList().remove(cashRegisterListCashRegister);
                    oldSessionIdOfCashRegisterListCashRegister = em.merge(oldSessionIdOfCashRegisterListCashRegister);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Session session) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Session persistentSession = em.find(Session.class, session.getId());
            List<CashRegister> cashRegisterListOld = persistentSession.getCashRegisterList();
            List<CashRegister> cashRegisterListNew = session.getCashRegisterList();
            List<CashRegister> attachedCashRegisterListNew = new ArrayList<CashRegister>();
            for (CashRegister cashRegisterListNewCashRegisterToAttach : cashRegisterListNew) {
                cashRegisterListNewCashRegisterToAttach = em.getReference(cashRegisterListNewCashRegisterToAttach.getClass(), cashRegisterListNewCashRegisterToAttach.getId());
                attachedCashRegisterListNew.add(cashRegisterListNewCashRegisterToAttach);
            }
            cashRegisterListNew = attachedCashRegisterListNew;
            session.setCashRegisterList(cashRegisterListNew);
            session = em.merge(session);
            for (CashRegister cashRegisterListOldCashRegister : cashRegisterListOld) {
                if (!cashRegisterListNew.contains(cashRegisterListOldCashRegister)) {
                    cashRegisterListOldCashRegister.setSessionId(null);
                    cashRegisterListOldCashRegister = em.merge(cashRegisterListOldCashRegister);
                }
            }
            for (CashRegister cashRegisterListNewCashRegister : cashRegisterListNew) {
                if (!cashRegisterListOld.contains(cashRegisterListNewCashRegister)) {
                    Session oldSessionIdOfCashRegisterListNewCashRegister = cashRegisterListNewCashRegister.getSessionId();
                    cashRegisterListNewCashRegister.setSessionId(session);
                    cashRegisterListNewCashRegister = em.merge(cashRegisterListNewCashRegister);
                    if (oldSessionIdOfCashRegisterListNewCashRegister != null && !oldSessionIdOfCashRegisterListNewCashRegister.equals(session)) {
                        oldSessionIdOfCashRegisterListNewCashRegister.getCashRegisterList().remove(cashRegisterListNewCashRegister);
                        oldSessionIdOfCashRegisterListNewCashRegister = em.merge(oldSessionIdOfCashRegisterListNewCashRegister);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = session.getId();
                if (findSession(id) == null) {
                    throw new NonexistentEntityException("The session with id " + id + " no longer exists.");
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
            Session session;
            try {
                session = em.getReference(Session.class, id);
                session.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The session with id " + id + " no longer exists.", enfe);
            }
            List<CashRegister> cashRegisterList = session.getCashRegisterList();
            for (CashRegister cashRegisterListCashRegister : cashRegisterList) {
                cashRegisterListCashRegister.setSessionId(null);
                cashRegisterListCashRegister = em.merge(cashRegisterListCashRegister);
            }
            em.remove(session);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Session> findSessionEntities() {
        return findSessionEntities(true, -1, -1);
    }

    public List<Session> findSessionEntities(int maxResults, int firstResult) {
        return findSessionEntities(false, maxResults, firstResult);
    }

    private List<Session> findSessionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Session.class));
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

    public Session findSession(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Session.class, id);
        } finally {
            em.close();
        }
    }

    public int getSessionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Session> rt = cq.from(Session.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

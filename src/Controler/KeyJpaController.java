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
import Entity.CashRegister;
import Entity.Key;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class KeyJpaController implements Serializable {

    public KeyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Key key) throws IllegalOrphanException {
        if (key.getCashRegisterList() == null) {
            key.setCashRegisterList(new ArrayList<CashRegister>());
        }
        List<String> illegalOrphanMessages = null;
        CashRegister cashRegisteridOrphanCheck = key.getCashRegisterid();
        if (cashRegisteridOrphanCheck != null) {
            Key oldKeyIdOfCashRegisterid = cashRegisteridOrphanCheck.getKeyId();
            if (oldKeyIdOfCashRegisterid != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The CashRegister " + cashRegisteridOrphanCheck + " already has an item of type Key whose cashRegisterid column cannot be null. Please make another selection for the cashRegisterid field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CashRegister cashRegisterid = key.getCashRegisterid();
            if (cashRegisterid != null) {
                cashRegisterid = em.getReference(cashRegisterid.getClass(), cashRegisterid.getId());
                key.setCashRegisterid(cashRegisterid);
            }
            List<CashRegister> attachedCashRegisterList = new ArrayList<CashRegister>();
            for (CashRegister cashRegisterListCashRegisterToAttach : key.getCashRegisterList()) {
                cashRegisterListCashRegisterToAttach = em.getReference(cashRegisterListCashRegisterToAttach.getClass(), cashRegisterListCashRegisterToAttach.getId());
                attachedCashRegisterList.add(cashRegisterListCashRegisterToAttach);
            }
            key.setCashRegisterList(attachedCashRegisterList);
            em.persist(key);
            if (cashRegisterid != null) {
                cashRegisterid.setKeyId(key);
                cashRegisterid = em.merge(cashRegisterid);
            }
            for (CashRegister cashRegisterListCashRegister : key.getCashRegisterList()) {
                Key oldKeyIdOfCashRegisterListCashRegister = cashRegisterListCashRegister.getKeyId();
                cashRegisterListCashRegister.setKeyId(key);
                cashRegisterListCashRegister = em.merge(cashRegisterListCashRegister);
                if (oldKeyIdOfCashRegisterListCashRegister != null) {
                    oldKeyIdOfCashRegisterListCashRegister.getCashRegisterList().remove(cashRegisterListCashRegister);
                    oldKeyIdOfCashRegisterListCashRegister = em.merge(oldKeyIdOfCashRegisterListCashRegister);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Key key) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Key persistentKey = em.find(Key.class, key.getId());
            CashRegister cashRegisteridOld = persistentKey.getCashRegisterid();
            CashRegister cashRegisteridNew = key.getCashRegisterid();
            List<CashRegister> cashRegisterListOld = persistentKey.getCashRegisterList();
            List<CashRegister> cashRegisterListNew = key.getCashRegisterList();
            List<String> illegalOrphanMessages = null;
            if (cashRegisteridNew != null && !cashRegisteridNew.equals(cashRegisteridOld)) {
                Key oldKeyIdOfCashRegisterid = cashRegisteridNew.getKeyId();
                if (oldKeyIdOfCashRegisterid != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The CashRegister " + cashRegisteridNew + " already has an item of type Key whose cashRegisterid column cannot be null. Please make another selection for the cashRegisterid field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cashRegisteridNew != null) {
                cashRegisteridNew = em.getReference(cashRegisteridNew.getClass(), cashRegisteridNew.getId());
                key.setCashRegisterid(cashRegisteridNew);
            }
            List<CashRegister> attachedCashRegisterListNew = new ArrayList<CashRegister>();
            for (CashRegister cashRegisterListNewCashRegisterToAttach : cashRegisterListNew) {
                cashRegisterListNewCashRegisterToAttach = em.getReference(cashRegisterListNewCashRegisterToAttach.getClass(), cashRegisterListNewCashRegisterToAttach.getId());
                attachedCashRegisterListNew.add(cashRegisterListNewCashRegisterToAttach);
            }
            cashRegisterListNew = attachedCashRegisterListNew;
            key.setCashRegisterList(cashRegisterListNew);
            key = em.merge(key);
            if (cashRegisteridOld != null && !cashRegisteridOld.equals(cashRegisteridNew)) {
                cashRegisteridOld.setKeyId(null);
                cashRegisteridOld = em.merge(cashRegisteridOld);
            }
            if (cashRegisteridNew != null && !cashRegisteridNew.equals(cashRegisteridOld)) {
                cashRegisteridNew.setKeyId(key);
                cashRegisteridNew = em.merge(cashRegisteridNew);
            }
            for (CashRegister cashRegisterListOldCashRegister : cashRegisterListOld) {
                if (!cashRegisterListNew.contains(cashRegisterListOldCashRegister)) {
                    cashRegisterListOldCashRegister.setKeyId(null);
                    cashRegisterListOldCashRegister = em.merge(cashRegisterListOldCashRegister);
                }
            }
            for (CashRegister cashRegisterListNewCashRegister : cashRegisterListNew) {
                if (!cashRegisterListOld.contains(cashRegisterListNewCashRegister)) {
                    Key oldKeyIdOfCashRegisterListNewCashRegister = cashRegisterListNewCashRegister.getKeyId();
                    cashRegisterListNewCashRegister.setKeyId(key);
                    cashRegisterListNewCashRegister = em.merge(cashRegisterListNewCashRegister);
                    if (oldKeyIdOfCashRegisterListNewCashRegister != null && !oldKeyIdOfCashRegisterListNewCashRegister.equals(key)) {
                        oldKeyIdOfCashRegisterListNewCashRegister.getCashRegisterList().remove(cashRegisterListNewCashRegister);
                        oldKeyIdOfCashRegisterListNewCashRegister = em.merge(oldKeyIdOfCashRegisterListNewCashRegister);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = key.getId();
                if (findKey(id) == null) {
                    throw new NonexistentEntityException("The key with id " + id + " no longer exists.");
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
            Key key;
            try {
                key = em.getReference(Key.class, id);
                key.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The key with id " + id + " no longer exists.", enfe);
            }
            CashRegister cashRegisterid = key.getCashRegisterid();
            if (cashRegisterid != null) {
                cashRegisterid.setKeyId(null);
                cashRegisterid = em.merge(cashRegisterid);
            }
            List<CashRegister> cashRegisterList = key.getCashRegisterList();
            for (CashRegister cashRegisterListCashRegister : cashRegisterList) {
                cashRegisterListCashRegister.setKeyId(null);
                cashRegisterListCashRegister = em.merge(cashRegisterListCashRegister);
            }
            em.remove(key);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Key> findKeyEntities() {
        return findKeyEntities(true, -1, -1);
    }

    public List<Key> findKeyEntities(int maxResults, int firstResult) {
        return findKeyEntities(false, maxResults, firstResult);
    }

    private List<Key> findKeyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Key.class));
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

    public Key findKey(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Key.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Key> rt = cq.from(Key.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

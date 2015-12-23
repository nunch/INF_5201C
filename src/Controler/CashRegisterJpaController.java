/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Controler.exceptions.IllegalOrphanException;
import Controler.exceptions.NonexistentEntityException;
import Entity.CashRegister;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Session;
import Entity.Key;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class CashRegisterJpaController implements Serializable {

    public CashRegisterJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CashRegister cashRegister) {
        if (cashRegister.getKeyList() == null) {
            cashRegister.setKeyList(new ArrayList<Key>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Session sessionId = cashRegister.getSessionId();
            if (sessionId != null) {
                sessionId = em.getReference(sessionId.getClass(), sessionId.getId());
                cashRegister.setSessionId(sessionId);
            }
            Key keyId = cashRegister.getKeyId();
            if (keyId != null) {
                keyId = em.getReference(keyId.getClass(), keyId.getId());
                cashRegister.setKeyId(keyId);
            }
            List<Key> attachedKeyList = new ArrayList<Key>();
            for (Key keyListKeyToAttach : cashRegister.getKeyList()) {
                keyListKeyToAttach = em.getReference(keyListKeyToAttach.getClass(), keyListKeyToAttach.getId());
                attachedKeyList.add(keyListKeyToAttach);
            }
            cashRegister.setKeyList(attachedKeyList);
            em.persist(cashRegister);
            if (sessionId != null) {
                sessionId.getCashRegisterList().add(cashRegister);
                sessionId = em.merge(sessionId);
            }
            if (keyId != null) {
                keyId.getCashRegisterList().add(cashRegister);
                keyId = em.merge(keyId);
            }
            for (Key keyListKey : cashRegister.getKeyList()) {
                CashRegister oldCashRegisteridOfKeyListKey = keyListKey.getCashRegisterid();
                keyListKey.setCashRegisterid(cashRegister);
                keyListKey = em.merge(keyListKey);
                if (oldCashRegisteridOfKeyListKey != null) {
                    oldCashRegisteridOfKeyListKey.getKeyList().remove(keyListKey);
                    oldCashRegisteridOfKeyListKey = em.merge(oldCashRegisteridOfKeyListKey);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CashRegister cashRegister) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CashRegister persistentCashRegister = em.find(CashRegister.class, cashRegister.getId());
            Session sessionIdOld = persistentCashRegister.getSessionId();
            Session sessionIdNew = cashRegister.getSessionId();
            Key keyIdOld = persistentCashRegister.getKeyId();
            Key keyIdNew = cashRegister.getKeyId();
            List<Key> keyListOld = persistentCashRegister.getKeyList();
            List<Key> keyListNew = cashRegister.getKeyList();
            List<String> illegalOrphanMessages = null;
            for (Key keyListOldKey : keyListOld) {
                if (!keyListNew.contains(keyListOldKey)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Key " + keyListOldKey + " since its cashRegisterid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (sessionIdNew != null) {
                sessionIdNew = em.getReference(sessionIdNew.getClass(), sessionIdNew.getId());
                cashRegister.setSessionId(sessionIdNew);
            }
            if (keyIdNew != null) {
                keyIdNew = em.getReference(keyIdNew.getClass(), keyIdNew.getId());
                cashRegister.setKeyId(keyIdNew);
            }
            List<Key> attachedKeyListNew = new ArrayList<Key>();
            for (Key keyListNewKeyToAttach : keyListNew) {
                keyListNewKeyToAttach = em.getReference(keyListNewKeyToAttach.getClass(), keyListNewKeyToAttach.getId());
                attachedKeyListNew.add(keyListNewKeyToAttach);
            }
            keyListNew = attachedKeyListNew;
            cashRegister.setKeyList(keyListNew);
            cashRegister = em.merge(cashRegister);
            if (sessionIdOld != null && !sessionIdOld.equals(sessionIdNew)) {
                sessionIdOld.getCashRegisterList().remove(cashRegister);
                sessionIdOld = em.merge(sessionIdOld);
            }
            if (sessionIdNew != null && !sessionIdNew.equals(sessionIdOld)) {
                sessionIdNew.getCashRegisterList().add(cashRegister);
                sessionIdNew = em.merge(sessionIdNew);
            }
            if (keyIdOld != null && !keyIdOld.equals(keyIdNew)) {
                keyIdOld.getCashRegisterList().remove(cashRegister);
                keyIdOld = em.merge(keyIdOld);
            }
            if (keyIdNew != null && !keyIdNew.equals(keyIdOld)) {
                keyIdNew.getCashRegisterList().add(cashRegister);
                keyIdNew = em.merge(keyIdNew);
            }
            for (Key keyListNewKey : keyListNew) {
                if (!keyListOld.contains(keyListNewKey)) {
                    CashRegister oldCashRegisteridOfKeyListNewKey = keyListNewKey.getCashRegisterid();
                    keyListNewKey.setCashRegisterid(cashRegister);
                    keyListNewKey = em.merge(keyListNewKey);
                    if (oldCashRegisteridOfKeyListNewKey != null && !oldCashRegisteridOfKeyListNewKey.equals(cashRegister)) {
                        oldCashRegisteridOfKeyListNewKey.getKeyList().remove(keyListNewKey);
                        oldCashRegisteridOfKeyListNewKey = em.merge(oldCashRegisteridOfKeyListNewKey);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cashRegister.getId();
                if (findCashRegister(id) == null) {
                    throw new NonexistentEntityException("The cashRegister with id " + id + " no longer exists.");
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
            CashRegister cashRegister;
            try {
                cashRegister = em.getReference(CashRegister.class, id);
                cashRegister.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cashRegister with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Key> keyListOrphanCheck = cashRegister.getKeyList();
            for (Key keyListOrphanCheckKey : keyListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CashRegister (" + cashRegister + ") cannot be destroyed since the Key " + keyListOrphanCheckKey + " in its keyList field has a non-nullable cashRegisterid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Session sessionId = cashRegister.getSessionId();
            if (sessionId != null) {
                sessionId.getCashRegisterList().remove(cashRegister);
                sessionId = em.merge(sessionId);
            }
            Key keyId = cashRegister.getKeyId();
            if (keyId != null) {
                keyId.getCashRegisterList().remove(cashRegister);
                keyId = em.merge(keyId);
            }
            em.remove(cashRegister);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CashRegister> findCashRegisterEntities() {
        return findCashRegisterEntities(true, -1, -1);
    }

    public List<CashRegister> findCashRegisterEntities(int maxResults, int firstResult) {
        return findCashRegisterEntities(false, maxResults, firstResult);
    }

    private List<CashRegister> findCashRegisterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CashRegister.class));
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

    public CashRegister findCashRegister(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CashRegister.class, id);
        } finally {
            em.close();
        }
    }

    public int getCashRegisterCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CashRegister> rt = cq.from(CashRegister.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

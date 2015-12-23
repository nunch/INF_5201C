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
import Entity.Payment;
import Entity.Transaction;
import Entity.TransactionArticles;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class TransactionJpaController implements Serializable {

    public TransactionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaction transaction) {
        if (transaction.getTransactionArticlesList() == null) {
            transaction.setTransactionArticlesList(new ArrayList<TransactionArticles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Payment paymentId = transaction.getPaymentId();
            if (paymentId != null) {
                paymentId = em.getReference(paymentId.getClass(), paymentId.getId());
                transaction.setPaymentId(paymentId);
            }
            List<TransactionArticles> attachedTransactionArticlesList = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesListTransactionArticlesToAttach : transaction.getTransactionArticlesList()) {
                transactionArticlesListTransactionArticlesToAttach = em.getReference(transactionArticlesListTransactionArticlesToAttach.getClass(), transactionArticlesListTransactionArticlesToAttach.getId());
                attachedTransactionArticlesList.add(transactionArticlesListTransactionArticlesToAttach);
            }
            transaction.setTransactionArticlesList(attachedTransactionArticlesList);
            em.persist(transaction);
            if (paymentId != null) {
                paymentId.getTransactionList().add(transaction);
                paymentId = em.merge(paymentId);
            }
            for (TransactionArticles transactionArticlesListTransactionArticles : transaction.getTransactionArticlesList()) {
                Transaction oldTransactionIdOfTransactionArticlesListTransactionArticles = transactionArticlesListTransactionArticles.getTransactionId();
                transactionArticlesListTransactionArticles.setTransactionId(transaction);
                transactionArticlesListTransactionArticles = em.merge(transactionArticlesListTransactionArticles);
                if (oldTransactionIdOfTransactionArticlesListTransactionArticles != null) {
                    oldTransactionIdOfTransactionArticlesListTransactionArticles.getTransactionArticlesList().remove(transactionArticlesListTransactionArticles);
                    oldTransactionIdOfTransactionArticlesListTransactionArticles = em.merge(oldTransactionIdOfTransactionArticlesListTransactionArticles);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaction transaction) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaction persistentTransaction = em.find(Transaction.class, transaction.getId());
            Payment paymentIdOld = persistentTransaction.getPaymentId();
            Payment paymentIdNew = transaction.getPaymentId();
            List<TransactionArticles> transactionArticlesListOld = persistentTransaction.getTransactionArticlesList();
            List<TransactionArticles> transactionArticlesListNew = transaction.getTransactionArticlesList();
            List<String> illegalOrphanMessages = null;
            for (TransactionArticles transactionArticlesListOldTransactionArticles : transactionArticlesListOld) {
                if (!transactionArticlesListNew.contains(transactionArticlesListOldTransactionArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TransactionArticles " + transactionArticlesListOldTransactionArticles + " since its transactionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (paymentIdNew != null) {
                paymentIdNew = em.getReference(paymentIdNew.getClass(), paymentIdNew.getId());
                transaction.setPaymentId(paymentIdNew);
            }
            List<TransactionArticles> attachedTransactionArticlesListNew = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesListNewTransactionArticlesToAttach : transactionArticlesListNew) {
                transactionArticlesListNewTransactionArticlesToAttach = em.getReference(transactionArticlesListNewTransactionArticlesToAttach.getClass(), transactionArticlesListNewTransactionArticlesToAttach.getId());
                attachedTransactionArticlesListNew.add(transactionArticlesListNewTransactionArticlesToAttach);
            }
            transactionArticlesListNew = attachedTransactionArticlesListNew;
            transaction.setTransactionArticlesList(transactionArticlesListNew);
            transaction = em.merge(transaction);
            if (paymentIdOld != null && !paymentIdOld.equals(paymentIdNew)) {
                paymentIdOld.getTransactionList().remove(transaction);
                paymentIdOld = em.merge(paymentIdOld);
            }
            if (paymentIdNew != null && !paymentIdNew.equals(paymentIdOld)) {
                paymentIdNew.getTransactionList().add(transaction);
                paymentIdNew = em.merge(paymentIdNew);
            }
            for (TransactionArticles transactionArticlesListNewTransactionArticles : transactionArticlesListNew) {
                if (!transactionArticlesListOld.contains(transactionArticlesListNewTransactionArticles)) {
                    Transaction oldTransactionIdOfTransactionArticlesListNewTransactionArticles = transactionArticlesListNewTransactionArticles.getTransactionId();
                    transactionArticlesListNewTransactionArticles.setTransactionId(transaction);
                    transactionArticlesListNewTransactionArticles = em.merge(transactionArticlesListNewTransactionArticles);
                    if (oldTransactionIdOfTransactionArticlesListNewTransactionArticles != null && !oldTransactionIdOfTransactionArticlesListNewTransactionArticles.equals(transaction)) {
                        oldTransactionIdOfTransactionArticlesListNewTransactionArticles.getTransactionArticlesList().remove(transactionArticlesListNewTransactionArticles);
                        oldTransactionIdOfTransactionArticlesListNewTransactionArticles = em.merge(oldTransactionIdOfTransactionArticlesListNewTransactionArticles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transaction.getId();
                if (findTransaction(id) == null) {
                    throw new NonexistentEntityException("The transaction with id " + id + " no longer exists.");
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
            Transaction transaction;
            try {
                transaction = em.getReference(Transaction.class, id);
                transaction.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaction with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TransactionArticles> transactionArticlesListOrphanCheck = transaction.getTransactionArticlesList();
            for (TransactionArticles transactionArticlesListOrphanCheckTransactionArticles : transactionArticlesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Transaction (" + transaction + ") cannot be destroyed since the TransactionArticles " + transactionArticlesListOrphanCheckTransactionArticles + " in its transactionArticlesList field has a non-nullable transactionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Payment paymentId = transaction.getPaymentId();
            if (paymentId != null) {
                paymentId.getTransactionList().remove(transaction);
                paymentId = em.merge(paymentId);
            }
            em.remove(transaction);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaction> findTransactionEntities() {
        return findTransactionEntities(true, -1, -1);
    }

    public List<Transaction> findTransactionEntities(int maxResults, int firstResult) {
        return findTransactionEntities(false, maxResults, firstResult);
    }

    private List<Transaction> findTransactionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaction.class));
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

    public Transaction findTransaction(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaction.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransactionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaction> rt = cq.from(Transaction.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

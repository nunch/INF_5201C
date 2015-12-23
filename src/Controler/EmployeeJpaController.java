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
import Entity.Retailer;
import java.util.ArrayList;
import java.util.List;
import Entity.Cashier;
import Entity.Employee;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yo
 */
public class EmployeeJpaController implements Serializable {

    public EmployeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) {
        if (employee.getRetailerList() == null) {
            employee.setRetailerList(new ArrayList<Retailer>());
        }
        if (employee.getCashierList() == null) {
            employee.setCashierList(new ArrayList<Cashier>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Retailer> attachedRetailerList = new ArrayList<Retailer>();
            for (Retailer retailerListRetailerToAttach : employee.getRetailerList()) {
                retailerListRetailerToAttach = em.getReference(retailerListRetailerToAttach.getClass(), retailerListRetailerToAttach.getId());
                attachedRetailerList.add(retailerListRetailerToAttach);
            }
            employee.setRetailerList(attachedRetailerList);
            List<Cashier> attachedCashierList = new ArrayList<Cashier>();
            for (Cashier cashierListCashierToAttach : employee.getCashierList()) {
                cashierListCashierToAttach = em.getReference(cashierListCashierToAttach.getClass(), cashierListCashierToAttach.getId());
                attachedCashierList.add(cashierListCashierToAttach);
            }
            employee.setCashierList(attachedCashierList);
            em.persist(employee);
            for (Retailer retailerListRetailer : employee.getRetailerList()) {
                Employee oldEmployeeIdOfRetailerListRetailer = retailerListRetailer.getEmployeeId();
                retailerListRetailer.setEmployeeId(employee);
                retailerListRetailer = em.merge(retailerListRetailer);
                if (oldEmployeeIdOfRetailerListRetailer != null) {
                    oldEmployeeIdOfRetailerListRetailer.getRetailerList().remove(retailerListRetailer);
                    oldEmployeeIdOfRetailerListRetailer = em.merge(oldEmployeeIdOfRetailerListRetailer);
                }
            }
            for (Cashier cashierListCashier : employee.getCashierList()) {
                Employee oldEmployeeIdOfCashierListCashier = cashierListCashier.getEmployeeId();
                cashierListCashier.setEmployeeId(employee);
                cashierListCashier = em.merge(cashierListCashier);
                if (oldEmployeeIdOfCashierListCashier != null) {
                    oldEmployeeIdOfCashierListCashier.getCashierList().remove(cashierListCashier);
                    oldEmployeeIdOfCashierListCashier = em.merge(oldEmployeeIdOfCashierListCashier);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Employee employee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee persistentEmployee = em.find(Employee.class, employee.getId());
            List<Retailer> retailerListOld = persistentEmployee.getRetailerList();
            List<Retailer> retailerListNew = employee.getRetailerList();
            List<Cashier> cashierListOld = persistentEmployee.getCashierList();
            List<Cashier> cashierListNew = employee.getCashierList();
            List<String> illegalOrphanMessages = null;
            for (Retailer retailerListOldRetailer : retailerListOld) {
                if (!retailerListNew.contains(retailerListOldRetailer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Retailer " + retailerListOldRetailer + " since its employeeId field is not nullable.");
                }
            }
            for (Cashier cashierListOldCashier : cashierListOld) {
                if (!cashierListNew.contains(cashierListOldCashier)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cashier " + cashierListOldCashier + " since its employeeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Retailer> attachedRetailerListNew = new ArrayList<Retailer>();
            for (Retailer retailerListNewRetailerToAttach : retailerListNew) {
                retailerListNewRetailerToAttach = em.getReference(retailerListNewRetailerToAttach.getClass(), retailerListNewRetailerToAttach.getId());
                attachedRetailerListNew.add(retailerListNewRetailerToAttach);
            }
            retailerListNew = attachedRetailerListNew;
            employee.setRetailerList(retailerListNew);
            List<Cashier> attachedCashierListNew = new ArrayList<Cashier>();
            for (Cashier cashierListNewCashierToAttach : cashierListNew) {
                cashierListNewCashierToAttach = em.getReference(cashierListNewCashierToAttach.getClass(), cashierListNewCashierToAttach.getId());
                attachedCashierListNew.add(cashierListNewCashierToAttach);
            }
            cashierListNew = attachedCashierListNew;
            employee.setCashierList(cashierListNew);
            employee = em.merge(employee);
            for (Retailer retailerListNewRetailer : retailerListNew) {
                if (!retailerListOld.contains(retailerListNewRetailer)) {
                    Employee oldEmployeeIdOfRetailerListNewRetailer = retailerListNewRetailer.getEmployeeId();
                    retailerListNewRetailer.setEmployeeId(employee);
                    retailerListNewRetailer = em.merge(retailerListNewRetailer);
                    if (oldEmployeeIdOfRetailerListNewRetailer != null && !oldEmployeeIdOfRetailerListNewRetailer.equals(employee)) {
                        oldEmployeeIdOfRetailerListNewRetailer.getRetailerList().remove(retailerListNewRetailer);
                        oldEmployeeIdOfRetailerListNewRetailer = em.merge(oldEmployeeIdOfRetailerListNewRetailer);
                    }
                }
            }
            for (Cashier cashierListNewCashier : cashierListNew) {
                if (!cashierListOld.contains(cashierListNewCashier)) {
                    Employee oldEmployeeIdOfCashierListNewCashier = cashierListNewCashier.getEmployeeId();
                    cashierListNewCashier.setEmployeeId(employee);
                    cashierListNewCashier = em.merge(cashierListNewCashier);
                    if (oldEmployeeIdOfCashierListNewCashier != null && !oldEmployeeIdOfCashierListNewCashier.equals(employee)) {
                        oldEmployeeIdOfCashierListNewCashier.getCashierList().remove(cashierListNewCashier);
                        oldEmployeeIdOfCashierListNewCashier = em.merge(oldEmployeeIdOfCashierListNewCashier);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = employee.getId();
                if (findEmployee(id) == null) {
                    throw new NonexistentEntityException("The employee with id " + id + " no longer exists.");
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
            Employee employee;
            try {
                employee = em.getReference(Employee.class, id);
                employee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Retailer> retailerListOrphanCheck = employee.getRetailerList();
            for (Retailer retailerListOrphanCheckRetailer : retailerListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the Retailer " + retailerListOrphanCheckRetailer + " in its retailerList field has a non-nullable employeeId field.");
            }
            List<Cashier> cashierListOrphanCheck = employee.getCashierList();
            for (Cashier cashierListOrphanCheckCashier : cashierListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the Cashier " + cashierListOrphanCheckCashier + " in its cashierList field has a non-nullable employeeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(employee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Employee> findEmployeeEntities() {
        return findEmployeeEntities(true, -1, -1);
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return findEmployeeEntities(false, maxResults, firstResult);
    }

    private List<Employee> findEmployeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
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

    public Employee findEmployee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> rt = cq.from(Employee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

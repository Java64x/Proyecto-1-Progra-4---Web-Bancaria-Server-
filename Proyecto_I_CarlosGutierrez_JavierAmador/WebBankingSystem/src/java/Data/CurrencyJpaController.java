/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Logic.Currency;
import Logic.Account;
import Data.exceptions.IllegalOrphanException;
import Data.exceptions.NonexistentEntityException;
import Data.exceptions.PreexistingEntityException;
import Data.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carlos
 */
public class CurrencyJpaController implements Serializable {

    public CurrencyJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Currency currency) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (currency.getAccountCollection() == null) {
            currency.setAccountCollection(new ArrayList<Account>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Account> attachedAccountCollection = new ArrayList<Account>();
            for (Account accountCollectionAccountToAttach : currency.getAccountCollection()) {
                accountCollectionAccountToAttach = em.getReference(accountCollectionAccountToAttach.getClass(), accountCollectionAccountToAttach.getNumber());
                attachedAccountCollection.add(accountCollectionAccountToAttach);
            }
            currency.setAccountCollection(attachedAccountCollection);
            em.persist(currency);
            for (Account accountCollectionAccount : currency.getAccountCollection()) {
                Currency oldMonedacodigoOfAccountCollectionAccount = accountCollectionAccount.getMonedacodigo();
                accountCollectionAccount.setMonedacodigo(currency);
                accountCollectionAccount = em.merge(accountCollectionAccount);
                if (oldMonedacodigoOfAccountCollectionAccount != null) {
                    oldMonedacodigoOfAccountCollectionAccount.getAccountCollection().remove(accountCollectionAccount);
                    oldMonedacodigoOfAccountCollectionAccount = em.merge(oldMonedacodigoOfAccountCollectionAccount);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCurrency(currency.getCode()) != null) {
                throw new PreexistingEntityException("Currency " + currency + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Currency currency) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Currency persistentCurrency = em.find(Currency.class, currency.getCode());
            Collection<Account> accountCollectionOld = persistentCurrency.getAccountCollection();
            Collection<Account> accountCollectionNew = currency.getAccountCollection();
            List<String> illegalOrphanMessages = null;
            for (Account accountCollectionOldAccount : accountCollectionOld) {
                if (!accountCollectionNew.contains(accountCollectionOldAccount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Account " + accountCollectionOldAccount + " since its monedacodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Account> attachedAccountCollectionNew = new ArrayList<Account>();
            for (Account accountCollectionNewAccountToAttach : accountCollectionNew) {
                accountCollectionNewAccountToAttach = em.getReference(accountCollectionNewAccountToAttach.getClass(), accountCollectionNewAccountToAttach.getNumber());
                attachedAccountCollectionNew.add(accountCollectionNewAccountToAttach);
            }
            accountCollectionNew = attachedAccountCollectionNew;
            currency.setAccountCollection(accountCollectionNew);
            currency = em.merge(currency);
            for (Account accountCollectionNewAccount : accountCollectionNew) {
                if (!accountCollectionOld.contains(accountCollectionNewAccount)) {
                    Currency oldMonedacodigoOfAccountCollectionNewAccount = accountCollectionNewAccount.getMonedacodigo();
                    accountCollectionNewAccount.setMonedacodigo(currency);
                    accountCollectionNewAccount = em.merge(accountCollectionNewAccount);
                    if (oldMonedacodigoOfAccountCollectionNewAccount != null && !oldMonedacodigoOfAccountCollectionNewAccount.equals(currency)) {
                        oldMonedacodigoOfAccountCollectionNewAccount.getAccountCollection().remove(accountCollectionNewAccount);
                        oldMonedacodigoOfAccountCollectionNewAccount = em.merge(oldMonedacodigoOfAccountCollectionNewAccount);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = currency.getCode();
                if (findCurrency(id) == null) {
                    throw new NonexistentEntityException("The currency with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Currency currency;
            try {
                currency = em.getReference(Currency.class, id);
                currency.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The currency with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Account> accountCollectionOrphanCheck = currency.getAccountCollection();
            for (Account accountCollectionOrphanCheckAccount : accountCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Currency (" + currency + ") cannot be destroyed since the Account " + accountCollectionOrphanCheckAccount + " in its accountCollection field has a non-nullable monedacodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(currency);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Currency> findCurrencyEntities() {
        return findCurrencyEntities(true, -1, -1);
    }

    public List<Currency> findCurrencyEntities(int maxResults, int firstResult) {
        return findCurrencyEntities(false, maxResults, firstResult);
    }

    private List<Currency> findCurrencyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Currency.class));
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

    public Currency findCurrency(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Currency.class, id);
        } finally {
            em.close();
        }
    }

    public int getCurrencyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Currency> rt = cq.from(Currency.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

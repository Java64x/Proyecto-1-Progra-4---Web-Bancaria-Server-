/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Logic.Transaction;
import Logic.Client;
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
public class AccountJpaController implements Serializable {

    public AccountJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Account account) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (account.getTransactionCollection() == null) {
            account.setTransactionCollection(new ArrayList<Transaction>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Client cedulaUsuario = account.getCedulaUsuario();
            if (cedulaUsuario != null) {
                cedulaUsuario = em.getReference(cedulaUsuario.getClass(), cedulaUsuario.getIdNumber());
                account.setCedulaUsuario(cedulaUsuario);
            }
            Currency monedacodigo = account.getMonedacodigo();
            if (monedacodigo != null) {
                monedacodigo = em.getReference(monedacodigo.getClass(), monedacodigo.getCode());
                account.setMonedacodigo(monedacodigo);
            }
            Collection<Transaction> attachedTransactionCollection = new ArrayList<Transaction>();
            for (Transaction transactionCollectionTransactionToAttach : account.getTransactionCollection()) {
                transactionCollectionTransactionToAttach = em.getReference(transactionCollectionTransactionToAttach.getClass(), transactionCollectionTransactionToAttach.getNumber());
                attachedTransactionCollection.add(transactionCollectionTransactionToAttach);
            }
            account.setTransactionCollection(attachedTransactionCollection);
            em.persist(account);
            if (cedulaUsuario != null) {
                cedulaUsuario.getAccountCollection().add(account);
                cedulaUsuario = em.merge(cedulaUsuario);
            }
            if (monedacodigo != null) {
                monedacodigo.getAccountCollection().add(account);
                monedacodigo = em.merge(monedacodigo);
            }
            for (Transaction transactionCollectionTransaction : account.getTransactionCollection()) {
                Account oldAccountnumberOfTransactionCollectionTransaction = transactionCollectionTransaction.getAccountnumber();
                transactionCollectionTransaction.setAccountnumber(account);
                transactionCollectionTransaction = em.merge(transactionCollectionTransaction);
                if (oldAccountnumberOfTransactionCollectionTransaction != null) {
                    oldAccountnumberOfTransactionCollectionTransaction.getTransactionCollection().remove(transactionCollectionTransaction);
                    oldAccountnumberOfTransactionCollectionTransaction = em.merge(oldAccountnumberOfTransactionCollectionTransaction);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAccount(account.getNumber()) != null) {
                throw new PreexistingEntityException("Account " + account + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Account account) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Account persistentAccount = em.find(Account.class, account.getNumber());
            Client cedulaUsuarioOld = persistentAccount.getCedulaUsuario();
            Client cedulaUsuarioNew = account.getCedulaUsuario();
            Currency monedacodigoOld = persistentAccount.getMonedacodigo();
            Currency monedacodigoNew = account.getMonedacodigo();
            Collection<Transaction> transactionCollectionOld = persistentAccount.getTransactionCollection();
            Collection<Transaction> transactionCollectionNew = account.getTransactionCollection();
            List<String> illegalOrphanMessages = null;
            for (Transaction transactionCollectionOldTransaction : transactionCollectionOld) {
                if (!transactionCollectionNew.contains(transactionCollectionOldTransaction)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaction " + transactionCollectionOldTransaction + " since its accountnumber field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cedulaUsuarioNew != null) {
                cedulaUsuarioNew = em.getReference(cedulaUsuarioNew.getClass(), cedulaUsuarioNew.getIdNumber());
                account.setCedulaUsuario(cedulaUsuarioNew);
            }
            if (monedacodigoNew != null) {
                monedacodigoNew = em.getReference(monedacodigoNew.getClass(), monedacodigoNew.getCode());
                account.setMonedacodigo(monedacodigoNew);
            }
            Collection<Transaction> attachedTransactionCollectionNew = new ArrayList<Transaction>();
            for (Transaction transactionCollectionNewTransactionToAttach : transactionCollectionNew) {
                transactionCollectionNewTransactionToAttach = em.getReference(transactionCollectionNewTransactionToAttach.getClass(), transactionCollectionNewTransactionToAttach.getNumber());
                attachedTransactionCollectionNew.add(transactionCollectionNewTransactionToAttach);
            }
            transactionCollectionNew = attachedTransactionCollectionNew;
            account.setTransactionCollection(transactionCollectionNew);
            account = em.merge(account);
            if (cedulaUsuarioOld != null && !cedulaUsuarioOld.equals(cedulaUsuarioNew)) {
                cedulaUsuarioOld.getAccountCollection().remove(account);
                cedulaUsuarioOld = em.merge(cedulaUsuarioOld);
            }
            if (cedulaUsuarioNew != null && !cedulaUsuarioNew.equals(cedulaUsuarioOld)) {
                cedulaUsuarioNew.getAccountCollection().add(account);
                cedulaUsuarioNew = em.merge(cedulaUsuarioNew);
            }
            if (monedacodigoOld != null && !monedacodigoOld.equals(monedacodigoNew)) {
                monedacodigoOld.getAccountCollection().remove(account);
                monedacodigoOld = em.merge(monedacodigoOld);
            }
            if (monedacodigoNew != null && !monedacodigoNew.equals(monedacodigoOld)) {
                monedacodigoNew.getAccountCollection().add(account);
                monedacodigoNew = em.merge(monedacodigoNew);
            }
            for (Transaction transactionCollectionNewTransaction : transactionCollectionNew) {
                if (!transactionCollectionOld.contains(transactionCollectionNewTransaction)) {
                    Account oldAccountnumberOfTransactionCollectionNewTransaction = transactionCollectionNewTransaction.getAccountnumber();
                    transactionCollectionNewTransaction.setAccountnumber(account);
                    transactionCollectionNewTransaction = em.merge(transactionCollectionNewTransaction);
                    if (oldAccountnumberOfTransactionCollectionNewTransaction != null && !oldAccountnumberOfTransactionCollectionNewTransaction.equals(account)) {
                        oldAccountnumberOfTransactionCollectionNewTransaction.getTransactionCollection().remove(transactionCollectionNewTransaction);
                        oldAccountnumberOfTransactionCollectionNewTransaction = em.merge(oldAccountnumberOfTransactionCollectionNewTransaction);
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
                String id = account.getNumber();
                if (findAccount(id) == null) {
                    throw new NonexistentEntityException("The account with id " + id + " no longer exists.");
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
            Account account;
            try {
                account = em.getReference(Account.class, id);
                account.getNumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The account with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Transaction> transactionCollectionOrphanCheck = account.getTransactionCollection();
            for (Transaction transactionCollectionOrphanCheckTransaction : transactionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Account (" + account + ") cannot be destroyed since the Transaction " + transactionCollectionOrphanCheckTransaction + " in its transactionCollection field has a non-nullable accountnumber field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Client cedulaUsuario = account.getCedulaUsuario();
            if (cedulaUsuario != null) {
                cedulaUsuario.getAccountCollection().remove(account);
                cedulaUsuario = em.merge(cedulaUsuario);
            }
            Currency monedacodigo = account.getMonedacodigo();
            if (monedacodigo != null) {
                monedacodigo.getAccountCollection().remove(account);
                monedacodigo = em.merge(monedacodigo);
            }
            em.remove(account);
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

    public List<Account> findAccountEntities() {
        return findAccountEntities(true, -1, -1);
    }

    public List<Account> findAccountEntities(int maxResults, int firstResult) {
        return findAccountEntities(false, maxResults, firstResult);
    }

    private List<Account> findAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Account.class));
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

    public Account findAccount(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Account.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Account> rt = cq.from(Account.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

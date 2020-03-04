/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
    , @NamedQuery(name = "Account.findByNumber", query = "SELECT a FROM Account a WHERE a.number = :number")
    , @NamedQuery(name = "Account.findByBalance", query = "SELECT a FROM Account a WHERE a.balance = :balance")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 22)
    @Column(name = "number")
    private String number;
    @Size(max = 45)
    @Column(name = "balance")
    private String balance;
    @JoinColumn(name = "cedulaUsuario", referencedColumnName = "idNumber")
    @ManyToOne(optional = false)
    private Client cedulaUsuario;
    @JoinColumn(name = "Moneda_codigo", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private Currency monedacodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountnumber")
    private Collection<Transaction> transactionCollection;

    public Account() {
    }

    public Account(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Client getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(Client cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public Currency getMonedacodigo() {
        return monedacodigo;
    }

    public void setMonedacodigo(Currency monedacodigo) {
        this.monedacodigo = monedacodigo;
    }

    @XmlTransient
    public Collection<Transaction> getTransactionCollection() {
        return transactionCollection;
    }

    public void setTransactionCollection(Collection<Transaction> transactionCollection) {
        this.transactionCollection = transactionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (number != null ? number.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.number == null && other.number != null) || (this.number != null && !this.number.equals(other.number))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Data.Account[ number=" + number + " ]";
    }
    
}

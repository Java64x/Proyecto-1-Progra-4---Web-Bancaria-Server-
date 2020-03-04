/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemabancario.Logic;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "cashier")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cashier.findAll", query = "SELECT c FROM Cashier c")
    , @NamedQuery(name = "Cashier.findByIdNumber", query = "SELECT c FROM Cashier c WHERE c.idNumber = :idNumber")
    , @NamedQuery(name = "Cashier.findByPassword", query = "SELECT c FROM Cashier c WHERE c.password = :password")})
public class Cashier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idNumber")
    private String idNumber;
    @Column(name = "password")
    private String password;

    public Cashier() {
    }

    public Cashier(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNumber != null ? idNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cashier)) {
            return false;
        }
        Cashier other = (Cashier) object;
        if ((this.idNumber == null && other.idNumber != null) || (this.idNumber != null && !this.idNumber.equals(other.idNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sistemabancario.Logica.Cashier[ idNumber=" + idNumber + " ]";
    }
    
}

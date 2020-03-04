/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemabancario.Logica;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "cuentabancaria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuentabancaria.findAll", query = "SELECT c FROM Cuentabancaria c")
    , @NamedQuery(name = "Cuentabancaria.findByNumero", query = "SELECT c FROM Cuentabancaria c WHERE c.cuentabancariaPK.numero = :numero")
    , @NamedQuery(name = "Cuentabancaria.findBySaldo", query = "SELECT c FROM Cuentabancaria c WHERE c.saldo = :saldo")
    , @NamedQuery(name = "Cuentabancaria.findByMonedacodigo", query = "SELECT c FROM Cuentabancaria c WHERE c.cuentabancariaPK.monedacodigo = :monedacodigo")})
public class Cuentabancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CuentabancariaPK cuentabancariaPK;
    @Column(name = "saldo")
    private String saldo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuentabancaria")
    private Collection<Movimiento> movimientoCollection;
    @JoinColumn(name = "Moneda_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Moneda moneda;
    @JoinColumn(name = "cedulaUsuario", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Usuario cedulaUsuario;

    public Cuentabancaria() {
    }

    public Cuentabancaria(CuentabancariaPK cuentabancariaPK) {
        this.cuentabancariaPK = cuentabancariaPK;
    }

    public Cuentabancaria(String numero, String monedacodigo) {
        this.cuentabancariaPK = new CuentabancariaPK(numero, monedacodigo);
    }

    public CuentabancariaPK getCuentabancariaPK() {
        return cuentabancariaPK;
    }

    public void setCuentabancariaPK(CuentabancariaPK cuentabancariaPK) {
        this.cuentabancariaPK = cuentabancariaPK;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    @XmlTransient
    public Collection<Movimiento> getMovimientoCollection() {
        return movimientoCollection;
    }

    public void setMovimientoCollection(Collection<Movimiento> movimientoCollection) {
        this.movimientoCollection = movimientoCollection;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public Usuario getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(Usuario cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuentabancariaPK != null ? cuentabancariaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuentabancaria)) {
            return false;
        }
        Cuentabancaria other = (Cuentabancaria) object;
        if ((this.cuentabancariaPK == null && other.cuentabancariaPK != null) || (this.cuentabancariaPK != null && !this.cuentabancariaPK.equals(other.cuentabancariaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sistemabancario.Logica.Cuentabancaria[ cuentabancariaPK=" + cuentabancariaPK + " ]";
    }
    
}

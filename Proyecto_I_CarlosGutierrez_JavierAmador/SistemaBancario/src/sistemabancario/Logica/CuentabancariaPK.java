/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemabancario.Logica;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Carlos
 */
@Embeddable
public class CuentabancariaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @Column(name = "Moneda_codigo")
    private String monedacodigo;

    public CuentabancariaPK() {
    }

    public CuentabancariaPK(String numero, String monedacodigo) {
        this.numero = numero;
        this.monedacodigo = monedacodigo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getMonedacodigo() {
        return monedacodigo;
    }

    public void setMonedacodigo(String monedacodigo) {
        this.monedacodigo = monedacodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        hash += (monedacodigo != null ? monedacodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuentabancariaPK)) {
            return false;
        }
        CuentabancariaPK other = (CuentabancariaPK) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        if ((this.monedacodigo == null && other.monedacodigo != null) || (this.monedacodigo != null && !this.monedacodigo.equals(other.monedacodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sistemabancario.Logica.CuentabancariaPK[ numero=" + numero + ", monedacodigo=" + monedacodigo + " ]";
    }
    
}

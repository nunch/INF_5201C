/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author yo
 */
@Entity
@Table(name = "key")
@NamedQueries({
    @NamedQuery(name = "Key.findAll", query = "SELECT k FROM Key k"),
    @NamedQuery(name = "Key.findById", query = "SELECT k FROM Key k WHERE k.id = :id")})
public class Key implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(mappedBy = "keyId")
    private List<CashRegister> cashRegisterList;
    @JoinColumn(name = "cashRegister_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CashRegister cashRegisterid;

    public Key() {
    }

    public Key(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CashRegister> getCashRegisterList() {
        return cashRegisterList;
    }

    public void setCashRegisterList(List<CashRegister> cashRegisterList) {
        this.cashRegisterList = cashRegisterList;
    }

    public CashRegister getCashRegisterid() {
        return cashRegisterid;
    }

    public void setCashRegisterid(CashRegister cashRegisterid) {
        this.cashRegisterid = cashRegisterid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Key)) {
            return false;
        }
        Key other = (Key) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Key[ id=" + id + " ]";
    }
    
}

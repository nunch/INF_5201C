/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "cashRegister")
@NamedQueries({
    @NamedQuery(name = "CashRegister.findAll", query = "SELECT c FROM CashRegister c"),
    @NamedQuery(name = "CashRegister.findById", query = "SELECT c FROM CashRegister c WHERE c.id = :id")})
public class CashRegister implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    @ManyToOne
    private Session sessionId;
    @JoinColumn(name = "key_id", referencedColumnName = "id")
    @ManyToOne
    private Key keyId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cashRegisterid")
    private List<Key> keyList;

    public CashRegister() {
    }

    public CashRegister(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Session getSessionId() {
        return sessionId;
    }

    public void setSessionId(Session sessionId) {
        this.sessionId = sessionId;
    }

    public Key getKeyId() {
        return keyId;
    }

    public void setKeyId(Key keyId) {
        this.keyId = keyId;
    }

    public List<Key> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<Key> keyList) {
        this.keyList = keyList;
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
        if (!(object instanceof CashRegister)) {
            return false;
        }
        CashRegister other = (CashRegister) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.CashRegister[ id=" + id + " ]";
    }
    
}

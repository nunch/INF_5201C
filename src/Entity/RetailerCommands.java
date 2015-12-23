/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
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
import javax.persistence.Table;

/**
 *
 * @author yo
 */
@Entity
@Table(name = "retailerCommands")
@NamedQueries({
    @NamedQuery(name = "RetailerCommands.findAll", query = "SELECT r FROM RetailerCommands r"),
    @NamedQuery(name = "RetailerCommands.findById", query = "SELECT r FROM RetailerCommands r WHERE r.id = :id")})
public class RetailerCommands implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "retailer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Retailer retailerId;
    @JoinColumn(name = "command_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Command commandId;

    public RetailerCommands() {
    }

    public RetailerCommands(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Retailer getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Retailer retailerId) {
        this.retailerId = retailerId;
    }

    public Command getCommandId() {
        return commandId;
    }

    public void setCommandId(Command commandId) {
        this.commandId = commandId;
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
        if (!(object instanceof RetailerCommands)) {
            return false;
        }
        RetailerCommands other = (RetailerCommands) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.RetailerCommands[ id=" + id + " ]";
    }
    
}

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
@Table(name = "commandedArticles")
@NamedQueries({
    @NamedQuery(name = "CommandedArticles.findAll", query = "SELECT c FROM CommandedArticles c"),
    @NamedQuery(name = "CommandedArticles.findById", query = "SELECT c FROM CommandedArticles c WHERE c.id = :id"),
    @NamedQuery(name = "CommandedArticles.findByQuantity", query = "SELECT c FROM CommandedArticles c WHERE c.quantity = :quantity")})
public class CommandedArticles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @JoinColumn(name = "command_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Command commandId;
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Article articleId;

    public CommandedArticles() {
    }

    public CommandedArticles(Integer id) {
        this.id = id;
    }

    public CommandedArticles(Integer id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Command getCommandId() {
        return commandId;
    }

    public void setCommandId(Command commandId) {
        this.commandId = commandId;
    }

    public Article getArticleId() {
        return articleId;
    }

    public void setArticleId(Article articleId) {
        this.articleId = articleId;
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
        if (!(object instanceof CommandedArticles)) {
            return false;
        }
        CommandedArticles other = (CommandedArticles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.CommandedArticles[ id=" + id + " ]";
    }
    
}

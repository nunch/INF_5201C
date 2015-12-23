/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "article")
@NamedQueries({
    @NamedQuery(name = "Article.findAll", query = "SELECT a FROM Article a"),
    @NamedQuery(name = "Article.findById", query = "SELECT a FROM Article a WHERE a.id = :id"),
    @NamedQuery(name = "Article.findByName", query = "SELECT a FROM Article a WHERE a.name = :name"),
    @NamedQuery(name = "Article.findByStock", query = "SELECT a FROM Article a WHERE a.stock = :stock"),
    @NamedQuery(name = "Article.findByBarcode", query = "SELECT a FROM Article a WHERE a.barcode = :barcode"),
    @NamedQuery(name = "Article.findByThreshold", query = "SELECT a FROM Article a WHERE a.threshold = :threshold"),
    @NamedQuery(name = "Article.findByPrice", query = "SELECT a FROM Article a WHERE a.price = :price"),
    @NamedQuery(name = "Article.findByType", query = "SELECT a FROM Article a WHERE a.type = :type")})
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "stock")
    private int stock;
    @Basic(optional = false)
    @Column(name = "barcode")
    private String barcode;
    @Basic(optional = false)
    @Column(name = "threshold")
    private int threshold;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articleId")
    private List<CommandedArticles> commandedArticlesList;
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Provider providerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articleId")
    private List<WarehouseArticles> warehouseArticlesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articleId")
    private List<AisleArticles> aisleArticlesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articleId")
    private List<TransactionArticles> transactionArticlesList;

    public Article() {
    }

    public Article(Integer id) {
        this.id = id;
    }

    public Article(Integer id, String name, int stock, String barcode, int threshold, BigDecimal price, String type) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.barcode = barcode;
        this.threshold = threshold;
        this.price = price;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CommandedArticles> getCommandedArticlesList() {
        return commandedArticlesList;
    }

    public void setCommandedArticlesList(List<CommandedArticles> commandedArticlesList) {
        this.commandedArticlesList = commandedArticlesList;
    }

    public Provider getProviderId() {
        return providerId;
    }

    public void setProviderId(Provider providerId) {
        this.providerId = providerId;
    }

    public List<WarehouseArticles> getWarehouseArticlesList() {
        return warehouseArticlesList;
    }

    public void setWarehouseArticlesList(List<WarehouseArticles> warehouseArticlesList) {
        this.warehouseArticlesList = warehouseArticlesList;
    }

    public List<AisleArticles> getAisleArticlesList() {
        return aisleArticlesList;
    }

    public void setAisleArticlesList(List<AisleArticles> aisleArticlesList) {
        this.aisleArticlesList = aisleArticlesList;
    }

    public List<TransactionArticles> getTransactionArticlesList() {
        return transactionArticlesList;
    }

    public void setTransactionArticlesList(List<TransactionArticles> transactionArticlesList) {
        this.transactionArticlesList = transactionArticlesList;
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
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Article[ id=" + id + " ]";
    }
    
}

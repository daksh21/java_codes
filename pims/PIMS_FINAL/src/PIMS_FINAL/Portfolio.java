/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PIMS_FINAL;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author daksh
 */
@Entity
@Table(name = "portfolio", catalog = "pims_new", schema = "")
@NamedQueries({
    @NamedQuery(name = "Portfolio.findAll", query = "SELECT p FROM Portfolio p"),
    @NamedQuery(name = "Portfolio.findByPid", query = "SELECT p FROM Portfolio p WHERE p.pid = :pid"),
    @NamedQuery(name = "Portfolio.findByName", query = "SELECT p FROM Portfolio p WHERE p.name = :name"),
    @NamedQuery(name = "Portfolio.findBySid", query = "SELECT p FROM Portfolio p WHERE p.sid = :sid"),
    @NamedQuery(name = "Portfolio.findByNShares", query = "SELECT p FROM Portfolio p WHERE p.nShares = :nShares")})
public class Portfolio implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "name")
    private String name;
    @Column(name = "sid")
    private Integer sid;
    @Column(name = "nShares")
    private Integer nShares;

    public Portfolio() {
    }

    public Portfolio(Integer pid) {
        this.pid = pid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        Integer oldPid = this.pid;
        this.pid = pid;
        changeSupport.firePropertyChange("pid", oldPid, pid);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        Integer oldSid = this.sid;
        this.sid = sid;
        changeSupport.firePropertyChange("sid", oldSid, sid);
    }

    public Integer getNShares() {
        return nShares;
    }

    public void setNShares(Integer nShares) {
        Integer oldNShares = this.nShares;
        this.nShares = nShares;
        changeSupport.firePropertyChange("NShares", oldNShares, nShares);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pid != null ? pid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Portfolio)) {
            return false;
        }
        Portfolio other = (Portfolio) object;
        if ((this.pid == null && other.pid != null) || (this.pid != null && !this.pid.equals(other.pid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "assignment2.Portfolio[ pid=" + pid + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.ca.dpd;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jackson
 */
@Entity
@Table(name = "cd_veterinary_species", catalog = "drugref2", schema = "public")
@NamedQueries({@NamedQuery(name = "CdVeterinarySpecies.findAll", query = "SELECT c FROM CdVeterinarySpecies c"), @NamedQuery(name = "CdVeterinarySpecies.findByDrugCode", query = "SELECT c FROM CdVeterinarySpecies c WHERE c.drugCode = :drugCode"), @NamedQuery(name = "CdVeterinarySpecies.findByVetSpecies", query = "SELECT c FROM CdVeterinarySpecies c WHERE c.vetSpecies = :vetSpecies"), @NamedQuery(name = "CdVeterinarySpecies.findByVetSubSpecies", query = "SELECT c FROM CdVeterinarySpecies c WHERE c.vetSubSpecies = :vetSubSpecies"), @NamedQuery(name = "CdVeterinarySpecies.findById", query = "SELECT c FROM CdVeterinarySpecies c WHERE c.id = :id")})
public class CdVeterinarySpecies implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "drug_code")
    private Integer drugCode;
    @Column(name = "vet_species", length = 80)
    private String vetSpecies;
    @Column(name = "vet_sub_species", length = 80)
    private String vetSubSpecies;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    public CdVeterinarySpecies() {
    }

    public CdVeterinarySpecies(Integer id) {
        this.id = id;
    }

    public Integer getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(Integer drugCode) {
        this.drugCode = drugCode;
    }

    public String getVetSpecies() {
        return vetSpecies;
    }

    public void setVetSpecies(String vetSpecies) {
        this.vetSpecies = vetSpecies;
    }

    public String getVetSubSpecies() {
        return vetSubSpecies;
    }

    public void setVetSubSpecies(String vetSubSpecies) {
        this.vetSubSpecies = vetSubSpecies;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof CdVeterinarySpecies)) {
            return false;
        }
        CdVeterinarySpecies other = (CdVeterinarySpecies) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.drugref.ca.dpd.CdVeterinarySpecies[id=" + id + "]";
    }

}

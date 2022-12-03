package org.drugref.ca.vigilance;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "vig_nomprodplus_detail")
public class NomprodplusDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nomprodPlusColumnNumber")
    private Integer nomprodPlusColumnNumber;

    @Column(name = "code")
    private String code;

    @Column(name = "descriptionFrench")
    private String descriptionFrench;

    @Column(name = "descriptionEnglish")
    private String descriptionEnglish;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomprodPlusColumnNumber(Integer nomprodPlusColumnNumber) {
        this.nomprodPlusColumnNumber = nomprodPlusColumnNumber;
    }

    public Integer getNomprodPlusColumnNumber() {
        return nomprodPlusColumnNumber;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDescriptionFrench(String descriptionFrench) {
        this.descriptionFrench = descriptionFrench;
    }

    public String getDescriptionFrench() {
        return descriptionFrench;
    }

    public void setDescriptionEnglish(String descriptionEnglish) {
        this.descriptionEnglish = descriptionEnglish;
    }

    public String getDescriptionEnglish() {
        return descriptionEnglish;
    }

    @Override
    public String toString() {
        return "NomprodplusDetail{" +
                "nomprodPlusColumnNumber=" + nomprodPlusColumnNumber + '\'' +
                "code=" + code + '\'' +
                "descriptionFrench=" + descriptionFrench + '\'' +
                "descriptionEnglish=" + descriptionEnglish + '\'' +
                '}';
    }
}

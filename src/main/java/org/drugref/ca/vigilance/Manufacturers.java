package org.drugref.ca.vigilance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "vig_manufacturers")
public class Manufacturers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "manufactureNameEnglish")
    private String manufactureNameEnglish;

    @Column(name = "manufactureNameFrench")
    private String manufactureNameFrench;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getManufactureNameEnglish() {
        return manufactureNameEnglish;
    }

    public void setManufactureNameEnglish(String manufactureNameEnglish) {
        this.manufactureNameEnglish = manufactureNameEnglish;
    }

    public String getManufactureNameFrench() {
        return manufactureNameFrench;
    }

    public void setManufactureNameFrench(String manufactureNameFrench) {
        this.manufactureNameFrench = manufactureNameFrench;
    }
}

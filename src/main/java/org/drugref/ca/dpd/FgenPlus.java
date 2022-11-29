package org.drugref.ca.dpd;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vig_fgenPlus")
public class FgenPlus {
    @Id
    @Column(name = "GENcode", nullable = false, length = 11)
    private String id;

    @Lob
    @Column(name = "GENcodeDetail")
    private String gENcodeDetail;

    @Lob
    @Column(name = "monographID")
    private String monographID;

    @Lob
    @Column(name = "genericNameFrench")
    private String genericNameFrench;

    @Lob
    @Column(name = "genericSimpleNameFrench")
    private String genericSimpleNameFrench;

    @Lob
    @Column(name = "genericNameEnglish")
    private String genericNameEnglish;

    @Lob
    @Column(name = "genericSimpleNameEnglish")
    private String genericSimpleNameEnglish;

    @Lob
    @Column(name = "usualNameFrench")
    private String usualNameFrench;

    @Lob
    @Column(name = "usualNameEnglish")
    private String usualNameEnglish;

    @Column(name = "allergenStatus")
    private Short allergenStatus;

    @Lob
    @Column(name = "ATCcode")
    private String aTCcode;

    @Lob
    @Column(name = "AHFSold")
    private String aHFSold;

    @Lob
    @Column(name = "AHFSnew")
    private String aHFSnew;

    @Lob
    @Column(name = "defaultIndication")
    private String defaultIndication;

    @Lob
    @Column(name = "tallManFrench")
    private String tallManFrench;

    @Lob
    @Column(name = "tallManEnglish")
    private String tallManEnglish;

    @Column(name = "actitityIndicator")
    private Boolean actitityIndicator;

    @Lob
    @Column(name = "allergenType")
    private String allergenType;

    @Column(name = "isDrug")
    private Boolean isDrug;

    @Lob
    @Column(name = "uniqueIdentifier")
    private String uniqueIdentifier;

    @Lob
    @Column(name = "dosageValidationRoutes")
    private String dosageValidationRoutes;

    @Column(name = "cytotoxicity")
    private Integer cytotoxicity;

    @Column(name = "excipient")
    private Boolean excipient;

    @Column(name = "comparativeDrugChart")
    private Integer comparativeDrugChart;

    @Lob
    @Column(name = "priceChart")
    private String priceChart;

    @Column(name = "isDoseAccordingToWeight")
    private Boolean isDoseAccordingToWeight;

    @Lob
    @Column(name = "TMcodesOfCCDD")
    private String tMcodesOfCCDD;

    @Column(name = "precisionIndicator")
    private Boolean precisionIndicator;

    @Column(name = "obsolescenceIndicator")
    private Boolean obsolescenceIndicator;

    @Column(name = "mgmntIndicatorProblematicDrugs")
    private Boolean mgmntIndicatorProblematicDrugs;

    @Column(name = "mgmtIndicatorDrugsInProfile")
    private Boolean mgmtIndicatorDrugsInProfile;

    @ManyToMany
    @JoinTable(name = "vig_nomprodPlus",
            joinColumns = @JoinColumn(name = "GENcode"),
            inverseJoinColumns = @JoinColumn(name = "GENcode"))
    private Set<NomprodPlus> nomprodPluses = new LinkedHashSet<>();

    public Set<NomprodPlus> getNomprodPluses() {
        return nomprodPluses;
    }

    public void setNomprodPluses(Set<NomprodPlus> nomprodPluses) {
        this.nomprodPluses = nomprodPluses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGENcodeDetail() {
        return gENcodeDetail;
    }

    public void setGENcodeDetail(String gENcodeDetail) {
        this.gENcodeDetail = gENcodeDetail;
    }

    public String getMonographID() {
        return monographID;
    }

    public void setMonographID(String monographID) {
        this.monographID = monographID;
    }

    public String getGenericNameFrench() {
        return genericNameFrench;
    }

    public void setGenericNameFrench(String genericNameFrench) {
        this.genericNameFrench = genericNameFrench;
    }

    public String getGenericSimpleNameFrench() {
        return genericSimpleNameFrench;
    }

    public void setGenericSimpleNameFrench(String genericSimpleNameFrench) {
        this.genericSimpleNameFrench = genericSimpleNameFrench;
    }

    public String getGenericNameEnglish() {
        return genericNameEnglish;
    }

    public void setGenericNameEnglish(String genericNameEnglish) {
        this.genericNameEnglish = genericNameEnglish;
    }

    public String getGenericSimpleNameEnglish() {
        return genericSimpleNameEnglish;
    }

    public void setGenericSimpleNameEnglish(String genericSimpleNameEnglish) {
        this.genericSimpleNameEnglish = genericSimpleNameEnglish;
    }

    public String getUsualNameFrench() {
        return usualNameFrench;
    }

    public void setUsualNameFrench(String usualNameFrench) {
        this.usualNameFrench = usualNameFrench;
    }

    public String getUsualNameEnglish() {
        return usualNameEnglish;
    }

    public void setUsualNameEnglish(String usualNameEnglish) {
        this.usualNameEnglish = usualNameEnglish;
    }

    public Short getAllergenStatus() {
        return allergenStatus;
    }

    public void setAllergenStatus(Short allergenStatus) {
        this.allergenStatus = allergenStatus;
    }

    public String getATCcode() {
        return aTCcode;
    }

    public void setATCcode(String aTCcode) {
        this.aTCcode = aTCcode;
    }

    public String getAHFSold() {
        return aHFSold;
    }

    public void setAHFSold(String aHFSold) {
        this.aHFSold = aHFSold;
    }

    public String getAHFSnew() {
        return aHFSnew;
    }

    public void setAHFSnew(String aHFSnew) {
        this.aHFSnew = aHFSnew;
    }

    public String getDefaultIndication() {
        return defaultIndication;
    }

    public void setDefaultIndication(String defaultIndication) {
        this.defaultIndication = defaultIndication;
    }

    public String getTallManFrench() {
        return tallManFrench;
    }

    public void setTallManFrench(String tallManFrench) {
        this.tallManFrench = tallManFrench;
    }

    public String getTallManEnglish() {
        return tallManEnglish;
    }

    public void setTallManEnglish(String tallManEnglish) {
        this.tallManEnglish = tallManEnglish;
    }

    public Boolean getActitityIndicator() {
        return actitityIndicator;
    }

    public void setActitityIndicator(Boolean actitityIndicator) {
        this.actitityIndicator = actitityIndicator;
    }

    public String getAllergenType() {
        return allergenType;
    }

    public void setAllergenType(String allergenType) {
        this.allergenType = allergenType;
    }

    public Boolean getIsDrug() {
        return isDrug;
    }

    public void setIsDrug(Boolean isDrug) {
        this.isDrug = isDrug;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getDosageValidationRoutes() {
        return dosageValidationRoutes;
    }

    public void setDosageValidationRoutes(String dosageValidationRoutes) {
        this.dosageValidationRoutes = dosageValidationRoutes;
    }

    public Integer getCytotoxicity() {
        return cytotoxicity;
    }

    public void setCytotoxicity(Integer cytotoxicity) {
        this.cytotoxicity = cytotoxicity;
    }

    public Boolean getExcipient() {
        return excipient;
    }

    public void setExcipient(Boolean excipient) {
        this.excipient = excipient;
    }

    public Integer getComparativeDrugChart() {
        return comparativeDrugChart;
    }

    public void setComparativeDrugChart(Integer comparativeDrugChart) {
        this.comparativeDrugChart = comparativeDrugChart;
    }

    public String getPriceChart() {
        return priceChart;
    }

    public void setPriceChart(String priceChart) {
        this.priceChart = priceChart;
    }

    public Boolean getIsDoseAccordingToWeight() {
        return isDoseAccordingToWeight;
    }

    public void setIsDoseAccordingToWeight(Boolean isDoseAccordingToWeight) {
        this.isDoseAccordingToWeight = isDoseAccordingToWeight;
    }

    public String getTMcodesOfCCDD() {
        return tMcodesOfCCDD;
    }

    public void setTMcodesOfCCDD(String tMcodesOfCCDD) {
        this.tMcodesOfCCDD = tMcodesOfCCDD;
    }

    public Boolean getPrecisionIndicator() {
        return precisionIndicator;
    }

    public void setPrecisionIndicator(Boolean precisionIndicator) {
        this.precisionIndicator = precisionIndicator;
    }

    public Boolean getObsolescenceIndicator() {
        return obsolescenceIndicator;
    }

    public void setObsolescenceIndicator(Boolean obsolescenceIndicator) {
        this.obsolescenceIndicator = obsolescenceIndicator;
    }

    public Boolean getMgmntIndicatorProblematicDrugs() {
        return mgmntIndicatorProblematicDrugs;
    }

    public void setMgmntIndicatorProblematicDrugs(Boolean mgmntIndicatorProblematicDrugs) {
        this.mgmntIndicatorProblematicDrugs = mgmntIndicatorProblematicDrugs;
    }

    public Boolean getMgmtIndicatorDrugsInProfile() {
        return mgmtIndicatorDrugsInProfile;
    }

    public void setMgmtIndicatorDrugsInProfile(Boolean mgmtIndicatorDrugsInProfile) {
        this.mgmtIndicatorDrugsInProfile = mgmtIndicatorDrugsInProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FgenPlus fgenPlus = (FgenPlus) o;
        return id != null && Objects.equals(id, fgenPlus.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
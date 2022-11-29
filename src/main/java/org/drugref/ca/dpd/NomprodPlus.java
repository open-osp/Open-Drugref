package org.drugref.ca.dpd;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vig_nomprodPlus")
public class NomprodPlus {
    @Id
    @Column(name = "productID", nullable = false)
    private Integer id;

    @Column(name = "GENcode")
    private String gENcode;

    @Column(name = "productNameFrench", length = 25)
    private String productNameFrench;

    @Column(name = "strengthFrench", length = 12)
    private String strengthFrench;

    @Column(name = "formFrench", length = 12)
    private String formFrench;

    @Column(name = "productNameEnglish", length = 25)
    private String productNameEnglish;

    @Column(name = "strengthEnglish", length = 12)
    private String strengthEnglish;

    @Column(name = "formEnglish", length = 12)
    private String formEnglish;

    @Column(name = "manufacturer", length = 3)
    private String manufacturer;

    @Column(name = "discontinued")
    private Character discontinued;

    @Column(name = "legalStatusPQ")
    private Character legalStatusPQ;

    @Column(name = "supply")
    private Boolean supply;

    @Column(name = "pseudoDIN")
    private Character pseudoDIN;

    @Column(name = "RAMQcoverage")
    private String rAMQcoverage;

    @Column(name = "unitCost")
    private Double unitCost;

    @Column(name = "productType")
    private Character productType;

    @Column(name = "inInstitutionList")
    private Character inInstitutionList;

    @Column(name = "SIGcode")
    private String sIGcode;

    @Column(name = "defaultExpirationDays")
    private Integer defaultExpirationDays;

    @Column(name = "monographID", length = 3)
    private String monographID;

    @Column(name = "medicationInformationSheet")
    private String medicationInformationSheet;

    @Column(name = "auxiliarySheet")
    private Integer auxiliarySheet;

    @Column(name = "auxiliaryLabels")
    private Integer auxiliaryLabels;

    @Column(name = "administrationRoute")
    private Character administrationRoute;

    @Column(name = "defaultRouteDosageValidation")
    private String defaultRouteDosageValidation;

    @Column(name = "ATC")
    private String atc;

    @Column(name = "AHFS")
    private Integer ahfs;

    @Column(name = "CRxForm")
    private String cRxForm;

    @Column(name = "CRxRoute")
    private String cRxRoute;

    @Column(name = "CRxSIG")
    private String cRxSIG;

    @Column(name = "CRxQuantity")
    private String cRxQuantity;

    @Column(name = "usualQuantity")
    private Integer usualQuantity;

    @Column(name = "usualDuration")
    private Integer usualDuration;

    @Column(name = "productNameCapitalizedFrench", length = 25)
    private String productNameCapitalizedFrench;

    @Column(name = "lowercaseStrengthFrench", length = 12)
    private String lowercaseStrengthFrench;

    @Column(name = "lowercaseFormFrench", length = 12)
    private String lowercaseFormFrench;

    @Column(name = "productNameCapitalizedEnglish", length = 25)
    private String productNameCapitalizedEnglish;

    @Column(name = "lowercaseStrengthEnglish", length = 12)
    private String lowercaseStrengthEnglish;

    @Column(name = "lowercaseFormEnglish", length = 12)
    private String lowercaseFormEnglish;

    @Column(name = "prescriptionOptometrists")
    private Character prescriptionOptometrists;

    @Column(name = "prescriptionPodiatrist")
    private Integer prescriptionPodiatrist;

    @Column(name = "prescriptionMidwives")
    private Integer prescriptionMidwives;

    @Column(name = "prescriptionNursePractitioner")
    private String prescriptionNursePractitioner;

    @Column(name = "exceptionalMedicationCodes")
    private String exceptionalMedicationCodes;

    @Column(name = "exceptionalMedicationForms")
    private String exceptionalMedicationForms;

    @Column(name = "defaultAdministrationRouteDoseValidationMACT")
    private String defaultAdministrationRouteDoseValidationMACT;

    @Column(name = "legalStatusCanada")
    private Character legalStatusCanada;

    @Column(name = "productSpecification")
    private String productSpecification;

    @Column(name = "prescriptionNurses")
    private String prescriptionNurses;

    @Column(name = "albertaCoverage")
    private Character albertaCoverage;

    @Column(name = "britishColumbiaCoverage")
    private Character britishColumbiaCoverage;

    @Column(name = "princeEdwardIslandCoverage")
    private Character princeEdwardIslandCoverage;

    @Column(name = "manitobaCoverage")
    private Character manitobaCoverage;

    @Column(name = "newBrunswickCoverage")
    private String newBrunswickCoverage;

    @Column(name = "novaScotiaCoverage")
    private Character novaScotiaCoverage;

    @Column(name = "ontarioCoverage")
    private Character ontarioCoverage;

    @Column(name = "saskatchewanCoverage")
    private Character saskatchewanCoverage;

    @Column(name = "newfoundlandCoverage")
    private Character newfoundlandCoverage;

    @Column(name = "nonInsuredHealthBenefitsCoverage")
    private Character nonInsuredHealthBenefitsCoverage;

    @Column(name = "defaultExpirationDays2")
    private Integer defaultExpirationDays2;

    @Column(name = "tallManProductNameFrench", length = 25)
    private String tallManProductNameFrench;

    @Column(name = "tallManProductNameEnglish", length = 25)
    private String tallManProductNameEnglish;

    @Column(name = "NTPcodeOfCCDD", length = 7)
    private String nTPcodeOfCCDD;

    @Column(name = "productMonographFilenameFrench")
    private String productMonographFilenameFrench;

    @Column(name = "productMonographFilenameEnglish")
    private String productMonographFilenameEnglish;

    @Column(name = "prescriptionRespiratoryTherapists")
    private Character prescriptionRespiratoryTherapists;

    @Column(name = "marketingStatus")
    private String marketingStatus;

    @Column(name = "PTAAlert")
    private Integer pTAAlert;

    @Column(name = "prescriptionDietetician")
    private Character prescriptionDietetician;

    @Column(name = "pediatricDosage")
    private Character pediatricDosage;

    @Column(name = "defaultChronicity")
    private Character defaultChronicity;

    @ManyToMany
    @JoinTable(name = "vig_fgenPlus",
            joinColumns = @JoinColumn(name = "GENcode"),
            inverseJoinColumns = @JoinColumn(name = "GENcode"))
    private Set<FgenPlus> fgenPluses = new LinkedHashSet<>();

    public Set<FgenPlus> getFgenPluses() {
        return fgenPluses;
    }

    public void setFgenPluses(Set<FgenPlus> fgenPluses) {
        this.fgenPluses = fgenPluses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGENcode() {
        return gENcode;
    }

    public void setGENcode(String gENcode) {
        this.gENcode = gENcode;
    }

    public String getProductNameFrench() {
        return productNameFrench;
    }

    public void setProductNameFrench(String productNameFrench) {
        this.productNameFrench = productNameFrench;
    }

    public String getStrengthFrench() {
        return strengthFrench;
    }

    public void setStrengthFrench(String strengthFrench) {
        this.strengthFrench = strengthFrench;
    }

    public String getFormFrench() {
        return formFrench;
    }

    public void setFormFrench(String formFrench) {
        this.formFrench = formFrench;
    }

    public String getProductNameEnglish() {
        return productNameEnglish;
    }

    public void setProductNameEnglish(String productNameEnglish) {
        this.productNameEnglish = productNameEnglish;
    }

    public String getStrengthEnglish() {
        return strengthEnglish;
    }

    public void setStrengthEnglish(String strengthEnglish) {
        this.strengthEnglish = strengthEnglish;
    }

    public String getFormEnglish() {
        return formEnglish;
    }

    public void setFormEnglish(String formEnglish) {
        this.formEnglish = formEnglish;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Character getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(Character discontinued) {
        this.discontinued = discontinued;
    }

    public Character getLegalStatusPQ() {
        return legalStatusPQ;
    }

    public void setLegalStatusPQ(Character legalStatusPQ) {
        this.legalStatusPQ = legalStatusPQ;
    }

    public Boolean getSupply() {
        return supply;
    }

    public void setSupply(Boolean supply) {
        this.supply = supply;
    }

    public Character getPseudoDIN() {
        return pseudoDIN;
    }

    public void setPseudoDIN(Character pseudoDIN) {
        this.pseudoDIN = pseudoDIN;
    }

    public String getRAMQcoverage() {
        return rAMQcoverage;
    }

    public void setRAMQcoverage(String rAMQcoverage) {
        this.rAMQcoverage = rAMQcoverage;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Character getProductType() {
        return productType;
    }

    public void setProductType(Character productType) {
        this.productType = productType;
    }

    public Character getInInstitutionList() {
        return inInstitutionList;
    }

    public void setInInstitutionList(Character inInstitutionList) {
        this.inInstitutionList = inInstitutionList;
    }

    public String getSIGcode() {
        return sIGcode;
    }

    public void setSIGcode(String sIGcode) {
        this.sIGcode = sIGcode;
    }

    public Integer getDefaultExpirationDays() {
        return defaultExpirationDays;
    }

    public void setDefaultExpirationDays(Integer defaultExpirationDays) {
        this.defaultExpirationDays = defaultExpirationDays;
    }

    public String getMonographID() {
        return monographID;
    }

    public void setMonographID(String monographID) {
        this.monographID = monographID;
    }

    public String getMedicationInformationSheet() {
        return medicationInformationSheet;
    }

    public void setMedicationInformationSheet(String medicationInformationSheet) {
        this.medicationInformationSheet = medicationInformationSheet;
    }

    public Integer getAuxiliarySheet() {
        return auxiliarySheet;
    }

    public void setAuxiliarySheet(Integer auxiliarySheet) {
        this.auxiliarySheet = auxiliarySheet;
    }

    public Integer getAuxiliaryLabels() {
        return auxiliaryLabels;
    }

    public void setAuxiliaryLabels(Integer auxiliaryLabels) {
        this.auxiliaryLabels = auxiliaryLabels;
    }

    public Character getAdministrationRoute() {
        return administrationRoute;
    }

    public void setAdministrationRoute(Character administrationRoute) {
        this.administrationRoute = administrationRoute;
    }

    public String getDefaultRouteDosageValidation() {
        return defaultRouteDosageValidation;
    }

    public void setDefaultRouteDosageValidation(String defaultRouteDosageValidation) {
        this.defaultRouteDosageValidation = defaultRouteDosageValidation;
    }

    public String getAtc() {
        return atc;
    }

    public void setAtc(String atc) {
        this.atc = atc;
    }

    public Integer getAhfs() {
        return ahfs;
    }

    public void setAhfs(Integer ahfs) {
        this.ahfs = ahfs;
    }

    public String getCRxForm() {
        return cRxForm;
    }

    public void setCRxForm(String cRxForm) {
        this.cRxForm = cRxForm;
    }

    public String getCRxRoute() {
        return cRxRoute;
    }

    public void setCRxRoute(String cRxRoute) {
        this.cRxRoute = cRxRoute;
    }

    public String getCRxSIG() {
        return cRxSIG;
    }

    public void setCRxSIG(String cRxSIG) {
        this.cRxSIG = cRxSIG;
    }

    public String getCRxQuantity() {
        return cRxQuantity;
    }

    public void setCRxQuantity(String cRxQuantity) {
        this.cRxQuantity = cRxQuantity;
    }

    public Integer getUsualQuantity() {
        return usualQuantity;
    }

    public void setUsualQuantity(Integer usualQuantity) {
        this.usualQuantity = usualQuantity;
    }

    public Integer getUsualDuration() {
        return usualDuration;
    }

    public void setUsualDuration(Integer usualDuration) {
        this.usualDuration = usualDuration;
    }

    public String getProductNameCapitalizedFrench() {
        return productNameCapitalizedFrench;
    }

    public void setProductNameCapitalizedFrench(String productNameCapitalizedFrench) {
        this.productNameCapitalizedFrench = productNameCapitalizedFrench;
    }

    public String getLowercaseStrengthFrench() {
        return lowercaseStrengthFrench;
    }

    public void setLowercaseStrengthFrench(String lowercaseStrengthFrench) {
        this.lowercaseStrengthFrench = lowercaseStrengthFrench;
    }

    public String getLowercaseFormFrench() {
        return lowercaseFormFrench;
    }

    public void setLowercaseFormFrench(String lowercaseFormFrench) {
        this.lowercaseFormFrench = lowercaseFormFrench;
    }

    public String getProductNameCapitalizedEnglish() {
        return productNameCapitalizedEnglish;
    }

    public void setProductNameCapitalizedEnglish(String productNameCapitalizedEnglish) {
        this.productNameCapitalizedEnglish = productNameCapitalizedEnglish;
    }

    public String getLowercaseStrengthEnglish() {
        return lowercaseStrengthEnglish;
    }

    public void setLowercaseStrengthEnglish(String lowercaseStrengthEnglish) {
        this.lowercaseStrengthEnglish = lowercaseStrengthEnglish;
    }

    public String getLowercaseFormEnglish() {
        return lowercaseFormEnglish;
    }

    public void setLowercaseFormEnglish(String lowercaseFormEnglish) {
        this.lowercaseFormEnglish = lowercaseFormEnglish;
    }

    public Character getPrescriptionOptometrists() {
        return prescriptionOptometrists;
    }

    public void setPrescriptionOptometrists(Character prescriptionOptometrists) {
        this.prescriptionOptometrists = prescriptionOptometrists;
    }

    public Integer getPrescriptionPodiatrist() {
        return prescriptionPodiatrist;
    }

    public void setPrescriptionPodiatrist(Integer prescriptionPodiatrist) {
        this.prescriptionPodiatrist = prescriptionPodiatrist;
    }

    public Integer getPrescriptionMidwives() {
        return prescriptionMidwives;
    }

    public void setPrescriptionMidwives(Integer prescriptionMidwives) {
        this.prescriptionMidwives = prescriptionMidwives;
    }

    public String getPrescriptionNursePractitioner() {
        return prescriptionNursePractitioner;
    }

    public void setPrescriptionNursePractitioner(String prescriptionNursePractitioner) {
        this.prescriptionNursePractitioner = prescriptionNursePractitioner;
    }

    public String getExceptionalMedicationCodes() {
        return exceptionalMedicationCodes;
    }

    public void setExceptionalMedicationCodes(String exceptionalMedicationCodes) {
        this.exceptionalMedicationCodes = exceptionalMedicationCodes;
    }

    public String getExceptionalMedicationForms() {
        return exceptionalMedicationForms;
    }

    public void setExceptionalMedicationForms(String exceptionalMedicationForms) {
        this.exceptionalMedicationForms = exceptionalMedicationForms;
    }

    public String getDefaultAdministrationRouteDoseValidationMACT() {
        return defaultAdministrationRouteDoseValidationMACT;
    }

    public void setDefaultAdministrationRouteDoseValidationMACT(String defaultAdministrationRouteDoseValidationMACT) {
        this.defaultAdministrationRouteDoseValidationMACT = defaultAdministrationRouteDoseValidationMACT;
    }

    public Character getLegalStatusCanada() {
        return legalStatusCanada;
    }

    public void setLegalStatusCanada(Character legalStatusCanada) {
        this.legalStatusCanada = legalStatusCanada;
    }

    public String getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(String productSpecification) {
        this.productSpecification = productSpecification;
    }

    public String getPrescriptionNurses() {
        return prescriptionNurses;
    }

    public void setPrescriptionNurses(String prescriptionNurses) {
        this.prescriptionNurses = prescriptionNurses;
    }

    public Character getAlbertaCoverage() {
        return albertaCoverage;
    }

    public void setAlbertaCoverage(Character albertaCoverage) {
        this.albertaCoverage = albertaCoverage;
    }

    public Character getBritishColumbiaCoverage() {
        return britishColumbiaCoverage;
    }

    public void setBritishColumbiaCoverage(Character britishColumbiaCoverage) {
        this.britishColumbiaCoverage = britishColumbiaCoverage;
    }

    public Character getPrinceEdwardIslandCoverage() {
        return princeEdwardIslandCoverage;
    }

    public void setPrinceEdwardIslandCoverage(Character princeEdwardIslandCoverage) {
        this.princeEdwardIslandCoverage = princeEdwardIslandCoverage;
    }

    public Character getManitobaCoverage() {
        return manitobaCoverage;
    }

    public void setManitobaCoverage(Character manitobaCoverage) {
        this.manitobaCoverage = manitobaCoverage;
    }

    public String getNewBrunswickCoverage() {
        return newBrunswickCoverage;
    }

    public void setNewBrunswickCoverage(String newBrunswickCoverage) {
        this.newBrunswickCoverage = newBrunswickCoverage;
    }

    public Character getNovaScotiaCoverage() {
        return novaScotiaCoverage;
    }

    public void setNovaScotiaCoverage(Character novaScotiaCoverage) {
        this.novaScotiaCoverage = novaScotiaCoverage;
    }

    public Character getOntarioCoverage() {
        return ontarioCoverage;
    }

    public void setOntarioCoverage(Character ontarioCoverage) {
        this.ontarioCoverage = ontarioCoverage;
    }

    public Character getSaskatchewanCoverage() {
        return saskatchewanCoverage;
    }

    public void setSaskatchewanCoverage(Character saskatchewanCoverage) {
        this.saskatchewanCoverage = saskatchewanCoverage;
    }

    public Character getNewfoundlandCoverage() {
        return newfoundlandCoverage;
    }

    public void setNewfoundlandCoverage(Character newfoundlandCoverage) {
        this.newfoundlandCoverage = newfoundlandCoverage;
    }

    public Character getNonInsuredHealthBenefitsCoverage() {
        return nonInsuredHealthBenefitsCoverage;
    }

    public void setNonInsuredHealthBenefitsCoverage(Character nonInsuredHealthBenefitsCoverage) {
        this.nonInsuredHealthBenefitsCoverage = nonInsuredHealthBenefitsCoverage;
    }

    public Integer getDefaultExpirationDays2() {
        return defaultExpirationDays2;
    }

    public void setDefaultExpirationDays2(Integer defaultExpirationDays2) {
        this.defaultExpirationDays2 = defaultExpirationDays2;
    }

    public String getTallManProductNameFrench() {
        return tallManProductNameFrench;
    }

    public void setTallManProductNameFrench(String tallManProductNameFrench) {
        this.tallManProductNameFrench = tallManProductNameFrench;
    }

    public String getTallManProductNameEnglish() {
        return tallManProductNameEnglish;
    }

    public void setTallManProductNameEnglish(String tallManProductNameEnglish) {
        this.tallManProductNameEnglish = tallManProductNameEnglish;
    }

    public String getNTPcodeOfCCDD() {
        return nTPcodeOfCCDD;
    }

    public void setNTPcodeOfCCDD(String nTPcodeOfCCDD) {
        this.nTPcodeOfCCDD = nTPcodeOfCCDD;
    }

    public String getProductMonographFilenameFrench() {
        return productMonographFilenameFrench;
    }

    public void setProductMonographFilenameFrench(String productMonographFilenameFrench) {
        this.productMonographFilenameFrench = productMonographFilenameFrench;
    }

    public String getProductMonographFilenameEnglish() {
        return productMonographFilenameEnglish;
    }

    public void setProductMonographFilenameEnglish(String productMonographFilenameEnglish) {
        this.productMonographFilenameEnglish = productMonographFilenameEnglish;
    }

    public Character getPrescriptionRespiratoryTherapists() {
        return prescriptionRespiratoryTherapists;
    }

    public void setPrescriptionRespiratoryTherapists(Character prescriptionRespiratoryTherapists) {
        this.prescriptionRespiratoryTherapists = prescriptionRespiratoryTherapists;
    }

    public String getMarketingStatus() {
        return marketingStatus;
    }

    public void setMarketingStatus(String marketingStatus) {
        this.marketingStatus = marketingStatus;
    }

    public Integer getPTAAlert() {
        return pTAAlert;
    }

    public void setPTAAlert(Integer pTAAlert) {
        this.pTAAlert = pTAAlert;
    }

    public Character getPrescriptionDietetician() {
        return prescriptionDietetician;
    }

    public void setPrescriptionDietetician(Character prescriptionDietetician) {
        this.prescriptionDietetician = prescriptionDietetician;
    }

    public Character getPediatricDosage() {
        return pediatricDosage;
    }

    public void setPediatricDosage(Character pediatricDosage) {
        this.pediatricDosage = pediatricDosage;
    }

    public Character getDefaultChronicity() {
        return defaultChronicity;
    }

    public void setDefaultChronicity(Character defaultChronicity) {
        this.defaultChronicity = defaultChronicity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NomprodPlus that = (NomprodPlus) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
package fi.rikusarlin.housingserver.data;

import javax.persistence.AttributeConverter;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fi.rikusarlin.housingserver.model.HousingBenefitApplication;

@Entity
@Table(name = "housingdata")
public class HousingBenefitApplicationJsonEntity extends DateRangedEntity{
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name="case_id", nullable=false)
    HousingBenefitCaseEntity housingBenefitCase;
	
	@Basic
    @Column(name = "housingdataType")	
	HousingDataType housingDataType;

	@Column(name = "data")
	@Convert(converter = HousingBenefitApplicationJsonConverter.class)
	HousingBenefitApplication householdMember;	
	
	public HousingBenefitApplicationJsonEntity() {
	}

	public HousingBenefitCaseEntity getHousingBenefitCase() {
		return housingBenefitCase;
	}

	public void setHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase) {
		this.housingBenefitCase = housingBenefitCase;
	}

	public HousingDataType getHousingDataType() {
		return housingDataType;
	}

	public void setHousingDataType(HousingDataType housingDataType) {
		this.housingDataType = housingDataType;
	}
	
	public HousingBenefitApplication getHousingBenefitApplication() {
		return householdMember;
	}

	public void setHousingBenefitApplication(HousingBenefitApplication householdMember) {
		this.householdMember = householdMember;
	}

	@Converter
	public static class HousingBenefitApplicationJsonConverter implements AttributeConverter<HousingBenefitApplication, String> {

	    private static final ObjectMapper mapper = new ObjectMapper();
		
	    static {
	    	mapper.registerModule(new JavaTimeModule());
	    	mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    }

	    @Override
	    public String convertToDatabaseColumn(HousingBenefitApplication HousingBenefitApplication) {
	    	String result;
			try {
				result = mapper.writeValueAsString(HousingBenefitApplication);
			} catch (JsonProcessingException e) {
				result = "";
			}
	        return result;
	    }

	    @Override
	    public HousingBenefitApplication convertToEntityAttribute(String s) {
	    	HousingBenefitApplication result;
	    	try {
				result = mapper.readValue(s, HousingBenefitApplication.class);
			} catch (JsonProcessingException e) {
				result = new HousingBenefitApplication();
			}
	        return result;
	    }

	}
}

package fi.rikusarlin.housingserver.data.json;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fi.rikusarlin.housingserver.data.DateRangedEntity;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;

@Entity
@Table(name = "application_json")
public class HousingBenefitApplicationJsonEntity extends DateRangedEntity{
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name="case_id", nullable=false)
    HousingBenefitCaseJsonEntity housingBenefitCase;
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "json", name="data")
	HousingBenefitApplication householdMember;	
	
	public HousingBenefitApplicationJsonEntity() {
	}

	public HousingBenefitCaseJsonEntity getHousingBenefitCase() {
		return housingBenefitCase;
	}

	public void setHousingBenefitCase(HousingBenefitCaseJsonEntity housingBenefitCase) {
		this.housingBenefitCase = housingBenefitCase;
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

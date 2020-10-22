package fi.rikusarlin.housingserver.data.json;

import javax.persistence.AttributeConverter;
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

import fi.rikusarlin.housingserver.data.DateRangedEntity;
import fi.rikusarlin.housingserver.model.HouseholdMember;

@Entity
@Table(name = "householdmember_json")
public class HouseholdMemberJsonEntity extends DateRangedEntity{
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name="case_id", nullable=false)
    HousingBenefitCaseJsonEntity housingBenefitCase;
	
	@Column(name = "data")
	@Convert(converter = HouseholdMemberJsonConverter.class)
	HouseholdMember householdMember;	
	
	public HouseholdMemberJsonEntity() {
	}

	public HousingBenefitCaseJsonEntity getHousingBenefitCase() {
		return housingBenefitCase;
	}

	public void setHousingBenefitCase(HousingBenefitCaseJsonEntity housingBenefitCase) {
		this.housingBenefitCase = housingBenefitCase;
	}

	public HouseholdMember getHouseholdMember() {
		return householdMember;
	}

	public void setHouseholdMember(HouseholdMember householdMember) {
		this.householdMember = householdMember;
	}

	@Converter
	public static class HouseholdMemberJsonConverter implements AttributeConverter<HouseholdMember, String> {

	    private static final ObjectMapper mapper = new ObjectMapper();
		
	    static {
	    	mapper.registerModule(new JavaTimeModule());
	    	mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    }

	    @Override
	    public String convertToDatabaseColumn(HouseholdMember HouseholdMember) {
	    	String result;
			try {
				result = mapper.writeValueAsString(HouseholdMember);
			} catch (JsonProcessingException e) {
				result = "";
			}
	        return result;
	    }

	    @Override
	    public HouseholdMember convertToEntityAttribute(String s) {
	    	HouseholdMember householdMember;
	    	try {
				householdMember = mapper.readValue(s, HouseholdMember.class);
			} catch (JsonProcessingException e) {
				householdMember = new HouseholdMember();
			}
	        return householdMember;
	    }

	}
}

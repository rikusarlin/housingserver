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
import fi.rikusarlin.housingserver.model.Income;

@Entity
@Table(name = "income_json")
public class IncomeJsonEntity extends DateRangedEntity{
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name="case_id", nullable=false)
    HousingBenefitCaseJsonEntity housingBenefitCase;
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "json", name="data")
	Income income;	
	
	public IncomeJsonEntity() {
	}

	public HousingBenefitCaseJsonEntity getHousingBenefitCase() {
		return housingBenefitCase;
	}

	public void setHousingBenefitCase(HousingBenefitCaseJsonEntity housingBenefitCase) {
		this.housingBenefitCase = housingBenefitCase;
	}

	public Income getIncome() {
		return income;
	}

	public void setIncome(Income income) {
		this.income = income;
	}

	@Converter
	public static class IncomeJsonConverter implements AttributeConverter<Income, String> {

	    private static final ObjectMapper mapper = new ObjectMapper();
		
	    static {
	    	mapper.registerModule(new JavaTimeModule());
	    	mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    }

	    @Override
	    public String convertToDatabaseColumn(Income income) {
	    	String result;
			try {
				result = mapper.writeValueAsString(income);
			} catch (JsonProcessingException e) {
				result = "";
			}
	        return result;
	    }

	    @Override
	    public Income convertToEntityAttribute(String s) {
	    	Income income;
	    	try {
				income = mapper.readValue(s, Income.class);
			} catch (JsonProcessingException e) {
				income = new Income();
			}
	        return income;
	    }

	}

}

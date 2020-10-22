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
import fi.rikusarlin.housingserver.model.Expense;

@Entity
@Table(name = "expense_json")
public class ExpenseJsonEntity extends DateRangedEntity{
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name="case_id", nullable=false)
    HousingBenefitCaseJsonEntity housingBenefitCase;
	
	@Column(name = "data")
	@Convert(converter = ExpenseJsonConverter.class)
	Expense expense;	
	
	public ExpenseJsonEntity() {
	}

	public HousingBenefitCaseJsonEntity getHousingBenefitCase() {
		return housingBenefitCase;
	}

	public void setHousingBenefitCase(HousingBenefitCaseJsonEntity housingBenefitCase) {
		this.housingBenefitCase = housingBenefitCase;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	@Converter
	public static class ExpenseJsonConverter implements AttributeConverter<Expense, String> {

	    private static final ObjectMapper mapper = new ObjectMapper();
		
	    static {
	    	mapper.registerModule(new JavaTimeModule());
	    	mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    }

	    @Override
	    public String convertToDatabaseColumn(Expense expense) {
	    	String result;
			try {
				result = mapper.writeValueAsString(expense);
			} catch (JsonProcessingException e) {
				result = "";
			}
	        return result;
	    }

	    @Override
	    public Expense convertToEntityAttribute(String s) {
	    	Expense expense;
	    	try {
				expense = mapper.readValue(s, Expense.class);
			} catch (JsonProcessingException e) {
				expense = new Expense();
			}
	        return expense;
	    }

	}
}

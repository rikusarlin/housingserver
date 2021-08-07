package fi.rikusarlin.housingserver.data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Column;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import fi.rikusarlin.housingserver.validation.DateRangeChecks;
import fi.rikusarlin.housingserver.validation.Severity;
import fi.rikusarlin.housingserver.validation.ValidDateRange;
import lombok.Getter;
import lombok.Setter;

@ValidDateRange(groups=DateRangeChecks.class,payload={Severity.Error.class})
@Getter @Setter
public abstract class DateRangedEntity extends EntityClass {
	@Column(value = "startDate")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate startDate;
    
    @Column(value = "endDate")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate endDate;  
    
    public DateRangedEntity(Integer id, String createdByUser, String modifiedByUser, LocalDateTime creationTime,
			LocalDateTime modificationTime) {
		super(id, createdByUser, modifiedByUser, creationTime, modificationTime);
	}

}

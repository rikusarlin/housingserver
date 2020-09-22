package fi.rikusarlin.housingserver.data;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import fi.rikusarlin.housingserver.validation.DateRangeChecks;
import fi.rikusarlin.housingserver.validation.Severity;
import fi.rikusarlin.housingserver.validation.ValidDateRange;

@ValidDateRange(groups=DateRangeChecks.class,payload={Severity.Info.class})
@MappedSuperclass
@Validated
public abstract class DateRangedEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
	Integer id;	
	@Basic
    @Column(name = "startDate", nullable = true)	
	@DateTimeFormat(pattern="dd.MM.yyyy")
	LocalDate startDate;
	@Basic
    @Column(name = "endDate", nullable = true)	
	@DateTimeFormat(pattern="dd.MM.yyyy")
	LocalDate endDate;
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}

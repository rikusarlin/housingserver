package fi.rikusarlin.housingserver.data;

import fi.rikusarlin.housingserver.validation.ValidDateRange;
import fi.rikusarlin.housingserver.data.DateRangedEntity;
import fi.rikusarlin.housingserver.validation.OverlappingDateRanges;

@OverlappingDateRanges(dateRange1 = "dateRange", dateRange2="parentDateRange")
public class ControlledDateRangeEntity {
	@ValidDateRange
	protected DateRangedEntity dateRange;
	protected DateRangedEntity parentDateRange;
	
	public DateRangedEntity getDateRange() {
		return dateRange;
	}
	public void setDateRange(DateRangedEntity dateRange) {
		this.dateRange = dateRange;
	}
	public DateRangedEntity getParentDateRange() {
		return parentDateRange;
	}
	public void setParentDateRange(DateRangedEntity parentDateRange) {
		this.parentDateRange = parentDateRange;
	}
}

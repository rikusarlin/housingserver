package fi.rikusarlin.housingserver.data;

import fi.rikusarlin.housingserver.validation.OverlappingDateRanges;
import fi.rikusarlin.housingserver.validation.ValidDateRange;
import lombok.Getter;
import lombok.Setter;

@OverlappingDateRanges(dateRange1 = "dateRange", dateRange2="parentDateRange")
@Getter
@Setter
public class ControlledDateRangeEntity {
	@ValidDateRange
	protected DateRangedEntity dateRange;
	protected DateRangedEntity parentDateRange;
}

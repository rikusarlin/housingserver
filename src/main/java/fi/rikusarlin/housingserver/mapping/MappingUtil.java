package fi.rikusarlin.housingserver.mapping;

import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class MappingUtil {
    public static ModelMapper modelMapper = new ModelMapper();
    public static ModelMapper modelMapperInsert = new ModelMapper();
       
    public static Condition<Object, Object> skipIds = new Condition<Object, Object>() {
        public boolean applies(MappingContext<Object, Object> context) {
            return !context.getMapping().getLastDestinationProperty().getName().equals("id");
        }
    };
    
    static {
    	modelMapperInsert.getConfiguration().setPropertyCondition(skipIds);
    }

}

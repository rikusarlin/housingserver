package fi.rikusarlin.housingserver.data;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class EntityClass {
	@Id
	@NotNull(message = "Id can not be empty")
    @Column(value = "id")
	Integer id;
	
    @Column(value = "creator")
    String createdByUser; 
     
    @Column(value = "modifier")
    String modifiedByUser;
    
    @Column(value = "created")
	LocalDateTime creationTime; 
  
    @Column(value = "modified")
    LocalDateTime modificationTime;
}

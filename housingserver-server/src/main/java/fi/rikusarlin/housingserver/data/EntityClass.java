package fi.rikusarlin.housingserver.data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Validated
@Getter
@Setter
public abstract class EntityClass {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
	Integer id;	
}

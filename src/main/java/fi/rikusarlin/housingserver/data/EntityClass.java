package fi.rikusarlin.housingserver.data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.validation.annotation.Validated;

@MappedSuperclass
@Validated
public abstract class EntityClass {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
	Integer id;	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}

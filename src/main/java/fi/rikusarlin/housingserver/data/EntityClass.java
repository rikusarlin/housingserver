package fi.rikusarlin.housingserver.data;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.validation.annotation.Validated;

@MappedSuperclass
@Validated
public abstract class EntityClass {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
	Integer id;

    @Column(name = "creator", nullable = false)
    String createdByUser; 
     
    @Column(name = "modifier", nullable = false)
    String modifiedByUser;
    
    @Column(name = "created", nullable = false)
	OffsetDateTime creationTime; 
  
    @Column(name = "modified")
    OffsetDateTime modificationTime;
    
    @PrePersist
    public void prePersist() {
        String createdByUser = getUsernameOfAuthenticatedUser();
        this.createdByUser = createdByUser;
        this.modifiedByUser = createdByUser;
        this.creationTime = OffsetDateTime.now();
        this.modificationTime = OffsetDateTime.now();
    }
      
    @PreUpdate
    public void preUpdate() {
        this.modifiedByUser = getUsernameOfAuthenticatedUser();
        this.modificationTime = OffsetDateTime.now();
    }
     
    private String getUsernameOfAuthenticatedUser() {
    	/**
    	 * In real life, we would get the user from security context like this.
    	 * In this demo, we are always a static "user".
    	 * 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        } 
        return ((User) authentication.getPrincipal()).getUsername();
    	 */
    	return "SuperUser";
    }
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	public String getModifiedByUser() {
		return modifiedByUser;
	}

	public void setModifiedByUser(String modifiedByUser) {
		this.modifiedByUser = modifiedByUser;
	}

	public OffsetDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(OffsetDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public OffsetDateTime getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(OffsetDateTime modificationTime) {
		this.modificationTime = modificationTime;
	}	
}

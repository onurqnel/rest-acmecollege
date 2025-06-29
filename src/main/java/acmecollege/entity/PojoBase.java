/***************************************************************************
 * File:  PojoBase.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * @author Mike Norman
 * @date 2020 04
 * Updated by:  Group 10
 *   Lewis Brown
 *   Josh Duffenais
 *   Asher Fincham
 *   Onur Önel
 *
 *
 */
package acmecollege.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 */
//TODO PB01 - Add annotation to define this class as superclass of all entities.  Please see Week 9 lecture slides.
@MappedSuperclass
//TODO PB02 - Add annotation to place all JPA annotations on fields.
@Access(AccessType.FIELD)
//TODO PB03 - Add annotation for listener class.
@EntityListeners(PojoListener.class)
public abstract class PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "id")
	protected int id;

	
	@Version
	@JsonIgnore
	protected int version;

	
	@Basic(optional = false)
    @Column(name = "created")
	@JsonIgnore
	protected LocalDateTime created;

	@Basic(optional = false)
    @Column(name = "updated")
	@JsonIgnore
	protected LocalDateTime updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}
	
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	/**
	 * Very important:  Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		/* Enhanced instanceof - yeah!
		 * As of JDK 14, no need for additional 'silly' cast:
		    if (animal instanceof Cat) {
		        Cat cat = (Cat) animal;
		        cat.meow();
                // Other class Cat operations ...
            }
		 */
		if (obj instanceof PojoBase otherPojoBase) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return Objects.equals(this.getId(), otherPojoBase.getId());
		}
		return false;
	}
}

/***************************************************************************
 * File:  PojoCompositeListener.java Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 *
 * Updated by:  Group 10
 *   Lewis Brown
 *   Josh Duffenais
 *   Asher Fincham
 *   Onur Önel
 *
 *
 */
package acmecollege.entity;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("unused")

public class PojoCompositeListener {

	// TODO PCL01 - What annotation is used when we want to do something just before object is INSERT'd into database?e
	@PrePersist
	@JsonIgnore
	public void setCreatedOnDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		LocalDateTime now = LocalDateTime.now();
		pojoBaseComposite.setCreated(now);
		// TODO PCL02 - What member field(s) do we wish to alter just before object is INSERT'd in the database?
	}

	// TODO PCL03 - What annotation is used when we want to do something just before object is UPDATE'd into database?
	@PreUpdate
	@JsonIgnore
	public void setUpdatedDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		LocalDateTime now = LocalDateTime.now();
		pojoBaseComposite.setUpdated(now);
		// TODO PCL04 - What member field(s) do we wish to alter just before object is UPDATE'd in the database?
	}

}

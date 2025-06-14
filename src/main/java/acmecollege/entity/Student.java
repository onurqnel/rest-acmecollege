/***************************************************************************
 * File:  Student.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the STUDENT database table in the acmecollege schema
 * </br>
 * </br>
 * 
 * Note: This is <b>NOT</b> the same Student entity from Lab 1/Assignment
 * 1/Assignment 2. </br>
 * This entity does <b>NOT</b> have member fields email, phoneNumber, level, or
 * program. </br>
 * 
 */
@SuppressWarnings("unused")
//TODO ST01 - Add the missing annotations.
//TODO ST02 - Do we need a mapped super class? If so, which one?
@Entity
@NamedQuery(name = Student.ALL_STUDENTS_QUERY_NAME, query = "SELECT s FROM Student s")
@NamedQuery(name = Student.QUERY_STUDENT_BY_ID, query = "SELECT s FROM Student s where s.id = :param1")
public class Student extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String ALL_STUDENTS_QUERY_NAME = "Student.findAll";
	public static final String QUERY_STUDENT_BY_ID = "Student.findAllByID";

	@Basic(optional = false)
    @Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Basic(optional = false)
    @Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "owner")
	@JsonManagedReference("student-membership-card")
	private Set<MembershipCard> membershipCards = new HashSet<>();

	// TODO ST06 - Add annotations for 1:M relation. Changes should not cascade.
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "student")
	@JsonManagedReference("student-course-registration")
	private Set<CourseRegistration> courseRegistrations = new HashSet<>();

	public Student() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonIgnore
	public Set<MembershipCard> getMembershipCards() {
		return membershipCards;
	}

	public void setMembershipCards(Set<MembershipCard> membershipCards) {
		this.membershipCards = membershipCards;
	}

	@JsonIgnore
	public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

	// Inherited hashCode/equals is sufficient for this entity class

}

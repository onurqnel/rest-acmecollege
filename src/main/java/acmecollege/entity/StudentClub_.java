package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-11-30T11:58:20.966-0500")
@StaticMetamodel(StudentClub.class)
public class StudentClub_ extends PojoBase_ {
	public static volatile SingularAttribute<StudentClub, String> name;
	public static volatile SetAttribute<StudentClub, ClubMembership> clubMemberships;
}

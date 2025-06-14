/*
 * Dec 2023
 *  Created and
 *  * Updated by:  Group 10
 *   Lewis Brown
 *   Josh Duffenais
 *   Asher Fincham
 *   Onur Önel
 *
 *
 */
package acmecollege.rest.resource;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.Course;
import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.USER_ROLE;


import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

    @EJB
    private ACMECollegeService service;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getAllCourses() {
        List<Course> courses = service.getAll(Course.class, Course.ALL_COURSES_QUERY);
        return Response.ok(courses).build();
    }

    @GET
    @Path("/{courseId}")
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getCourseById(@PathParam("courseId") int courseId) {
        Course course = service.getById(Course.class, Course.FIND_BY_ID_QUERY, courseId);
        if (course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(course).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response createCourse(Course newCourse) {
        Course addedCourse = service.persistCourse(newCourse);
        return Response.status(Response.Status.CREATED).entity(addedCourse).build();
    }

    @DELETE
    @Path("/{courseId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteCourse(@PathParam("courseId") int courseId) {
        service.deleteCourseById(courseId);
        return Response.noContent().build();
    }

    //Update method (PUT) can be added
}

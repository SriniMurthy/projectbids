package com.srini.projectbids.resources;

import com.srini.projectbids.api.MarketPlaceErrorMapper;
import com.srini.projectbids.api.NewProjectRequest;
import com.srini.projectbids.api.ProjectResponse;
import com.srini.projectbids.core.Project;


import com.srini.projectbids.db.ProjectDAO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import com.fasterxml.jackson.annotation.JsonProperty;


import io.dropwizard.hibernate.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

import com.google.inject.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.srini.projectbids.api.NewBidRequest;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Path("/projects")


public class MarketPlaceResource {

    @Inject
    private ProjectDAO projectDAO;

    public MarketPlaceResource(ProjectDAO dao) {
        this.projectDAO = dao;
    }

    @GET
    @Path("/projects/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getProject( @DefaultValue("") @PathParam("id") Long id) {

        Project project = projectDAO.findById(id);
        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Exception("Project  :"+id + "does not exist.")).build();
        }
        Gson gson = new Gson();
        String str = gson.toJson(project);
        log.debug("Project :"+str);
        log.debug("GSON PROJECT IS::"+str);
        return Response.ok().entity(new ProjectResponse(Response.Status.OK,"Found project " +project.getName(), project)).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getProjects() {
        List<Project> projectList = projectDAO.findAll();
        if (projectList == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Exception("There are no projects in the database.")).build();
        }
        return  Response.ok().entity(new ProjectResponse(Response.Status.OK, "Successfully returned project list",projectList)).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response addProject(@Valid  NewProjectRequest newProject){

        List<Project> existingProject = projectDAO.findByName(newProject.getName());

        if (!existingProject.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).entity(new Exception(newProject.getName() + " already exists.")).build();
        }

        Project proj = new Project();
        createNewProject(newProject,proj);


        Long id = projectDAO.create(proj);

        proj.setProjectID(id);
        return Response.ok().entity(new ProjectResponse(Response.Status.OK,"Added Project to database ", proj)).build();
    }

    private void createNewProject(@Valid NewProjectRequest newProject,Project proj) {
        log.debug("In Create New Project");
        proj.setName(newProject.getName());
        proj.setDescription(newProject.getDescription());
        proj.setMaxBudget(newProject.getBudget());
        proj.setStartBidDate(new Date());
        proj.setOwnerID(newProject.getOwnerID());
        proj.setEndBidDate( newProject.getEndBidDate() );
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProject(@Valid NewProjectRequest newProject){

        Project existingProject = projectDAO.findById(newProject.getProjectID());
        if (existingProject == null) {
            return Response.status(Response.Status.CONFLICT).entity(new Exception(newProject.getName() + " does not exist.")).build();
        }
        Project proj = new Project();
        proj.setName(newProject.getName());
        proj.setDescription(newProject.getDescription());
        proj.setMaxBudget(newProject.getBudget());
        proj.setStartBidDate(new Date());
        proj.setOwnerID(newProject.getOwnerID());
        proj.setEndBidDate( newProject.getEndBidDate() );

        Long projID =  projectDAO.update(proj);
        proj.setProjectID(projID);
        return Response.ok().entity(new ProjectResponse(Response.Status.OK,"Success", proj)).build();
    }
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response deleteProject( @DefaultValue("") @PathParam("id") Long id) {
        log.debug("ID IS :"+id);
        Project project = projectDAO.findById(id);
        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Exception("Project  :"+id + "does not exist.")).build();
        }
        try {
            projectDAO.deleteProject( project );
        }catch (Throwable th) {
            log.error( "Error deleting project",th );
            return Response.status(Response.Status.BAD_REQUEST).entity(new ProjectResponse(Response.Status.BAD_REQUEST,th.getLocalizedMessage(), project)).build();
        }
        return Response.ok().entity(new ProjectResponse(Response.Status.OK, "Successfully deleted project:" +project.getName(),project)).build();
    }

    @GET
    @Path("/openprojects/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAllOpenProjects() {
        List<Project> projectList = projectDAO.findOpenProjects();
        if (projectList == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Exception("There are no open projects in the database.")).build();
        }

        return Response.ok().entity(new ProjectResponse(Response.Status.OK,"Found list of open projects ", projectList)).build();
    }



}

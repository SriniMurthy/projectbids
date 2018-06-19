package com.srini.projectbids.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.srini.projectbids.core.Project;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.core.Response.Status;
import java.util.List;

@ToString
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ProjectResponse {

    Project project;
    List<Project> projectList;
    String message;

    private Integer status = null;

     public ProjectResponse(Status ok,String message,List<Project> projList) {
        this.status = status;
        this.projectList = projList;
        this.message = message;
    }
    public ProjectResponse(Status ok,String message,Project proj) {
        this.status = status;
        this.project = proj;
        this.message = message;
    }
}


package com.srini.projectbids.db;

import com.srini.projectbids.core.Project;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import  org.hibernate.Session;
import com.srini.projectbids.core.Bid;

import java.sql.*;
import java.util.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectDAO extends AbstractDAO<Project> {
    private static final Logger log = LoggerFactory.getLogger(ProjectDAO.class);


    public ProjectDAO(SessionFactory sessionFactory) {
           super( sessionFactory );
           try {
                log.debug(" In ProjectDAO Constructor");
           }catch (Throwable th) {
               th.printStackTrace(System.err);
               log.debug("Error In Project DAO creation");
           }
       }

        public Project findById(long id) {
            return  get(id);
        }


    public List<Project> findByName(String name) {
        StringBuilder builder = new StringBuilder("%");
        builder.append(name).append("%");
        return list(
                namedQuery("com.srini.projectbids.core.Project.findByName")
                        .setParameter("name", builder.toString())
        );
    }

    public List<Project> findOpenProjects() {

        return list(
                namedQuery("com.srini.projectbids.core.Project.findOpenProjects")

        );
    }

    public Long create(Project proj) {
            return persist(proj).getProjectID();
    }

    public List<Project> findAll() {
        return list(namedQuery("com.srini.projectbids.core.Project.findAll"));
    }

    public Long update(Project proj) {

           Project aProj = null;
            try {

                aProj = super.persist( proj );//.getProjectID()

            }catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        return aProj.getProjectID();
    }
    public boolean deleteProject(Project project) {
       try {
             removeAllBids(project);
             currentSession().delete( project );
             return true;
       }catch (Throwable th) {
           th.printStackTrace(System.out);
           return false;
       }
    }
    private void removeAllBids(Project project) throws Exception {
        Set<Bid> bids = project.getBids();
        for (Bid aBid: bids) {
           currentSession().delete(aBid);
        }
        project.setBids(null);
    }
}

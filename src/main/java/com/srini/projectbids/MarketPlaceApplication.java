package com.srini.projectbids;

import com.srini.projectbids.db.ProjectDAO;
import com.srini.projectbids.db.BidDAO;


import com.srini.projectbids.resources.MarketPlaceResource;
import com.srini.projectbids.resources.BiddingResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
//import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;
//import org.skife.jdbi.v2.DBI;
import com.srini.projectbids.core.Project;
import com.srini.projectbids.core.Bid;
import org.hibernate.Session;
import java.net.URLClassLoader;
import java.net.URL;


import io.dropwizard.db.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarketPlaceApplication extends Application<MarketPlaceConfiguration> {
    private static final Logger log = LoggerFactory.getLogger(MarketPlaceApplication.class);
   static {
       System.setProperty("java.io.tmpdir", "C:\\temp");

   }
    public static void main(final String[] args) throws Exception {
        new MarketPlaceApplication().run(args);
    }

    @Override
    public String getName() {
        return "The Big GrandOld MarketPlace";
    }

    @Override
    public void initialize(Bootstrap<MarketPlaceConfiguration> bootstrap) {
       try {
            bootstrap.addBundle(hibernate);
            log.debug(" I have added Hibernate bundle");

        }catch(Throwable th) {
           th.printStackTrace(System.err);
           log.debug("Error In BootStrap Initialize");
       }

    }

    private final HibernateBundle<MarketPlaceConfiguration> hibernate =
            new HibernateBundle<MarketPlaceConfiguration>(Project.class,Bid.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(MarketPlaceConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };


    @Override
    public void run(final MarketPlaceConfiguration configuration,
                    final Environment environment) {

            try{
                final ProjectDAO dao = new ProjectDAO(hibernate.getSessionFactory());
                final BidDAO bidDAO = new BidDAO(hibernate.getSessionFactory());

                Class.forName("org.h2.Driver");

                environment.jersey().register(new MarketPlaceResource(dao));
                environment.jersey().register(new BiddingResource(bidDAO,dao));


            }catch (Exception ex) {
                 ex.printStackTrace(System.err);
                throw new RuntimeException("Unknown Error while running MarketPlace Application",ex);
            }

        }
    }



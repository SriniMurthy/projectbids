package com.srini.projectbids.db;

import com.srini.projectbids.core.Project;
import com.srini.projectbids.core.Bid;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.sql.*;
import java.util.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidDAO extends AbstractDAO<Bid> {

    private static final Logger log = LoggerFactory.getLogger(BidDAO.class);


    public BidDAO(SessionFactory sessionFactory) {
           super( sessionFactory );
           try {
                log.debug(" In BidDAO Constructor");
           }catch (Throwable th) {
               log.error("Error In BidDAO  creation",th);
           }
       }

       public Optional<Bid> findById(long id) {
            return Optional.ofNullable(get(id));
        }


    public List<Bid> findByName(String name) {
        StringBuilder builder = new StringBuilder("%");
        builder.append(name).append("%");
        return list(
                namedQuery("com.srini.projectbids.core.Bid.findByBidderName")
                        .setParameter("name", builder.toString())
        );
    }
    public List<Bid> findBidsByProject(Long projectID) {

        return list(
                namedQuery("com.srini.projectbids.core.Bid.findAllBidsForProject")
                        .setParameter("projectID", projectID)
        );
    }

    public Long create(Bid bid) {
            return persist(bid).getBidID();
    }

    public List<Bid> findAllBids() {

           return list(namedQuery("com.srini.projectbids.core.Bid.findAllBids"));
    }

    public Long update(Bid bid) {
        return persist(bid).getBidID();
    }

    public boolean deleteBid(Bid bid) {
        try {
            currentSession().delete( bid );
            return true;
        }catch (Throwable th) {
            log.error("Error deleting Bid:"+bid.toString(),th);
            return false;
        }
    }
}

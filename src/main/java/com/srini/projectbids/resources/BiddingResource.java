package com.srini.projectbids.resources;

import javax.ws.rs.Path;

import com.srini.projectbids.core.Project;
import com.srini.projectbids.db.ProjectDAO;
import com.srini.projectbids.db.BidDAO;
import com.srini.projectbids.api.NewBidRequest;
import com.srini.projectbids.api.BiddingResponse;
import com.srini.projectbids.core.Bid;
import com.srini.projectbids.db.BidDAO;
import com.srini.projectbids.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

import java.util.Optional;


import com.google.inject.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.srini.projectbids.api.NewBidRequest;
import com.srini.projectbids.api.NewProjectRequest;
import com.srini.projectbids.api.LateBidderException;
import com.srini.projectbids.api.MarketPlaceException;

import com.srini.projectbids.core.Bid;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.hibernate.*;

@Path("/projectbids")


public class BiddingResource {

    @Inject
    private BidDAO bidDAO;

    @Inject
    private ProjectDAO projectDAO;


    private static final Logger log = LoggerFactory.getLogger( BiddingResource.class );

    public BiddingResource(BidDAO dao) {
        this.bidDAO = dao;
    }

    public BiddingResource(BidDAO dao,ProjectDAO pDAO) {
        projectDAO = pDAO;
        bidDAO = dao;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/createBid")
    public Response addBid(NewBidRequest newBid) throws LateBidderException {
        log.debug( "Attempting to find project with ID:" + newBid.getProjectID() );
        Project existingProject = projectDAO.findById( newBid.getProjectID() );
        //Project eprj = null;
        if (existingProject == null) {
            log.error( "Unable to find project with ID:" + newBid.getProjectID() );

            return Response.status( Response.Status.CONFLICT ).entity( new Exception( newBid.getProjectID() + " does not exist." ) ).build();
        } else {
            // eprj = existingProject.get();
            log.debug( existingProject + " is the existing project" );
        }


        Bid bid = new Bid();

        try {
            createNewBid( newBid,bid,existingProject );
            log.debug( "Bidding Project date:" + bid.getBidDate() +
                    " and proj bid ends on:" + existingProject.getEndBidDate() );

            if (bid.getBidDate().after( existingProject.getEndBidDate() )) {
                throw new LateBidderException( "You are late in this bid!" );
            }

            Long id = bidDAO.create( bid );

            bid.setBidID( id );
            existingProject.addBid( bid );
            projectDAO.update( existingProject );
        } catch (Throwable th) {
            log.error( "Error creating new Bid for project",th );
            return Response.status(Response.Status.BAD_REQUEST).entity( new BiddingResponse( Response.Status.BAD_REQUEST,th.getLocalizedMessage(),bid ) ).build();
        }
        return Response.ok().entity( new BiddingResponse( Response.Status.OK,"",bid ) ).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("{projectID}")
    public Response getAllBidsForProject(@PathParam("projectID") Long projectID) throws MarketPlaceException {
        try {
            log.debug( "Attempting to find Bids for project with ID:" + projectID );
            List<Bid> bidders = bidDAO.findBidsByProject( projectID );
            if (bidders.isEmpty()) {
                return Response.status( Response.Status.NOT_FOUND ).entity( new BiddingResponse( Response.Status.NOT_FOUND,"No bids found for this project",bidders ) ).build();
            }

            Project existingProject = projectDAO.findById( projectID );
            if (existingProject == null) {
                return Response.status( Response.Status.NOT_FOUND ).entity( new BiddingResponse( Response.Status.NOT_FOUND,"No projects found.",bidders ) ).build();
            }
            log.debug( "Number of Bids for Project " + projectID + "is :" + existingProject.getBids().size() );
            return Response.ok().entity( new BiddingResponse( Response.Status.OK,"",bidders ) ).build();
        } catch (Exception ex) {
            throw new MarketPlaceException( ex );
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/name/getBids/{name}")
    public Response getAllBidsForBidder(@PathParam("name") String name) {
        log.debug( "Attempting to find Bids in entire marketspace" );
        List<Bid> bidders = bidDAO.findByName( name );
        log.debug( "Number of BIDS In Marketplace is :" + bidders.size() );
        return Response.ok().entity( new BiddingResponse( Response.Status.OK,"",bidders ) ).build();
    }

    @GET

    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/")
    public Response getAllBids() {
        log.debug( "Attempting to find Bids in entire marketspace" );
        List<Bid> bidders = bidDAO.findAllBids();
        log.debug( "Number of BIDS In Marketplace is :" + bidders.size() );
        return Response.ok().entity( new BiddingResponse( Response.Status.OK,"",bidders ) ).build();
    }

    private void createNewBid(@Valid NewBidRequest newBid,Bid bid,Project project) {
        bid.setBidAmount( newBid.getBidAmount() );
        bid.setBidDate( new Date() );
        bid.setBidderName( newBid.getBidderName() );

        bid.setProject( project );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/getCheapestBid/{id}")

    public Response getCheapestBid(@PathParam("id") Long id) {
        List<Bid> bidders = bidDAO.findBidsByProject( id );
        Bid cheapestBid = BidCalculator.findLeastBid( bidders );
        return Response.ok().entity( new BiddingResponse( Response.Status.OK,"",cheapestBid ) ).build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response deleteBid(@DefaultValue("") @PathParam("id") Long id) {
        log.debug( "ID IS :" + id );
        Optional<Bid> bid = bidDAO.findById( id );

        if (Optional.of( bid ) == null) {
            return Response.status( Response.Status.NOT_FOUND ).entity( new Exception( "Bid  :" + id + "does not exist." ) ).build();
        }

        bidDAO.deleteBid( bid.get() );
        return Response.ok().entity( new BiddingResponse( Response.Status.OK,"Bid with ID:" + id +
                " has been deleted",bid.get() ) ).build();
    }
}

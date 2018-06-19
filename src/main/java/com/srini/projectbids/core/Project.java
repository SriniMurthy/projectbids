package com.srini.projectbids.core;

import com.srini.projectbids.util.BidCalculator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.FetchType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import java.io.Serializable;

import javax.persistence.JoinTable;


import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.fasterxml.jackson.annotation.*;

@Entity(name="Project")
@Table(name="projects")

@NamedQueries({
        @NamedQuery(name = "com.srini.projectbids.core.Project.findAll",
                query = "select pr from Project pr"),
        @NamedQuery(name = "com.srini.projectbids.core.Project.findByName",
                query = "select pr from Project pr "
                        + "where pr.name like :name "),
        @NamedQuery(name = "com.srini.projectbids.core.Project.findOpenProjects",
                query = "select pr from Project pr "
                        + "where pr.endBidDate > CURDATE()")
})
public class Project implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Project.class);

    /**
     * project name
     */
    @Column(name = "name")
    protected String name;

    @Column(name = "description")
    protected String description;

    @Column(name = "maxbudget")
    protected Double maxBudget;

    @Column (name = "ownerID")
    protected String ownerID;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="project_id")
    protected Long id;


    protected Double leastBidAmount;

    @Column(name="startbiddate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    protected Date startBidDate;

    @Column (name="endbiddate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    protected Date endBidDate;

    @JsonManagedReference
    @OneToMany(fetch=FetchType.EAGER,mappedBy="project",cascade=CascadeType.ALL,orphanRemoval=true )
    protected Set <Bid> bids = Collections.synchronizedSet(new HashSet<Bid>());

    public void setBids(Set<Bid> bids ){
        this.bids = bids;
    }


    public Long getProjectID() {
        return id;
    }

    public void setProjectID(Long id) {
        this.id = id;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid pBid) throws Exception {
        Date currentDate = new Date();
        log.debug("Adding Bid :"+ pBid.getBidderName() + " ::"+ pBid.getBidID() +
                " to project "+ this.getName());
        if (currentDate.after(endBidDate)){
            throw new Exception("Project closed for bidding now");
        }
        bids.add(pBid);
    }

    public void removeBid(Bid pBid) throws Exception {
        bids.remove(pBid);
        pBid.setProject(null);
    }
    public void removeAllBids() {
        bids = null;
    }

    public Double getLeastBidAmount() {
        return leastBidAmount;
    }

    public void setLeastBidAmount(Double leastBidAmount) {
        this.leastBidAmount = leastBidAmount;
    }

    public Project(String name, String description, Double maxBudget, Date endDate){

        bids =  Collections.synchronizedSet(new HashSet<Bid>());

        this.name = name;
        this.description = description;
        this.maxBudget = maxBudget;
        this.endBidDate = endDate;
    }
    public Project() {
        log.debug("Default Constructor");
        if (bids == null) {
            bids = Collections.synchronizedSet( new HashSet<Bid>() );
        }
    }


    public String getName() {
        return name;
    }

    public Double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }

    public void setEndBidDate(Date endBidDate) {
        setEndDate(endBidDate);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartBidDate() {
        return startBidDate;
    }

    public void setStartBidDate(Date startBidDate) {
        this.startBidDate = startBidDate;
    }



    public Date getEndBidDate() {
        return endBidDate;
    }

     public void setEndDate(Date endDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss" );
            dateFormat.setLenient( false );
            this.endBidDate = dateFormat.parse( endDate.toString() );
        }catch (ParseException pEx) {
            this.endBidDate = endDate;
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        if (!(o instanceof Project )) return false;
        Project proj = (Project) o;

        return ((name.equals(proj.getName())) &&
                (this.id == (proj.getProjectID())) &&
                (this.getOwnerID().equals(proj.getOwnerID())) );
    }

    @Override
    public int hashCode() {
        int retVal = getProjectID().hashCode();
        retVal = 31 * retVal +  name.hashCode() + ownerID.hashCode() + endBidDate.hashCode();
        return retVal;
    }
    public Bid getLeastBid() {
        Bid leastBid =  BidCalculator.findLeastBid(bids);
        if (leastBid != null)
           leastBidAmount = leastBid.bidAmount;
        return leastBid;
    }

}

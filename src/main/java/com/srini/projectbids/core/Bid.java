package com.srini.projectbids.core;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.util.List;
import javax.persistence.JoinTable;
import javax.persistence.SequenceGenerator;

import javax.persistence.TableGenerator;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity (name="Bid")
@Table(name="bids")

@NamedQueries({
        @NamedQuery(name = "com.srini.projectbids.core.Bid.findAllBidsForProject",
                query = "select bd from Bid bd where bd.project.id = :projectID"),

        @NamedQuery(name = "com.srini.projectbids.core.Bid.findAllBids",
                query = "select bd from Bid bd"),
        @NamedQuery(name = "com.srini.projectbids.core.Bid.findByBidderName",
                query = "select bd from Bid bd where bidderName like :name"),
})
public class Bid implements Comparable<Bid>, Serializable {

    @Column(name="biddate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    protected Date bidDate;

    @Column(name="bidamount")
    protected Double bidAmount;

    @Column (name="name")
    protected String bidderName;

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    protected Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "project_id",nullable=false)
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Bid )) return false;

        Bid aBid = (Bid)o;
        return
          (this.getBidID().equals(aBid.getBidID()));
    }

    @Override
    public int hashCode() {
        int retVal = 31;
        try {
              retVal = bidderName.hashCode() + this.bidAmount.hashCode() +
		        (this.getProject().getName().hashCode()) + 31;
            return retVal;
        }catch (Exception ex) {
            ex.printStackTrace(System.err);

        }
        return retVal;
    }


    public Date getBidDate() {
        return bidDate;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String name) {
        this.bidderName = name;
    }



    public Long getBidID() {
        return id;
    }

    public void setBidID(Long id) {
        this.id = id;
    }

    @Override
    public int compareTo(Bid o) {
        Double bidAmount = this.bidAmount;
        Double otherAmount = o.bidAmount;
        return (int) (bidAmount - otherAmount);
    }
}


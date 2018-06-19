package com.srini.projectbids.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dropwizard.validation.ValidationMethod;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@Data
@Slf4j
@Accessors(chain = true)
public class NewBidRequest {

    @NotEmpty
    @JsonProperty
    private String bidderName;

    @NotNull
    @JsonProperty
    private double bidAmount;

    @NotNull
    @JsonProperty
    private Long projectID;


    public String getBidderName() {
        return bidderName;
}

    Long bidID;

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Long getProjectID() {
        return projectID;
    }
   /* public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }*/

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public Long getBidID() {
        return bidID;
    }

    public void setBidID(Long bidID) {
        this.bidID = bidID;
    }


}


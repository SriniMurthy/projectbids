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
import com.fasterxml.jackson.annotation.JsonFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Date;

@Data
@Slf4j
@Accessors(chain = true)
public class NewProjectRequest {
    public static final String VALID_NAME_REGEXP = "[@A-Za-z0-9.,'-_ ]*";

    @NotEmpty
    @JsonProperty
    @Length(max = 100)
    private String name;

    @NotEmpty
    @JsonProperty
    @Length(max = 250)
    private String description;

    @NotNull
    @JsonProperty
    private Double budget;

    @NotEmpty
    @JsonProperty
    private String ebdString;

    @JsonProperty
    private Date endBidDate;


    @NotEmpty
    @JsonProperty
    @Length(max = 200)
    private String ownerID;

    Long projectID;


    public String getName() {
        return name;
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

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public Date getEndBidDate() {
        return endBidDate;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    @ValidationMethod(message = "name must consist only of letters, numbers, dashes, underscores, apostrophes, spaces, periods, asterisks, atsigns, and commas")
    @JsonIgnore
    public boolean isValidName() {
        return name.matches(VALID_NAME_REGEXP);
    }

    @ValidationMethod (message =" bid End date must be valid!")
    public boolean isValidBidEndDate() {
           boolean retVal = true;
            try {
                DateFormat dateFormat = new SimpleDateFormat( "MM-dd-yyyy" );
                dateFormat.setLenient( false );
                this.endBidDate = dateFormat.parse( ebdString );
            }catch (ParseException pEx) {
                log.error("Error validating end of bid date",pEx);
                retVal = false;
            }
            return retVal;
        }

}


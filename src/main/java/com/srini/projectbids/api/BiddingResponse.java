package com.srini.projectbids.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.srini.projectbids.core.Bid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.io.Serializable;

@ToString
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class BiddingResponse implements Serializable {

    Bid bid;
    List<Bid> bidList;
    String statusMessage;

    private Integer status = null;

    public BiddingResponse(Status ok,String message,List<Bid> bidList) {
        this.status = status;
        this.bidList = bidList;
        this.statusMessage = message;
    }
    public BiddingResponse(Status ok,String message,Bid bid) {
        this.status = status;
        this.bid = bid;
        this.statusMessage = message;
    }
}


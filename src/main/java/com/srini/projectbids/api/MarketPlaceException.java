package com.srini.projectbids.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.srini.projectbids.core.Bid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@ToString
@Getter
@Setter
@JsonInclude(Include.NON_NULL)

@Provider
public class  MarketPlaceException extends Exception implements
        ExceptionMapper<MarketPlaceException>
{
    private static final long serialVersionUID = 1L;

    public MarketPlaceException(Throwable th) {
        super(th);
    }

    public MarketPlaceException(String string) {
        super(string);
    }

    @Override
    public Response toResponse(MarketPlaceException exception)
    {
        return Response.status(Status.BAD_REQUEST).entity(exception.getMessage())
                .type("application/json").build();
    }
}



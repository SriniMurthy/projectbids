package com.srini.projectbids.api;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.*;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class MarketPlaceErrorMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        Response.Status type = getStatusType(ex);

        Error error = new Error(
                ex.getMessage(), ex);

        return Response.status( type.getStatusCode() )
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Response.Status  getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return Status.fromStatusCode( ((WebApplicationException)ex).getResponse().getStatus() );
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}


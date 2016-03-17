/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vargovcik.peter.iot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import vargovcik.peter.iot.db.DBHandler;
import vargovcik.peter.iot.model.IOTNode;
import vargovcik.peter.iot.model.TempData;

/**
 * REST Web Service
 *
 * @author Peter Vargovcik
 */
@Path("TempData")
public class TempDataResource {

    @Context
    private UriInfo context;
    private DBHandler dBHandler = DBHandler.getInstance();

    /**
     * Creates a new instance of TempDataResource
     */
    public TempDataResource() {
    }

    /**
     * Retrieves representation of an instance of vargovcik.peter.iot.TempDataResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})    
    public Response getXml() {
       ArrayList<TempData> nodelist = dBHandler.getTempDataList();

        GenericEntity<List<TempData>> output = new GenericEntity<List<TempData>>(nodelist) {
        };

        return Response
                .status(Response.Status.OK)
                .entity(output)
                .build();
    }
    
    @GET
    @Path("/{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})    
    public Response getTemperaturesFromTol(@PathParam("from") String from, @PathParam("to") String to) {
        
        SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");
        Date dateFrom,dateTo;
        
        try {
            dateFrom = ft.parse(from);
            dateTo = ft.parse(to);
        } catch (ParseException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        }
        
        
       ArrayList<TempData> nodelist = dBHandler.getTemperatures(dateFrom, dateTo);

        GenericEntity<List<TempData>> output = new GenericEntity<List<TempData>>(nodelist) {
        };

        return Response
                .status(Response.Status.OK)
                .entity(output)
                .build();
    }
    
    @GET
    @Path("/temperaturesNow")
    @Produces({"application/json", "application/xml"})
    public Response currentTemperatures() {

       ArrayList<TempData> nodelist = dBHandler.getCurentTempDataList();

        GenericEntity<List<TempData>> output = new GenericEntity<List<TempData>>(nodelist) {
        };

        return Response
                .status(Response.Status.OK)
                .entity(output)
                .build();

    }
    
    /*
    @GET
    @Path("/shutter/{direction}")
    @Produces({"application/json", "application/xml"})
    public Response shutter(@PathParam("direction") String direction) {

        String shutterURL = "http://192.168.1.10:9010/?rollerdoor/";
        int responseCode = -1;

        if (direction.equals("up") || direction.equals("down")) {

            responseCode = sendCommandToURL(shutterURL + direction);

            if (responseCode != -1 || responseCode != 0) {
                return Response
                        .status(Response.Status.ACCEPTED)
                        .entity("{\"responseCode\" : " + responseCode + " }")
                        .build();
            } else {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }

        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

    }
    

    /**
     * PUT method for updating or creating an instance of TempDataResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
    
    @POST    
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addTempData(TempData tempData) {
        if (tempData != null) {
            String success = dBHandler.addTempData(tempData.getZone(), tempData.getTemperature());

            if (success.equals("ok")) {
                return Response
                        .status(Response.Status.OK)
                        .entity(tempData)
                        .build();
            } else {
                return Response
                        .status(Response.Status.CONFLICT)
                        .entity(success)
                        .build();
            }
        } else {
            return Response
                    .noContent()
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }
    
     @POST    
    @Path("/test")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response testPost(TempData tempData) {
        if (tempData != null) {

            return Response
                        .status(Response.Status.OK)
                        .entity(tempData)
                        .build();
        } else {
            return Response
                    .noContent()
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }
    
     @GET
    @Path("/test/{from}/{to}")
    @Produces({"application/json", "application/xml"})
    public Response test(@PathParam("from") String from, @PathParam("to") String to) {

        TempData testResponseObj = new TempData(from+":"+to, 23.75f, new Date());
        return Response
                .status(Response.Status.OK)
                .entity(testResponseObj)
                .build();

    }
}

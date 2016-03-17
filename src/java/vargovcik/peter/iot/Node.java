/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vargovcik.peter.iot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import vargovcik.peter.iot.db.DBHandler;
import vargovcik.peter.iot.model.IOTNode;

/**
 * REST Web Service
 *
 * @author Peter Vargovcik
 */
@Path("node")
public class Node {

    @Context
    private UriInfo context;
    private DBHandler dBHandler = DBHandler.getInstance();

    /**
     * Creates a new instance of Node
     */
    public Node() {
    }

    /**
     * Retrieves representation of an instance of vargovcik.peter.iot.Node
     *
     * @param nodeId
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/{nodeId}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getXml(@PathParam("nodeId") String nodeId) {
        int nodeid = 0;
        
        try{
            nodeid = Integer.parseInt(nodeId);
        }
        catch(NumberFormatException e){
            dBHandler.log(e.getMessage(), e);
            return Response
                 .noContent()
                .status(Response.Status.BAD_REQUEST)                    
                .build();
        }
                
        IOTNode nodeToFind = new IOTNode();
        nodeToFind.setId(nodeid);
        
        IOTNode nodeObj = dBHandler.getIOTNode(nodeToFind);
        
        if(nodeObj != null){
            return Response
                    .status(Response.Status.OK)
                    .entity(nodeObj)
                    .build();
        }
        else{
            return Response
                 .noContent()
                .status(Response.Status.NOT_FOUND)                    
                .build();
        }
    }

    /**
     * PUT method for updating or creating an instance of Node
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response putXml(IOTNode node) {
        if (node != null) {
            //String success = dBHandler.addNode(node);
            
            return Response
                        .status(Response.Status.OK)
                        .entity(node)
                        .build();
            
//            if (success.equals("ok")) {
//                return Response
//                        .status(Response.Status.OK)
//                        .entity(node)
//                        .build();
//            } else {
//                return Response
//                        .status(Response.Status.CONFLICT)
//                        .entity(success)
//                        .build();
//            }
        } else {
            return Response
                    .noContent()
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response addIOTNode(IOTNode node) {
        if (node != null) {
            
            if(dBHandler.nodeExists(node.getId())){
                return Response
                        .status(Response.Status.NOT_MODIFIED)
                        .entity(dBHandler.getIOTNode(node))
                        .build();
            }
            
            dBHandler.log("New Node Request : "+node.toString());
            
            IOTNode resNode = dBHandler.addIOTNode(node);
            
            if (resNode != null) {
                return Response
                        .status(Response.Status.CREATED)
                        .entity(resNode)
                        .build();
            } else {
                return Response
                        .status(Response.Status.CONFLICT)
                        .entity(node)
                        .build();
            }
        } else {
            return Response
                    .noContent()
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }
}

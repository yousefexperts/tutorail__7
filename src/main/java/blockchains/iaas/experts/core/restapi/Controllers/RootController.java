package blockchains.iaas.experts.core.restapi.Controllers;

import blockchains.iaas.experts.core.restapi.model.response.LinkCollectionResponse;
import blockchains.iaas.experts.core.restapi.util.UriUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/")
public class RootController {
    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getSubscriptions(){
        LinkCollectionResponse response = new LinkCollectionResponse();
        response.add(UriUtil.generateSubResourceLink(uriInfo, "submit-transaction", false, "submit-transaction"));
        response.add(UriUtil.generateSubResourceLink(uriInfo, "receive-transaction", false, "receive-transaction"));
        response.add(UriUtil.generateSubResourceLink(uriInfo, "receive-transactions", false, "receive-transactions"));
        response.add(UriUtil.generateSubResourceLink(uriInfo, "ensure-transaction-state", false, "ensure-transaction-state"));
        response.add(UriUtil.generateSubResourceLink(uriInfo, "detect-orphaned-transaction", false, "detect-orphaned-transaction"));

        return Response.ok(response).build();
    }
}

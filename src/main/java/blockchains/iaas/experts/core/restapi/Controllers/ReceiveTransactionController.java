package blockchains.iaas.experts.core.restapi.Controllers;

import blockchains.iaas.experts.core.management.BlockchainManager;
import blockchains.iaas.experts.core.management.model.SubscriptionType;
import blockchains.iaas.experts.core.restapi.model.request.ReceiveTransactionsRequest;
import blockchains.iaas.experts.core.restapi.util.UriUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("receive-transaction")
public class ReceiveTransactionController extends SubscriptionController{

    @GET
    //@Operation(summary="gets a list of all receive transaction subscriptions")
    public Response get(){
        return getSubscriptions(SubscriptionType.RECEIVE_TRANSACTION, uriInfo);
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response receiveTransaction(ReceiveTransactionsRequest request){
        final BlockchainManager manager = new BlockchainManager();
        manager.receiveTransaction(request.getSubscriptionId(), request.getFrom(), request.getBlockchainId(),
                request.getWaitFor(), request.getEpUrl());

        return Response.created(UriUtil.generateSubResourceURI(this.uriInfo, request.getSubscriptionId(), false))
                .build();
    }
}

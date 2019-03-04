package blockchains.iaas.experts.core.restapi.Controllers;

import blockchains.iaas.experts.core.management.BlockchainManager;
import blockchains.iaas.experts.core.management.model.SubscriptionType;
import blockchains.iaas.experts.core.restapi.model.request.ReceiveTransactionsRequest;
import blockchains.iaas.experts.core.restapi.util.UriUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("receive-transactions")
public class ReceiveTransactionsController extends SubscriptionController {

    @GET
    public Response get(){
        return getSubscriptions(SubscriptionType.RECEIVE_TRANSACTIONS, uriInfo);
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response receiveTransaction(ReceiveTransactionsRequest request){
        final BlockchainManager manager = new BlockchainManager();
        manager.receiveTransactions(request.getSubscriptionId(), request.getFrom(), request.getBlockchainId(),
                request.getWaitFor(), request.getEpUrl());

        return Response.created(UriUtil.generateSubResourceURI(this.uriInfo, request.getSubscriptionId(), false))
                .build();
    }
}

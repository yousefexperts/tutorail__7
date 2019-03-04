package blockchains.iaas.experts.core.restapi.Controllers;

import blockchains.iaas.experts.core.management.BlockchainManager;
import blockchains.iaas.experts.core.management.model.SubscriptionType;
import blockchains.iaas.experts.core.restapi.model.request.EnsureTransactionStateRequest;
import blockchains.iaas.experts.core.restapi.util.UriUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("ensure-transaction-state")
public class EnsureTransactionStateController extends SubscriptionController {

    @GET
    public Response get(){
        return getSubscriptions(SubscriptionType.ENSURE_TRANSACTION_STATE, uriInfo);
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response ensureTransactionState(EnsureTransactionStateRequest request){
        final BlockchainManager manager = new BlockchainManager();
        manager.ensureTransactionState(request.getSubscriptionId(), request.getTxId(), request.getBlockchainId(),
                request.getWaitFor(), request.getEpUrl());

        return Response.created(UriUtil.generateSubResourceURI(this.uriInfo, request.getSubscriptionId(), false))
                .build();
    }
}

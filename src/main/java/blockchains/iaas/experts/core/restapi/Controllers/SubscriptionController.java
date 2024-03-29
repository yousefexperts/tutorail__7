package blockchains.iaas.experts.core.restapi.Controllers;

import blockchains.iaas.experts.core.management.SubscriptionManager;
import blockchains.iaas.experts.core.management.model.Subscription;
import blockchains.iaas.experts.core.management.model.SubscriptionType;
import blockchains.iaas.experts.core.restapi.model.response.LinkCollectionResponse;
import blockchains.iaas.experts.core.restapi.util.UriUtil;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

public abstract class SubscriptionController {

    @Context
    protected UriInfo uriInfo;

    Response getSubscriptions(final SubscriptionType type, final UriInfo uriInfo) {
        final SubscriptionManager manager = SubscriptionManager.getInstance();
        final Collection<String> subscriptions = manager.getAllSubscriptionIdsOfType(type);
        final LinkCollectionResponse response = new LinkCollectionResponse();

        for (final String subscriptionId : subscriptions) {
            response.add(UriUtil.generateSubResourceLink(uriInfo, subscriptionId, false, "self"));
        }

        return Response.ok(response).build();
    }

    private Response getSubscription(final String subscriptionId){
        final SubscriptionManager manager = SubscriptionManager.getInstance();
        final Subscription subscription = manager.getSubscription(subscriptionId);

        if(subscription != null) {
            final LinkCollectionResponse response = new LinkCollectionResponse();
            response.add(UriUtil.generateSelfLink(uriInfo));
            return Response.ok(response).build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    Response removeSubscription(final String subscriptionId){
        final SubscriptionManager manager = SubscriptionManager.getInstance();
        final Subscription subscription = manager.getSubscription(subscriptionId);

        if(subscription!= null){
            subscription.unsubscribe();
            // removing the subscription from the list is done elsewhere (in the BlockchainManager)
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/{subscriptionId}")
    public Response removeSubscriptionOperation(@PathParam("subscriptionId")final String subscriptionId){
        return this.removeSubscription(subscriptionId);
    }

    @GET
    @Path("/{subscriptionId}")
    public Response getSubscriptionDetails(@PathParam("subscriptionId")final String subscriptionId){
        return this.getSubscription(subscriptionId);
    }
}

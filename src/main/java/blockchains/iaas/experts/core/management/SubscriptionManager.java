package blockchains.iaas.experts.core.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blockchains.iaas.experts.core.management.model.Subscription;
import blockchains.iaas.experts.core.management.model.SubscriptionType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class SubscriptionManager {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionManager.class);
    private static SubscriptionManager instance = null;
    private Map<String, Subscription> subscriptions = Collections.synchronizedMap(new HashMap<>());

    private SubscriptionManager() {

    }

    public static SubscriptionManager getInstance() {
        if (instance == null) {
            instance = new SubscriptionManager();
        }

        return instance;
    }

    public void createSubscription(String subscriptionId, Subscription subscription) {
        if (this.subscriptions.containsKey(subscriptionId)) {
            log.error("subscription-id <{}> already exists!");
        } else {
            this.subscriptions.put(subscriptionId, subscription);
        }
    }

    public Subscription getSubscription(String subscriptionId){
        if(this.subscriptions.containsKey(subscriptionId)){
            return this.subscriptions.get(subscriptionId);
        }else{
            log.info("trying to retrieve a non-existent subscription: <{}>! null is returned");
            return null;
        }
    }

    public void removeSubscription(String subscriptionId){
        if(this.subscriptions.containsKey(subscriptionId)){
            this.subscriptions.remove(subscriptionId);
        }else{
            log.info("trying to remove a non-existent subscription: <{}>! nothing is removed");
        }
    }

    public Collection<String> getAllSubscriptionIdsOfType(SubscriptionType type){
        return this.subscriptions
                .keySet()
                .stream()
                .filter(subscriptionId -> this.subscriptions.get(subscriptionId).getType()==type)
                .collect(Collectors.toList());
    }

}

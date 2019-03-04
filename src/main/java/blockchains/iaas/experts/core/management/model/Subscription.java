package blockchains.iaas.experts.core.management.model;


public abstract class Subscription {
    private SubscriptionType type;

    public Subscription(final SubscriptionType type){
        this.type = type;
    }

    public SubscriptionType getType() {
        return type;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public abstract void unsubscribe();
}

package blockchains.iaas.experts.core.management.model;




public class ObservableSubscription extends  Subscription {
    private rx.Subscription subscription;

    public ObservableSubscription(rx.Subscription subscription, SubscriptionType type) {
        super(type);
        this.subscription = subscription;
    }

    public rx.Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(rx.Subscription subscription) {
        this.subscription = subscription;
    }

    public void unsubscribe() {
        this.subscription.unsubscribe();
    }
}

package blockchains.iaas.experts.core.management.model;

import java.util.concurrent.CompletableFuture;

import blockchains.iaas.experts.core.exceptions.ManualUnsubscriptionException;


public class CompletableFutureSubscription<T> extends Subscription {
    private CompletableFuture<T> future;

    public CompletableFutureSubscription(CompletableFuture<T> future, SubscriptionType type) {
        super(type);
        this.future = future;
    }

    public CompletableFuture<T> getFuture() {
        return future;
    }

    public void setFuture(CompletableFuture<T> future) {
        this.future = future;
    }

    @Override
    public void unsubscribe() {
        future.completeExceptionally(new ManualUnsubscriptionException());
    }
}

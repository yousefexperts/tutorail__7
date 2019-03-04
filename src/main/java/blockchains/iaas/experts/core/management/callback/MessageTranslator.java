package blockchains.iaas.experts.core.management.callback;

import blockchains.iaas.experts.core.model.Transaction;
import blockchains.iaas.experts.core.model.TransactionState;
import blockchains.iaas.experts.core.restapi.model.response.CallbackMessage;


public abstract class MessageTranslator {
    public abstract CallbackMessage convert(String subscriptionId, boolean isErrorMessage, Transaction transaction, TransactionState state);

    public CallbackMessage convert(String subscriptionId, boolean isErrorMessage, Transaction transaction){
        return this.convert( subscriptionId, isErrorMessage, transaction, transaction.getState());
    }

    public CallbackMessage convert(String subscriptionId, boolean isErrorMessage, TransactionState state){
        return this.convert( subscriptionId, isErrorMessage, null, state);
    }
}

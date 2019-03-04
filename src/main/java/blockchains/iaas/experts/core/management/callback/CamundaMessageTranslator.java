package blockchains.iaas.experts.core.management.callback;

import blockchains.iaas.experts.core.model.Transaction;
import blockchains.iaas.experts.core.model.TransactionState;
import blockchains.iaas.experts.core.restapi.model.response.CallbackMessage;
import blockchains.iaas.experts.core.restapi.model.response.CamundaMessage;
import blockchains.iaas.experts.core.restapi.model.response.CamundaVariable;

import java.util.Map;


public class CamundaMessageTranslator extends MessageTranslator {
    @Override
    public CallbackMessage convert(String subscriptionId, boolean isErrorMessage, Transaction transaction, TransactionState state) {
        final CamundaMessage result = new CamundaMessage();
        final String processInstnaceId = subscriptionId.substring(subscriptionId.indexOf('_') + 1);
        final String msgName = (isErrorMessage) ? "error_" : "message_" + subscriptionId;
        result.setMessageName(msgName);
        result.setProcessInstanceId(processInstnaceId);
        Map<String, CamundaVariable> variables = result.getProcessVariables();

        variables.put("status", new CamundaVariable(state.toString(), "String"));

        if (transaction != null) {
            variables.put("from", new CamundaVariable(transaction.getFrom(), "String"));
            variables.put("to", new CamundaVariable(transaction.getTo(), "String"));
            variables.put("value", new CamundaVariable(transaction.getValueAsString(), "Long"));
            variables.put("transactionId", new CamundaVariable(transaction.getTransactionHash(), "String"));

            if (transaction.getBlock() != null) { //it could be null if we are accepting transactions with 0 confirmations
                variables.put("blockId", new CamundaVariable(transaction.getBlock().getHash(), "String"));
                variables.put("blockNumber", new CamundaVariable(String.valueOf(transaction.getBlock().getNumberAsLong()), "Long"));
            } else {
                variables.put("blockId", new CamundaVariable("", "String"));
                variables.put("blockNumber", new CamundaVariable("-1", "Long"));
            }
        }

        return result;
    }


}

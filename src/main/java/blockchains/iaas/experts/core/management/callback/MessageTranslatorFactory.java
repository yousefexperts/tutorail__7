package blockchains.iaas.experts.core.management.callback;


public class MessageTranslatorFactory {
    // TODO pick up the implementation based on some input (config file, or message input)
    public static MessageTranslator getCallbackAdapter(){
        return new CamundaMessageTranslator();
    }
}

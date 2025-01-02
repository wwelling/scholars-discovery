package edu.tamu.scholars.discovery.messaging.listener;

import static edu.tamu.scholars.discovery.AppConstants.EMPTY_BYTE_ARRAY;
import static edu.tamu.scholars.discovery.AppConstants.ID;
import static org.springframework.messaging.simp.stomp.StompCommand.RECEIPT;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscriptionListener implements ApplicationListener<SessionSubscribeEvent> {

    private final AbstractSubscribableChannel clientOutboundChannel;

    public SubscriptionListener(AbstractSubscribableChannel clientOutboundChannel) {
        this.clientOutboundChannel = clientOutboundChannel;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        Message<byte[]> message = event.getMessage();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getReceipt() != null) {
            StompHeaderAccessor receipt = StompHeaderAccessor.create(RECEIPT);
            receipt.setReceipt(accessor.getReceipt());
            receipt.setSubscriptionId(accessor.getSubscriptionId());
            receipt.setDestination(accessor.getDestination());
            receipt.setSessionId(accessor.getSessionId());
            receipt.setUser(accessor.getUser());
            receipt.addNativeHeader(ID, accessor.getFirstNativeHeader(ID));
            clientOutboundChannel.send(MessageBuilder.createMessage(EMPTY_BYTE_ARRAY, receipt.getMessageHeaders()));
        }
    }

}
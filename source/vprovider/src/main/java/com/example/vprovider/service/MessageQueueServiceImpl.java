package com.example.vprovider.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@AllArgsConstructor
@Slf4j
@Service
public class MessageQueueServiceImpl implements MessageQueueService{

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void push(String topicName, Object message) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("Fail to push message {} to {}, reason {}",
                        topicName, buildStringMessage(message), throwable.getLocalizedMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringTSendResult) {
                log.info("Sent message {} to {} with offset {}",
                        buildStringMessage(message), topicName, stringTSendResult.getRecordMetadata().offset());
            }
        });
    }

    private String buildStringMessage(Object message) {
        String messageString = "";
        try {
            messageString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Fail to convert message to String, reason {}", e.getLocalizedMessage());
        }
        return messageString;
    }
}

package com.example.vhandler.service;

import com.example.vhandler.dto.VoucherHistoryDto;
import com.example.vhandler.repository.VoucherHistoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class MessageQueueListener {

    private ObjectMapper objectMapper;
    private VoucherHistoryRepository voucherHistoryRepository;
    private SmsCacheServiceImpl smsCacheService;

    @KafkaListener(topics = "${kafka.topic.voucher-code}", groupId = "${kafka.group.voucher-code}")
    public void listenOnVoucherCode(ConsumerRecord message) {
        try {
            // receive voucher from kafka
            log.info("Receive kafka message {}", message.toString());
            VoucherHistoryDto voucherCodeDetail = objectMapper.readValue(message.value().toString(),
                    VoucherHistoryDto.class);

            // push to send-sms cache in redis, then cron job will run to
            // 1. update code to db
            // 2. send sms
            smsCacheService.save(voucherCodeDetail.getId(), voucherCodeDetail);
        } catch (JsonProcessingException e) {
            log.error("Fail to parse message {}, cause by {}", message.toString(), e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}

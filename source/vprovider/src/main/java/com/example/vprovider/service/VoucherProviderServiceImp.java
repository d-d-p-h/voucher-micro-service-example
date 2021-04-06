package com.example.vprovider.service;

import com.example.vprovider.dto.VoucherCodeDetail;
import com.example.vprovider.event.VoucherCodeEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Random;

@Service
@Slf4j
public class VoucherProviderServiceImp implements VoucherProviderService {

    @Value("${kafka.topic.voucher-code}")
    private String voucherCodeTopic;

    private final MessageQueueService messageQueueService;
    private final VoucherCodeEventPublisher voucherCodeEventPublisher;

    public VoucherProviderServiceImp(MessageQueueService messageQueueService, VoucherCodeEventPublisher voucherCodeEventPublisher) {
        this.messageQueueService = messageQueueService;
        this.voucherCodeEventPublisher = voucherCodeEventPublisher;
    }

    @Override
    public String generateVoucher(String phoneNumber, String voucherType, long id) {
        /*
           get a random number
               number % 2 == 0 -> simulate timeout
                   else        -> return immediate
         */
        int random = new Random().nextInt();
        if (random % 2 == 0) {
            log.info("sleep for 40s");
            try {
                Thread.sleep(40000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        VoucherCodeDetail voucherCodeDetail = VoucherCodeDetail.builder()
                .id(id)
                .phoneNumber(phoneNumber)
                .voucherType(voucherType)
                .build();

        return generateVoucherCode(voucherCodeDetail);
    }

    @Override
    public void bulkGenerateVoucher(Collection<VoucherCodeDetail> voucherCodeDetails) {
        voucherCodeEventPublisher.publishVoucherCodeEvent(voucherCodeDetails);
    }

    @Override
    public String generateVoucherCode(VoucherCodeDetail voucherCodeDetail) {
        String voucherCode = voucherCodeDetail.getVoucherType() + "-" + System.currentTimeMillis();
        voucherCodeDetail.setVoucherCode(voucherCode);
        messageQueueService.push(voucherCodeTopic, voucherCodeDetail);
        return voucherCode;
    }
}

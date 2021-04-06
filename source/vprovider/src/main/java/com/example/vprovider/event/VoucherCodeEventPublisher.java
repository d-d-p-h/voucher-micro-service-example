package com.example.vprovider.event;

import com.example.vprovider.dto.VoucherCodeDetail;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class VoucherCodeEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishVoucherCodeEvent(Collection<VoucherCodeDetail> voucherCodeDetailList) {
        VoucherCodeEvent voucherCodeEvent = new VoucherCodeEvent(this, voucherCodeDetailList);
        applicationEventPublisher.publishEvent(voucherCodeEvent);
    }
}

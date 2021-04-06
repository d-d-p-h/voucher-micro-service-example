package com.example.vprovider.event;

import com.example.vprovider.dto.VoucherCodeDetail;
import com.example.vprovider.service.VoucherProviderService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class VoucherCodeEventListener implements ApplicationListener<VoucherCodeEvent> {

    private VoucherProviderService voucherProviderService;

    @Override
    public void onApplicationEvent(VoucherCodeEvent voucherCodeEvent) {
        Collection<VoucherCodeDetail> voucherCodeDetailList = voucherCodeEvent.getVoucherCodeDetailList();
        if (!CollectionUtils.isEmpty(voucherCodeDetailList)) {
            voucherCodeDetailList.stream()
                    .map(voucherCodeDetail -> voucherProviderService.generateVoucherCode(voucherCodeDetail))
                    .collect(Collectors.toList());
        }
    }
}

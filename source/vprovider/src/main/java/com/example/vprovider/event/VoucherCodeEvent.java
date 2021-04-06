package com.example.vprovider.event;

import com.example.vprovider.dto.VoucherCodeDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;

@Getter
@Setter
public class VoucherCodeEvent extends ApplicationEvent {

    private Collection<VoucherCodeDetail> voucherCodeDetailList;

    public VoucherCodeEvent(Object source, Collection<VoucherCodeDetail> voucherCodeDetails) {
        super(source);
        this.voucherCodeDetailList = voucherCodeDetails;
    }
}

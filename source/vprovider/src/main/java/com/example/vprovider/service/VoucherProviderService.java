package com.example.vprovider.service;

import com.example.vprovider.dto.VoucherCodeDetail;

import java.util.Collection;

public interface VoucherProviderService {

    String generateVoucher(String phoneNumber, String voucherType, long id);

    void bulkGenerateVoucher(Collection<VoucherCodeDetail> voucherCodeDetail);

    String generateVoucherCode(VoucherCodeDetail voucherCodeDetail);
}

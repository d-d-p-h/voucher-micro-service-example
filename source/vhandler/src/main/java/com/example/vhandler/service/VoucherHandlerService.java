package com.example.vhandler.service;

import com.example.vhandler.dto.PageableResponseDto;
import com.example.vhandler.dto.VoucherHistoryDto;
import com.example.vhandler.dto.VoucherInfo;
import com.example.vhandler.entity.VoucherHistory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface VoucherHandlerService {

    String generateVoucherCode(String phoneNumber, String voucherType) throws Exception;

    List<VoucherHistory> findAllVoucher(VoucherInfo info) throws Exception;

    PageableResponseDto<VoucherHistoryDto> findAllVoucherPageable(VoucherInfo info) throws Exception;

    String generateVoucherCodes(Collection<VoucherHistoryDto> voucherHistoryDtoList) throws Exception;

    String bulkGenerateVoucherCode(Collection<VoucherHistoryDto> voucherHistoryDtos) throws Exception;

    List<VoucherHistory> bulkUpdateVoucher(Map<Long, VoucherHistoryDto> voucherHistoryDtoMap);
}

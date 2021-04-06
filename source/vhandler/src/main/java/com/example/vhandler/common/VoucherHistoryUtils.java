package com.example.vhandler.common;

import com.example.vhandler.dto.VoucherHistoryDto;
import com.example.vhandler.entity.VoucherHistory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VoucherHistoryUtils {

    public static List<VoucherHistory> mapToVoucherHistory(Collection<VoucherHistoryDto> voucherHistoryDtoCol) {
        if (!CollectionUtils.isEmpty(voucherHistoryDtoCol)) {
            return  voucherHistoryDtoCol.stream()
                    .map(dto -> VoucherHistory.builder()
                    .phoneNumber(dto.getPhoneNum())
                    .voucherType(dto.getVoucherType()).build())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static List<VoucherHistoryDto> mapToVoucherHistoryDto(Collection<VoucherHistory> voucherHistoryCollection) {
        if (!CollectionUtils.isEmpty(voucherHistoryCollection)) {
            return voucherHistoryCollection.stream()
                    .map(his -> VoucherHistoryDto.builder()
                    .id(his.getId())
                    .phoneNum(his.getPhoneNumber())
                    .voucherCode(his.getVoucherCode())
                    .voucherType(his.getVoucherType())
                    .build()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}

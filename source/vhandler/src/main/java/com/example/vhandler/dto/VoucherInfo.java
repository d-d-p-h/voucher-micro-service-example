package com.example.vhandler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherInfo {

    private String phoneNum;
    private String voucherType;
    private Integer pageNum;
    private Integer pageSize;
}

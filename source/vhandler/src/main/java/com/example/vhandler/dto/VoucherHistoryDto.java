package com.example.vhandler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoucherHistoryDto implements Serializable {

    private long id;
    private String phoneNum;
    private String voucherCode;
    private String voucherType;
}

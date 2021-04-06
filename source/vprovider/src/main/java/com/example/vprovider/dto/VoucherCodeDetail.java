package com.example.vprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherCodeDetail {

    private long id;
    private String phoneNumber;
    private String voucherCode;
    private String voucherType;

}

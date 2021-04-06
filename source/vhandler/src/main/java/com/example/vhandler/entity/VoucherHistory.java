package com.example.vhandler.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "voucher_hist")
public class VoucherHistory extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "v_hist_seq", sequenceName = "v_hist_seq")
    @Column(name = "id")
    private Long id;

    @NotNull(message = "phone number is required")
    @Column(name = "phone_num")
    private String phoneNumber;

    @Column(name = "voucher_cd")
    private String voucherCode;

    @NotNull(message = "voucher type is required")
    @Column(name = "voucher_typ")
    private String voucherType;

    @Column(name = "received_by")
    private String receivedBy;
}

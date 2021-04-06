package com.example.vprovider.controller;

import com.example.vprovider.dto.VoucherCodeDetail;
import com.example.vprovider.service.VoucherProviderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/vprovider")
public class VoucherProviderController {

    private final VoucherProviderService voucherProviderService;

    @GetMapping(path = "/generate")
    public ResponseEntity<String> generateVoucher(@NotBlank @RequestParam(name = "pno") String phoneNumber,
                                            @NotBlank @RequestParam(name = "typ") String voucherType,
                                            @NotBlank @RequestParam(name = "id") long id) {
        return ResponseEntity.of(Optional.of(voucherProviderService.generateVoucher(phoneNumber,voucherType, id)));
    }

    @PostMapping(path = "/generate/bulk")
    public ResponseEntity<String> bulkGenerateVoucher(@RequestBody Collection<VoucherCodeDetail> voucherCodeDetail) {
        voucherProviderService.bulkGenerateVoucher(voucherCodeDetail);
        return ResponseEntity.of(Optional.of("Success"));
    }
}

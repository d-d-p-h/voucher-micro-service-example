package com.example.vhandler.controller;

import com.example.vhandler.common.ResponseBody;
import com.example.vhandler.dto.PageableResponseDto;
import com.example.vhandler.dto.VoucherHistoryDto;
import com.example.vhandler.dto.VoucherInfo;
import com.example.vhandler.entity.VoucherHistory;
import com.example.vhandler.service.VoucherHandlerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/voucher")
@Slf4j
@AllArgsConstructor
@Validated
public class VoucherHandlerController {

    private final VoucherHandlerService vHandlerService;

    @GetMapping(path = "/code")
    public ResponseEntity<ResponseBody<String>> generateVoucherCode(@RequestParam(name = "pno") @NotBlank String phoneNumber,
                                                                    @RequestParam(name = "typ") @Size(min = 1, max = 20) String voucherType) throws Exception {
        return ResponseEntity.of(Optional.of(new ResponseBody<>(HttpStatus.OK.value(), "Success",
                vHandlerService.generateVoucherCode(phoneNumber, voucherType))));
    }

    @PostMapping(path = "/filter")
    public ResponseEntity<ResponseBody<List<VoucherHistory>>> findAllVoucher(@RequestBody(required = false) VoucherInfo info) throws Exception {
        return  ResponseEntity.of(Optional.of(new ResponseBody<>(HttpStatus.OK.value(), "Success",
                vHandlerService.findAllVoucher(info))));
    }

    @PostMapping(path = "/filter/paging")
    public ResponseEntity<ResponseBody<PageableResponseDto<VoucherHistoryDto>>> filterVoucher(@Valid @RequestBody VoucherInfo info) throws Exception {
        return  ResponseEntity.of(Optional.of(new ResponseBody<>(HttpStatus.OK.value(), "Success",
                vHandlerService.findAllVoucherPageable(info))));
    }

    @PostMapping(path = "/bulkgen")
    public ResponseEntity<ResponseBody<String>> bulkGenerateVoucherCode(@RequestBody Collection<VoucherHistoryDto> voucherHistoryDtos) throws Exception {
        return ResponseEntity.of(Optional.of(new ResponseBody<>(HttpStatus.OK.value(), "Success",
                vHandlerService.generateVoucherCodes(voucherHistoryDtos))));
    }
}

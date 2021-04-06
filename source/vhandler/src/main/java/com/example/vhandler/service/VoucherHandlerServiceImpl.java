package com.example.vhandler.service;

import com.example.vhandler.common.AppConstant;
import com.example.vhandler.common.CommonUtils;
import com.example.vhandler.common.VoucherHistoryUtils;
import com.example.vhandler.dto.PageableResponseDto;
import com.example.vhandler.dto.VoucherHistoryDto;
import com.example.vhandler.dto.VoucherInfo;
import com.example.vhandler.entity.VoucherHistory;
import com.example.vhandler.repository.VoucherHistoryRepository;
import com.example.vhandler.repository.VoucherSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VoucherHandlerServiceImpl implements VoucherHandlerService{

    private final VoucherHistoryRepository voucherHistoryRepo;
    private final RestTemplate restTemplate;
    private final RetryCacheServiceImpl retryCacheService;
    private final ObjectMapper objectMapper;

    @Value("${api.voucher.provider.url}")
    private String voucherProviderUrl;
    @Value("${api.voucher.provider.end-point.generate}")
    private String generateEndPoint;
    @Value("${api.voucher.provider.end-point.generate-bulk}")
    private String bulkGenerateEndPoint;

    public VoucherHandlerServiceImpl(VoucherHistoryRepository voucherHistoryRepo, RestTemplate restTemplate, RetryCacheServiceImpl retryCacheService, ObjectMapper objectMapper) {
        this.voucherHistoryRepo = voucherHistoryRepo;
        this.restTemplate = restTemplate;
        this.retryCacheService = retryCacheService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateVoucherCode(String phoneNumber, String voucherType) throws Exception {
        log.info("Start generateVoucherCode for {} with type {}", phoneNumber, voucherType);
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new Exception("Invalid phone number");
        }

        VoucherHistory voucherHistory = voucherHistoryRepo.saveAndFlush(
                    VoucherHistory.builder()
                    .phoneNumber(phoneNumber)
                    .voucherType(voucherType)
                    .build());

        Map<String, Object> params = new HashMap<>();
        params.put("pno", phoneNumber);
        params.put("typ", voucherType);
        params.put("id", voucherHistory.getId());
        String apiUrl = CommonUtils.buildParamUrl(voucherProviderUrl, generateEndPoint, params);
        try {
            ResponseEntity<String> result = restTemplate.getForEntity(apiUrl, String.class);
            if (HttpStatus.OK.value() == result.getStatusCodeValue()) {
                log.info("Receive voucher code for phone number {}", phoneNumber);
                voucherHistory.setVoucherCode(result.getBody());
                voucherHistory.setReceivedBy(AppConstant.RECEIVED_BY_API);
                voucherHistoryRepo.saveAndFlush(voucherHistory);
            }
        } catch (Exception e) {
            if (e.getCause() instanceof  SocketTimeoutException) {
                log.warn("Timeout when generating voucher code for phone number {} - type {} - uid {}",
                        phoneNumber, voucherType, voucherHistory.getId());
                return AppConstant.PROCESSING_MSG;
            } else {
                log.error("Fail to generate voucher for phone number {} - type {} - uid {}, cause by {}",
                        phoneNumber, voucherType, voucherHistory.getId(), e.getLocalizedMessage());
                // push to retry-voucher-code cache to try again
                VoucherHistoryDto voucherHistoryDto = VoucherHistoryDto.builder()
                        .id(voucherHistory.getId())
                        .phoneNum(voucherHistory.getPhoneNumber())
                        .voucherType(voucherHistory.getVoucherType())
                        .voucherCode(voucherHistory.getVoucherCode())
                        .build();
                retryCacheService.save(voucherHistory.getId(), voucherHistoryDto);
                throw e;
            }
        }
        return voucherHistory.getVoucherCode();
    }

    @Override
    public List<VoucherHistory> findAllVoucher(VoucherInfo info) throws Exception{
        log.info("Start findAllVoucher for {}", objectMapper.writeValueAsString(info));
        return voucherHistoryRepo.findAll(buildSpecification(info));
    }

    @Override
    public PageableResponseDto<VoucherHistoryDto> findAllVoucherPageable(VoucherInfo info) throws Exception{
        log.info("Start to findAllVoucherPageable for {}", objectMapper.writeValueAsString(info));
        VoucherSpecification spec = buildSpecification(info);
        PageRequest paging = PageRequest.of(info.getPageNum() - 1, info.getPageSize());

        Page<VoucherHistory> page = voucherHistoryRepo.findAll(spec, paging);
        PageableResponseDto<VoucherHistoryDto> result =  new PageableResponseDto<>();
        result.setData(VoucherHistoryUtils.mapToVoucherHistoryDto(page.getContent()));
        result.setTotal(page.getTotalElements());

        return result;
    }

    @Override
    public String generateVoucherCodes(Collection<VoucherHistoryDto> voucherHistoryDtoList) throws Exception {
        log.info("Start generateVoucherCodes");
        try {
            List<VoucherHistory> voucherHistories = VoucherHistoryUtils.mapToVoucherHistory(voucherHistoryDtoList);
            voucherHistoryRepo.saveAll(voucherHistories);
            voucherHistoryRepo.flush();

            return bulkGenerateVoucherCode(VoucherHistoryUtils.mapToVoucherHistoryDto(voucherHistories));
        } catch (Exception e) {
            log.error("Fail to generateVoucherCodes due to {}", e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public String bulkGenerateVoucherCode(Collection<VoucherHistoryDto> voucherHistoryDtoList) throws Exception {
        String voucherHistoryJsonList = objectMapper.writeValueAsString(voucherHistoryDtoList);

        log.info("Start bulkGenerateVoucherCode for {}", voucherHistoryJsonList);

        String url = CommonUtils.buildParamUrl(voucherProviderUrl, bulkGenerateEndPoint, null);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(voucherHistoryJsonList, httpHeaders);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url,
                    HttpMethod.POST, httpEntity, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Fail to bulkGenerateVoucherCode due to {}", e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public List<VoucherHistory> bulkUpdateVoucher(Map<Long, VoucherHistoryDto> voucherHistoryDtoMap) {
        log.info("Start bulkUpdateVoucher");
        List<VoucherHistory> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(voucherHistoryDtoMap)) {
            List<VoucherHistory> voucherHistories = voucherHistoryRepo.findAllByIdIn(voucherHistoryDtoMap.keySet());
            if (!CollectionUtils.isEmpty(voucherHistories)) {
                for (VoucherHistory voucherHistory : voucherHistories) {
                    if (StringUtils.isBlank(voucherHistory.getReceivedBy())) {
                        VoucherHistoryDto voucherHistoryDto = voucherHistoryDtoMap.get(voucherHistory.getId());
                        voucherHistory.setReceivedBy(AppConstant.RECEIVED_BY_MQ);
                        voucherHistory.setVoucherCode(voucherHistoryDto.getVoucherCode());
                        result.add(voucherHistory);
                    }
                }
                result = voucherHistoryRepo.saveAll(result);
                voucherHistoryRepo.flush();
            }
        }
        return result;
    }

    private VoucherSpecification buildSpecification(VoucherInfo info) {
        return info != null ? VoucherSpecification.builder()
                .phoneNumber(info.getPhoneNum())
                .voucherType(info.getVoucherType())
                .build() : new VoucherSpecification();
    }
}

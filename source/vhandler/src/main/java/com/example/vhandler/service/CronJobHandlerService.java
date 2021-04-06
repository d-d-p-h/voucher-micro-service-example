package com.example.vhandler.service;

import com.example.vhandler.dto.VoucherHistoryDto;
import com.example.vhandler.entity.VoucherHistory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CronJobHandlerService {

    private final VoucherHandlerService voucherHandlerService;
    private final RetryCacheServiceImpl retryCacheService;
    private final SmsCacheServiceImpl smsCacheService;

    @Scheduled(initialDelay = 60000L, fixedDelay = 120000L)
    public void retryJob() {
        log.info("Start retryJob");
        Map<Long, VoucherHistoryDto> voucherHistoryDtoMap = retryCacheService.findAll();
        if (!CollectionUtils.isEmpty(voucherHistoryDtoMap)) {
            try {
                voucherHandlerService.bulkGenerateVoucherCode(voucherHistoryDtoMap.values());
                Long[] ids = voucherHistoryDtoMap.keySet().toArray(new Long[0]);
                retryCacheService.deleteById(ids);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(initialDelay = 60000L, fixedDelay = 120000L)
    public void sendSmsJob() {
        log.info("Start sendSmsJob");
        Map<Long, VoucherHistoryDto> voucherHistoryDtoMap = smsCacheService.findAll();
        if (!CollectionUtils.isEmpty(voucherHistoryDtoMap)) {
            // bulk update voucher code
            List<VoucherHistory> voucherHistories = voucherHandlerService.bulkUpdateVoucher(voucherHistoryDtoMap);
            // send sms then clean cache
            Long[] ids = voucherHistoryDtoMap.keySet().toArray(new Long[0]);
            log.info("Send sms");
            smsCacheService.deleteById(ids);
        }
    }
}

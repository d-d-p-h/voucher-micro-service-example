package com.example.vhandler.repository;

import com.example.vhandler.entity.VoucherHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "voucherhistory", path = "voucher-history")
public interface VoucherHistoryRepository extends JpaRepository<VoucherHistory, Integer>, JpaSpecificationExecutor<VoucherHistory> {

    @Override
    @RestResource
    Optional<VoucherHistory> findById(Integer integer);

    @Override
    @RestResource
    Page<VoucherHistory> findAll(Pageable pageable);

    @RestResource
    List<VoucherHistory> findAllByIdIn(@Param("id") Collection<Long> ids);
}

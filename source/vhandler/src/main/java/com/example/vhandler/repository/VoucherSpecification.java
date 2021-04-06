package com.example.vhandler.repository;

import com.example.vhandler.entity.VoucherHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoucherSpecification implements Specification<VoucherHistory> {

    private String phoneNumber;
    private String voucherType;

    @Override
    public Predicate toPredicate(Root<VoucherHistory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNoneBlank(phoneNumber)) {
            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber));
        }

        if (StringUtils.isNoneBlank(voucherType)) {
            predicates.add(criteriaBuilder.equal(root.get("voucherType"), voucherType));
        }

        Predicate[] pre = predicates.toArray(new Predicate[0]);

        return criteriaBuilder.and(pre);
    }
}

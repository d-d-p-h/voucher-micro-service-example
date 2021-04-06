package com.example.vhandler.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Column(name = "creat_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updt_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "creat_dt")
    @CreatedDate
    private Date createdDate;

    @Column(name = "updt_dt")
    @LastModifiedDate
    private Date updatedDate;
}

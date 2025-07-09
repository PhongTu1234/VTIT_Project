package com.vtit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vtit.utils.SecurityUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String code;

    @Column(length = 100)
    private String name;

    @Column(name = "created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdDate;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedDate;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @PrePersist //Trước khi lưu mới (INSERT)
    protected void onCreate() {
    	this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
    			SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdDate = Instant.now();
//        if (isActive == null) isActive = true;
//        if (isDeleted == null) isDeleted = false;
    }

    @PreUpdate //Trước khi cập nhật (UPDATE)
    protected void onUpdate() {
    	this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
    			SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedDate = Instant.now();
    }
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BookCategory> bookCategories;

}
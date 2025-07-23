package com.vtit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vtit.utils.CustomInstantDeserializer;
import com.vtit.utils.CustomInstantSerializer;
import com.vtit.utils.SecurityUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Users", uniqueConstraints = {
	    @UniqueConstraint(columnNames = "email")
	})
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username không được để trống")
    private String username;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Password không được để trống")
    private String password;

    @Column(length = 100)
    private String fullname;

    @Column(length = 100)
    @Email(message = "Email không đúng định dạng")
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;
    
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @JsonSerialize(using = CustomInstantSerializer.class)
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant birthday;

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

    @PrePersist //Trước khi lưu mới (INSERT)
    protected void onCreate() {
    	this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
    			SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdDate = Instant.now();
        if (isActive == null) isActive = true;
    }

    @PreUpdate //Trước khi cập nhật (UPDATE)
    protected void onUpdate() {
    	this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
    			SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedDate = Instant.now();
    }
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //xóa trong List Java = xóa trong DB
    @JsonIgnore
    private List<Borrowing> borrowings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //xóa trong List Java = xóa trong DB
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //xóa trong List Java = xóa trong DB
    @JsonIgnore
    private List<Comment> comments;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "User_Role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> user_Role;
}

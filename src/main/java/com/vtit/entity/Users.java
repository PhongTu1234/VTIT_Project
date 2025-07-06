package com.vtit.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Users")
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

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PrePersist //Trước khi lưu mới (INSERT)
    protected void onCreate() {
        this.createdDate = new Date();
        if (isActive == null) isActive = true;
    }

    @PreUpdate //Trước khi cập nhật (UPDATE)
    protected void onUpdate() {
        this.updatedDate = new Date();
    }
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //xóa trong List Java = xóa trong DB
    @JsonIgnore
    private List<UserRole> userRoles;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //xóa trong List Java = xóa trong DB
    @JsonIgnore
    private List<Borrowing> borrowings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //xóa trong List Java = xóa trong DB
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //xóa trong List Java = xóa trong DB
    @JsonIgnore
    private List<Comment> comments;
}

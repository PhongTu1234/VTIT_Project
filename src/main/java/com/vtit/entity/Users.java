package com.vtit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vtit.utils.CustomInstantDeserializer;
import com.vtit.utils.CustomInstantSerializer;
import com.vtit.utils.SecurityUtil;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username không được để trống")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 100)
    private String fullname;

    @Email(message = "Email không đúng định dạng")
    @Column(length = 100, unique = true)
    private String email;

    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Số điện thoại không hợp lệ")
    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @JsonSerialize(using = CustomInstantSerializer.class)
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant birthday;

    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    @Column(name = "updated_date")
    private Instant updatedDate;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.createdDate = Instant.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.updatedDate = Instant.now();
    }

    // Quan hệ với Borrowing
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Borrowing> borrowings;

    // Quan hệ với Post
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Post> posts;

    // Quan hệ với Comment
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    // Quan hệ với Role (mỗi user chỉ có 1 role)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Roles role;
}

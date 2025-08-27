package com.vtit.utils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.vtit.entity.Permission;
import com.vtit.reponsitory.PermissionRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component("customPermissionEvaluator")
public class CustomPermissionEvaluator {

    private final PermissionRepository permissionRepository;

    public CustomPermissionEvaluator(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    /**
     * Hàm này được gọi trong @PreAuthorize để kiểm tra quyền của user theo HttpRequest
     */
    public boolean check(Authentication authentication) {
        // Lấy request hiện tại
        HttpServletRequest request = ((ServletRequestAttributes) 
                RequestContextHolder.getRequestAttributes()).getRequest();

        if (request == null) {
            return false;
        }

        String method = request.getMethod();
        String uri = request.getRequestURI();

        // Lấy permission user có từ JWT (được load vào GrantedAuthority)
        Set<String> userPermissions = authentication.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())  // ví dụ: "api.v1.library.book.detail"
                .collect(Collectors.toSet());

        // Lấy tất cả permission đang active trong DB
        List<Permission> dbPermissions = permissionRepository.findByIsActiveTrue();

        for (Permission p : dbPermissions) {
            // So khớp METHOD + PATH (có thể tùy biến regex cho {id})
            String pathRegex = p.getPathParent()
                                 .replace("{id}", "\\d+"); // ví dụ: /api/v1/books/{id} => /api/v1/books/\d+

            if (method.equalsIgnoreCase(p.getMethod()) && uri.matches(pathRegex)) {
                // Nếu user có đúng quyền code của API này thì cho phép
                if (userPermissions.contains(p.getCode())) {
                    return true;
                }
            }
        }

        return false;
    }
}
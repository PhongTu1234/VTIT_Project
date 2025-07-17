package com.vtit.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;
import com.vtit.dto.response.auth.ResLoginDTO;

@Service
public class SecurityUtil {
	
	private final JwtEncoder jwtEncoder;
	
	public SecurityUtil(JwtEncoder encoder) {
		this.jwtEncoder = encoder;
	}
	
	public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

	@Value("${jwt.secret}")
	private String jwtKey;
	
	@Value("${jwt.access-token-validity-seconds}")
	private Long accessTokenExpiration;
	
	@Value("${jwt.refresh-token-validity-seconds}")
	private Long refreshTokenExpiration;
	
	private SecretKey getSecretKet() {
		byte[] keyBytes = Base64.from(jwtKey).decode();
		return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
	}
	
	public Jwt checkValidRefreshToken(String token) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKet())
				.macAlgorithm(JWT_ALGORITHM).build();
		try {
			return jwtDecoder.decode(token);
		} catch (Exception e) {
			System.out.println(">>> Refresh Token error: " + e.getMessage());
			throw e;
		}
	}
	
	public String createAccessToken(String email, ResLoginDTO.UserInfo userInfo) {
	    Instant now = Instant.now();
	    Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);
	    
	    List<String> permissions = new ArrayList<>();
	    permissions.add("ROLE_USER_CREATE");
	    permissions.add("ROLE_USER_READ");

	    JwtClaimsSet claims = JwtClaimsSet.builder()
	        .issuedAt(now)
	        .expiresAt(validity)
	        .subject(email)
	        .claim("user", userInfo)
	        .claim("permission", permissions)
	        .build();

	    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
	    return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
	}
	
	public String createRefreshToken(String email, ResLoginDTO resLoginDTO) {
	    Instant now = Instant.now();
	    Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

	    JwtClaimsSet claims = JwtClaimsSet.builder()
	        .issuedAt(now)
	        .expiresAt(validity)
	        .subject(email)
	        .claim("user", resLoginDTO.getUserInfo())
	        .build();

	    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
	    return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
	}
	
	/**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt ) {
        	return jwt.getSubject();
        }else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
//    public static boolean isAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
//    }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
//    public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return (
//            authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
//        );
//    }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
//    public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
//        return !hasCurrentUserAnyOfAuthorities(authorities);
//    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
//    public static boolean hasCurrentUserThisAuthority(String authority) {
//        return hasCurrentUserAnyOfAuthorities(authority);
//    }
//
//    private static Stream<String> getAuthorities(Authentication authentication) {
//        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
//    }

}

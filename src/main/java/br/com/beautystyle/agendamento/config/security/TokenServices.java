package br.com.beautystyle.agendamento.config.security;

import br.com.beautystyle.agendamento.model.entity.User;
import br.com.beautystyle.agendamento.model.entity.UserProfessional;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TokenServices {

    @Value("${agendamento.jwt.expiration}")
    private String expiration;

    @Value("${agendamento.jwt.secret}")
    private String secret;

    public boolean isValidateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String createToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + Long.parseLong(expiration));
        Long tenant = -1L;
        if (user instanceof UserProfessional)
            tenant = getTenant((UserProfessional) user);
        return Jwts.builder()
                .setIssuer("BeautyStyle")
                .setSubject(user.getId().toString())
                .setIssuedAt(date)
                .setId(tenant.toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private Long getTenant(UserProfessional user) {
        return user.getCompany().getId();
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public Long getTenant(HttpServletRequest request) {
        String token = getToken(request);
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getId());
    }

    public Long getUserId(HttpServletRequest request) {
        String token = getToken(request);
        return getUserId(token);
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }

}

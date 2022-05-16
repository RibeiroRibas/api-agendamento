package br.com.beautystyle.agendamento.config.security;

import br.com.beautystyle.agendamento.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServices {

    @Value( "${agendamento.jwt.expiration}")
    private String expiration;

    @Value( "${agendamento.jwt.secret}")
    private String secret;

    public boolean isValidateToken(String token) {
        try{
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String createToken(Authentication authentication){
        User loged = (User) authentication.getPrincipal();
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer("agendamento")//qual a plicação está gerando o token
                .setSubject(loged.getId().toString())//usuario, quem é o dono
                .setIssuedAt(date)// qual foi a data
                .setExpiration(expirationDate) // tempo que o usuario vai ficar logado em milisegundos
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact(); // o token tem que ser criptografado(algoritimo pra fazer a criptografia, minha senha que coloquei na properties)
    }

    public Long getUserId(String token) {
        // classe da api jsonwebtoken
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}

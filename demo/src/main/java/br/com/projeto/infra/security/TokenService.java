package br.com.projeto.infra.security;

import br.com.projeto.models.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API citry-login")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt",exception);
        }
    }

    public String validationToken(String tokenJWT){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API citry-login")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class senhas {
    public static void main(String[] args) {
        String flamengo10 = new BCryptPasswordEncoder().encode("flamengo10");
        System.out.println("senha"+ flamengo10);
    }
}

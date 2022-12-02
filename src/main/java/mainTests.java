import br.com.beautystyle.agendamento.repository.JobRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class mainTests {
    public static void main(String[] args) {
        String flamengo10 = new BCryptPasswordEncoder().encode("flamengo10");
        System.out.println(flamengo10);
        String daysOfTheWeek = "";
        List<Integer> daysOfWeek = new ArrayList<>();
        for (int day = 2; day <= 6; day++) {
            daysOfWeek.add(day);
        }
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);

        String join = StringUtils.join(daysOfWeek.toArray(), ",");
        System.out.println(join);
        JobRepository repository = null;
        teste(repository);
    }

    static void teste(JpaRepository<?, Long> repository) {
        repository.deleteById(1L);
    }
}

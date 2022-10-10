import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    }
}

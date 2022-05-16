package br.com.beautystyle.agendamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AgendamentoApplication {

	public static void main(String[] args) {

		SpringApplication.run(AgendamentoApplication.class, args);
	}

}

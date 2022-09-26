package org.tog.togmobilemediaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// TODO: remove excludes once DB is configured
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TogMobileMediaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TogMobileMediaServiceApplication.class, args);
	}

}

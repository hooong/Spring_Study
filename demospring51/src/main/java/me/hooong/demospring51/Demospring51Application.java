package me.hooong.demospring51;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demospring51Application {

	// 컴포넌트 스캔 범위를 벗어남.
//	@Autowired
//	MyService myService;
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Demospring51Application.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

}

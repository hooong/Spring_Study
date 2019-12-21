package me.hooong.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import out.MyService;

@SpringBootApplication
public class Demospring51Application {

	// 컴포넌트 스캔 범위를 벗어남.
//	@Autowired
//	MyService myService;

	public static void main(String[] args) {
		SpringApplication.run(Demospring51Application.class, args);
	}

}

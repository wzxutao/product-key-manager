package cn.rypacker.productkeymanager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ProductKeyManagerApplication{

	public static void main(String[] args) {
		new SpringApplicationBuilder(ProductKeyManagerApplication.class).headless(false).run(args);
	}

}

package cn.rypacker.productkeymanager;

import cn.rypacker.productkeymanager.bootstrap.FirstTimeInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;

@SpringBootApplication
public class ProductKeyManagerApplication{
	public static void main(String[] args) throws IOException, InterruptedException {
		FirstTimeInitializer.initIfNecessary();
		new SpringApplicationBuilder(ProductKeyManagerApplication.class).headless(false).run(args);
	}

}

package cn.rypacker.productkeymanager;

import cn.rypacker.productkeymanager.bootstrap.FirstTimeInitialization;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;

@SpringBootApplication
public class ProductKeyManagerApplication{
	public static void main(String[] args) throws IOException {
		// will be called twice due to restart of main. but doesn't matter
		FirstTimeInitialization.initIfNecessary();
		new SpringApplicationBuilder(ProductKeyManagerApplication.class).headless(false).run(args);
	}

}

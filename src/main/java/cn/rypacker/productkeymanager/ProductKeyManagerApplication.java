package cn.rypacker.productkeymanager;

import cn.rypacker.productkeymanager.bootstrap.DatabaseUpdater;
import cn.rypacker.productkeymanager.bootstrap.DbRestorer;
import cn.rypacker.productkeymanager.bootstrap.FirstTimeInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ProductKeyManagerApplication{
	private static ConfigurableApplicationContext context;


	public static void main(String[] args) throws IOException, InterruptedException {
		FirstTimeInitializer.initIfNecessary();
		DbRestorer.restoreDbIfRequested();
		DatabaseUpdater.updateIfNeeded();
		var app = new SpringApplicationBuilder(ProductKeyManagerApplication.class)
				.headless(false);
		context = app.run(args);
	}

	public static void close(int milliDelay){
		new Thread(()->{
			try {
				Thread.sleep(milliDelay);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			System.exit(0);
		}).start();
	}

}

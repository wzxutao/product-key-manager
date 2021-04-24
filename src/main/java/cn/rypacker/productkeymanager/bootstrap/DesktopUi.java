package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.desktopui.MainFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class DesktopUi implements CommandLineRunner {

    @Autowired
    Environment env;

    @Override
    public void run(String... args) throws Exception {
        EventQueue.invokeLater(()->{
            var frame = new MainFrame();
            frame.init(env.getProperty("server.port"));
            frame.setVisible(true);
        });
    }
}

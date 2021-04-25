package cn.rypacker.productkeymanager.desktopui;

import cn.rypacker.productkeymanager.services.AdminAccountManager;

import javax.swing.*;
import java.awt.*;
public class AdminAccountCreationFrame extends JFrame{

    JTextField usernameField;
    JTextField passwordField;
    JTextField passwordConfirmField;

    JLabel messageLabel;

    private volatile boolean running = true;


    public AdminAccountCreationFrame() throws HeadlessException {

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(4, 2));

        var l = new JLabel("用户名");
        usernameField = new JTextField(16);
        p.add(l);
        p.add(usernameField);

        l = new JLabel("密码：");
        passwordField = new JTextField(16);
        p.add(l);
        p.add(passwordField);

        l = new JLabel("确认密码：");
        passwordConfirmField = new JTextField(16);
        p.add(l);
        p.add(passwordConfirmField);

        var b = new JButton("创建");
        b.addActionListener(e -> {
            String s = e.getActionCommand();
            if (s.equals("创建")) {
                var username = usernameField.getText().strip();
                var password = passwordField.getText().strip();
                var passwordConfirmed = passwordConfirmField.getText().strip();

                if(username.isEmpty() || password.isEmpty()){
                    messageLabel.setText("用户名密码不能为空");
                    return;
                }

                if(!password.equals(passwordConfirmed)){
                    messageLabel.setText("两次密码不一致");
                    return;
                }

                AdminAccountManager.createAdminAccount(username, password, false);
                AdminAccountCreationFrame.this.setVisible(false);
                running = false;
            }
        });
        p.add(b);

        messageLabel = new JLabel("");
        p.add(messageLabel);

        add(p);

        setTitle("管理员账户创建");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public boolean isRunning() {
        return running;
    }
}
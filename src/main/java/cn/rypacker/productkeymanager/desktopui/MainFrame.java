package cn.rypacker.productkeymanager.desktopui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame extends JFrame {

    private static final int DEFAULT_WIDTH = 580;
    private static final int DEFAULT_HEIGHT = 580;

    private QrCodePanel qrCodePanel;

    public MainFrame() throws HeadlessException {
        this.setTitle("帝国皇家机械设备有限公司序列号管理系统");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void init(String port) {
        var ipList = LocalIpHelper.getIpList(port);
        var qrPanel = new QrCodePanel(ipList);
        this.qrCodePanel = qrPanel;
        add(qrPanel);
        addIpListComboBox(ipList);
    }


    private void addIpListComboBox(java.util.List<String> ipList){
        var combo = new JComboBox<String>();
        for (var ip :
                ipList) {
            combo.addItem(ip);
        }
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var ipIndex = combo.getSelectedIndex();
                qrCodePanel.setCurrentImage(ipIndex);
                qrCodePanel.repaint();
            }
        });

        var comboPanel = new JPanel();
        comboPanel.add(combo);

        this.add(comboPanel, BorderLayout.NORTH);
    }
}

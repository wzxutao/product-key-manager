package cn.rypacker.productkeymanager.desktopui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainFrame extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

    private static final int DEFAULT_WIDTH = 580;
    private static final int DEFAULT_HEIGHT = 600;

    private static final String LOGO_ICON_PATH = "resources/icon/logo.jpg";
    TrayIcon trayIcon;
    SystemTray tray;

    private QrCodePanel qrCodePanel;

    public MainFrame() throws HeadlessException {
        this.setTitle("帝国皇家机械设备有限公司序列号管理系统");
        this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        systemTrayInit();
    }

    public void init(String port) {
        var ipList = LocalIpHelper.getIpList(port);
        var qrPanel = new QrCodePanel(ipList);
        this.qrCodePanel = qrPanel;
        add(qrPanel);
        addIpListComboBox(ipList);
    }

    private void systemTrayInit(){
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.this.setExtendedState(JFrame.ICONIFIED);
            }
        });

        if(SystemTray.isSupported()){
            logger.info("system tray supported");
            tray=SystemTray.getSystemTray();

            Image image=Toolkit.getDefaultToolkit().getImage(LOGO_ICON_PATH);
            ActionListener exitListener=new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    logger.info("Exiting....");
                    System.exit(0);
                }
            };
            PopupMenu popup=new PopupMenu();
            var defaultItem=new MenuItem("Open");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);

            defaultItem=new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            trayIcon=new TrayIcon(image, "SystemTray Demo", popup);
            trayIcon.setImageAutoSize(true);
        }else{
            logger.info("system tray not supported");
        }
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==ICONIFIED){
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                        logger.info("added to SystemTray");
                    } catch (AWTException ex) {
                        logger.info("unable to add to tray");
                    }
                }
                if(e.getNewState()==7){
                    try{
                        tray.add(trayIcon);
                        setVisible(false);
                        logger.info("added to SystemTray");
                    }catch(AWTException ex){
                        logger.info("unable to add to system tray");
                    }
                }
                if(e.getNewState()==MAXIMIZED_BOTH){
                    tray.remove(trayIcon);
                    setVisible(true);
                    logger.info("Tray icon removed");
                }
                if(e.getNewState()==NORMAL){
                    tray.remove(trayIcon);
                    setVisible(true);
                    logger.info("Tray icon removed");
                }
            }
        });
        setIconImage(Toolkit.getDefaultToolkit().getImage(LOGO_ICON_PATH));
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

package cn.rypacker.productkeymanager.desktopui;

import cn.rypacker.productkeymanager.config.StaticInformation;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.PrintStream;

public class LoadingLogo {

    private static final PrintStream stdOut = System.out;

    private static JFrame logoFrame;
    private static JPanel logoPanel;

    private static void initIfNeeded(){
        if(logoFrame != null) return;

        logoFrame = new JFrame();
//        logoPanel = new JPanel(){
//            private BufferedImage image;
//            {
//                try {
//                    image = ImageIO.read(new File("resources/icon/logo.jpg"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(image, 0, 0, this);
//            }
//        };

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        logoFrame.setSize(new Dimension(1000, 500));
        logoFrame.setLocation(dim.width/2-logoFrame.getSize().width/2,
                dim.height/2-logoFrame.getSize().height/2);
        logoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        logoFrame.setTitle(StaticInformation.APPLICATION_TITLE);
        logoFrame.setUndecorated(true);

//        logoFrame.add(logoPanel);
        hide();

    }

    public static void addStdoutTextArea(){
        var textArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        var scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0,0,1000,500);
        scrollPane.add(textArea);
        scrollPane.setViewportView(textArea);


        logoFrame.add(scrollPane,BorderLayout.NORTH);
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);
    }

    public static void restoreStdout(){
        System.setOut(stdOut);
    }


    public static void show(){
        initIfNeeded();
        if(!logoFrame.isVisible()){
            logoFrame.setVisible(true);
//            logoPanel.repaint();
        }
    }

    public static void hide(){
        logoFrame.setVisible(false);
        restoreStdout();
    }
}

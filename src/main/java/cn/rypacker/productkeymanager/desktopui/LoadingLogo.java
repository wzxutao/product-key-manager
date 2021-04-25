package cn.rypacker.productkeymanager.desktopui;

import cn.rypacker.productkeymanager.config.StaticInformation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoadingLogo {

    private static JFrame logoFrame = new JFrame();
    private static JPanel logoPanel = new JPanel(){
        private BufferedImage image;
        {
            try {
                image = ImageIO.read(new File("resources/icon/logo.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    };

    static{
        logoFrame.add(logoPanel);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        logoFrame.setSize(new Dimension(500, 500));
        logoFrame.setLocation(dim.width/2-logoFrame.getSize().width/2,
                dim.height/2-logoFrame.getSize().height/2);
        logoFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        logoFrame.setTitle(StaticInformation.APPLICATION_TITLE);
        hide();
    }


    public static void show(){
        logoFrame.setVisible(true);
        logoPanel.repaint();
//        logoFrame.revalidate();
    }

    public static void hide(){
        logoFrame.setVisible(false);
    }
}

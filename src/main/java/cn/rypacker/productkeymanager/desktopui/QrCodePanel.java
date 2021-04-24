package cn.rypacker.productkeymanager.desktopui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class QrCodePanel extends JPanel {

    private static Logger logger = LoggerFactory.getLogger(QrCodePanel.class);

    private java.util.List<File> imageFiles = new ArrayList<>();
    private BufferedImage image;

    public QrCodePanel(java.util.List<String> ipList) {
        int index = 0;
        for (var ip :
                ipList) {
            var imageFile = generateQrCode(ip, index);
            this.imageFiles.add(imageFile);

            logger.info("QR code generated for: " + ip);
            index++;
        }
        setCurrentImage(0);
    }

    public void setCurrentImage(int index){
        try {
            image = ImageIO.read(imageFiles.get(index));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    public File generateQrCode(String src, int index){
        String filePath = "data" + File.separator + String.format("urlQr%d.png", index);
        int size = 512;
        String qrCodeFileType = "png";
        File qrCodeFile = new File(filePath);
        try {

            Map<EncodeHintType, Object> crunchifyHintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            crunchifyHintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Now with version 3.4.1 you could change margin (white border size)
            crunchifyHintType.put(EncodeHintType.MARGIN, 1); /* default = 4 */
            Object put = crunchifyHintType.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            QRCodeWriter mYQRCodeWriter = new QRCodeWriter(); // throws com.google.zxing.WriterException
            BitMatrix crunchifyBitMatrix = mYQRCodeWriter.encode(src, BarcodeFormat.QR_CODE, size,
                    size, crunchifyHintType);
            int CrunchifyWidth = crunchifyBitMatrix.getWidth();

            // The BufferedImage subclass describes an Image with an accessible buffer of image data.
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);

            // Creates a Graphics2D, which can be used to draw into this BufferedImage.
            image.createGraphics();

            // This Graphics2D class extends the Graphics class to provide more sophisticated control over geometry, coordinate transformations, color management, and text layout.
            // This is the fundamental class for rendering 2-dimensional shapes, text and images on the Java(tm) platform.
            Graphics2D crunchifyGraphics = (Graphics2D) image.getGraphics();

            // setColor() sets this graphics context's current color to the specified color.
            // All subsequent graphics operations using this graphics context use this specified color.
            crunchifyGraphics.setColor(Color.white);

            // fillRect() fills the specified rectangle. The left and right edges of the rectangle are at x and x + width - 1.
            crunchifyGraphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);

            // TODO: Please change this color as per your need
            crunchifyGraphics.setColor(Color.BLUE);

            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (crunchifyBitMatrix.get(i, j)) {
                        crunchifyGraphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            ImageIO.write(image, qrCodeFileType, qrCodeFile);
            return qrCodeFile;
        } catch (WriterException | IOException e) {
            System.out.println("\nSorry.. Something went wrong...\n");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}

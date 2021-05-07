package cn.rypacker.productkeymanager.desktopui;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    private StringBuilder buffer = new StringBuilder();

    public CustomOutputStream(JTextArea jTextArea) {
        this.textArea = jTextArea;
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
//        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);

    }

    @Override
    public void write(int b) throws IOException {
        var c = String.valueOf((char) b);
        buffer.append(c);
        if(c.contains("\n")){
            var s = buffer.toString();
            textArea.setText(s);
        }
    }
}

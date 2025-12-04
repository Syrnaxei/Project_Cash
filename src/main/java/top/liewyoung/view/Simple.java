package top.liewyoung.view;

import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TextDialog extends JDialog {
    TextDialog(JFrame parent,String title,boolean modal,String content) {
        super(parent,title, modal);
        Container contentPane = getContentPane();
        contentPane.add(new JLabel(content), BorderLayout.NORTH);
        setBounds(100, 100, 450, 300);
    }
}

class DialogWithButton extends JDialog {

    public  DialogWithButton(JFrame parent,String title,boolean modal) {
        super(parent,title, modal);
        Container contentPane = getContentPane();
        JButton okButton = new JButton("OK");
        contentPane.add(okButton,BorderLayout.SOUTH);
        setBounds(100, 100, 450, 300);
    }
}

public class Simple extends JFrame {

    public Simple() {
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        JButton button = new JButton("Show");
        button.setBounds(10, 10, 100, 30);
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                TextDialog dialog = new TextDialog(Simple.this,"Hello",true,"Hello LiewYoung");
                DialogWithButton dialogWithButton = new DialogWithButton(Simple.this,"Show",true);
                dialogWithButton.setLocationRelativeTo(Simple.this);
                dialogWithButton.setVisible(true);
                dialog.setVisible(true);
            }
        });

        contentPane.add(button);
        setSize(300,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Simple();
    }
}

package bitmex.Bot.controller;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Test_2 {

    public static void main(String[] args) {
        JFrame jFrame = getJFrame();
        JPanel jPanel = new JPanel();
        jFrame.add(jPanel);
        jPanel.setBackground(Color.LIGHT_GRAY);
        JButton jButtonStart = new JButton("START");
        JButton jButtonStop = new JButton("STOP");

        jPanel.add(jButtonStart);
        jPanel.add(jButtonStop);




        jPanel.add(new JLabel("Commands"));
        JTextField jTextField = new JTextField("insert commands",30);
        jPanel.add(jTextField);
        jPanel.revalidate();


        JButton jButtonSet = new JButton("SET");
        jPanel.add(jButtonSet);






        JTextArea jTextArea = new JTextArea(35, 90);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jTextArea.setLineWrap(true);
        jPanel.add(jScrollPane);

        // с помощбю этого метода выводим текст
        jTextArea.append("\n");


        jButtonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // тут прописать старт программы
                jPanel.setBackground(Color.GREEN);
            }
        });


        jButtonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // тут прописать Стоп программы
                jPanel.setBackground(Color.RED);
            }
        });



        jButtonSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // тут прописать настройки программы
                jPanel.setBackground(Color.yellow);
                String text = jTextField.getText();
                jTextArea.append(text + "\n");

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000 * 10);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        jPanel.setBackground(Color.GREEN);
                    }
                }.start();
            }
        });






//
//        Font font = new Font("Bitstream Charter", Font.BOLD, 20);
    }

    private static JFrame getJFrame() {
        JFrame jFrame = new JFrame() {};
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        jFrame.setSize(1110, 635);
        jFrame.setLocation(dimension.width/2 - 570, dimension.height/2 - 325);
        jFrame.setTitle("II POWER by SAMUIL_OLEGOVICH");
//        jFrame.setIconImage();
        return jFrame;
    }
}





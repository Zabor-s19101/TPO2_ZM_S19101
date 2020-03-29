/**
 *
 *  @author Zaborowski Mateusz S19101
 *
 */

package zad1;


import com.sun.javafx.application.PlatformImpl;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    JFrame frame = new JFrame("TPO2");

    // info TextArea
    JTextArea textArea = new JTextArea("Weather: " + s.getWeatherJSON() + "\n" +
            "Rate for " + s.getRateFor() + ": " + s.getRate1() + "\n" +
            "Rate for PLN: " + s.getRate2());
    textArea.setEditable(false);
    JScrollPane jScrollPane = new JScrollPane(textArea);
    jScrollPane.setPreferredSize(new Dimension(800, 75));
    frame.getContentPane().add(jScrollPane, BorderLayout.NORTH);

    // java-FX WebEngine
    JFXPanel jfxPanel = new JFXPanel();
    jfxPanel.setPreferredSize(new Dimension(800, 700));
    PlatformImpl.runLater(() -> {
      WebView browser = new WebView();
      jfxPanel.setScene(new Scene(browser));
      browser.getEngine().load("https://en.wikipedia.org/wiki/" + s.getCity());
    });
    frame.getContentPane().add(jfxPanel, BorderLayout.CENTER);

    // input JPanel
    JPanel inputs = new JPanel();
    inputs.setLayout(new BoxLayout(inputs, BoxLayout.LINE_AXIS));
    inputs.add(Box.createHorizontalStrut(3));

    JLabel countryLabel = new JLabel("Country:");
    JTextField countryTF = new JTextField(10);
    countryTF.setMaximumSize(countryTF.getPreferredSize());
    countryTF.setText(s.getCountry());
    countryLabel.setLabelFor(countryTF);
    inputs.add(countryLabel);
    inputs.add(countryTF);
    inputs.add(Box.createHorizontalStrut(10));

    JLabel cityLabel = new JLabel("City:");
    JTextField cityTF = new JTextField(10);
    cityTF.setMaximumSize(cityTF.getPreferredSize());
    cityTF.setText(s.getCity());
    cityLabel.setLabelFor(cityTF);
    inputs.add(cityLabel);
    inputs.add(cityTF);
    inputs.add(Box.createHorizontalStrut(10));

    JLabel rateForLabel = new JLabel("Rate for:");
    JTextField rateForTF = new JTextField(3);
    rateForTF.setMaximumSize(rateForTF.getPreferredSize());
    rateForTF.setText(s.getRateFor());
    rateForLabel.setLabelFor(rateForTF);
    inputs.add(rateForLabel);
    inputs.add(rateForTF);
    inputs.add(Box.createHorizontalGlue());

    JButton searchButton = new JButton("Search");
    searchButton.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        s.setCountry(countryTF.getText());
        s.getWeather(cityTF.getText());
        s.getRateFor(rateForTF.getText());
        s.getNBPRate();

        textArea.setText("Weather: " + s.getWeatherJSON() + "\n" +
                "Rate for " + s.getRateFor() + ": " + s.getRate1() + "\n" +
                "Rate for PLN: " + s.getRate2());

        PlatformImpl.runLater(() -> {
          WebView browser = new WebView();
          jfxPanel.setScene(new Scene(browser));
          browser.getEngine().load("https://en.wikipedia.org/wiki/" + s.getCity());
        });
      }
    });
    inputs.add(searchButton);

    frame.getContentPane().add(inputs, BorderLayout.SOUTH);

    frame.pack();
    frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

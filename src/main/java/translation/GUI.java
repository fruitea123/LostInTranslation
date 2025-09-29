package translation;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            LanguageCodeConverter langConv = new LanguageCodeConverter();
            CountryCodeConverter countryConv = new CountryCodeConverter();

            JFrame frame = new JFrame("Country Name Translator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setLayout(new BorderLayout(10, 10));

            JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel langLabel = new JLabel("Language:");
            List<String> allLanguages = new ArrayList<>(langConv.getAllLanguages());
            JComboBox<String> languageCombo = new JComboBox<>(allLanguages.toArray(new String[0]));
            langPanel.add(langLabel);
            langPanel.add(languageCombo);

            List<String> allCountries = new ArrayList<>(countryConv.getAllCountries());
            JList<String> countryList = new JList<>(allCountries.toArray(new String[0]));
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane countryScroll = new JScrollPane(countryList);

            JLabel resultLabel = new JLabel("Translation: ");
            resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

            frame.add(langPanel, BorderLayout.NORTH);
            frame.add(countryScroll, BorderLayout.CENTER);
            frame.add(resultLabel, BorderLayout.SOUTH);

            Runnable updateTranslation = () -> {
                String langName = (String) languageCombo.getSelectedItem();
                String countryName = countryList.getSelectedValue();

                if (langName == null || countryName == null) {
                    resultLabel.setText("Translation: (please select)");
                    return;
                }

                String langCode = langConv.fromLanguage(langName);
                String countryCode = countryConv.fromCountry(countryName);

                String translation = translator.translate(countryCode, langCode);
                if (translation == null) translation = "(no translation found)";
                resultLabel.setText("Translation: " + translation);
            };

            languageCombo.addActionListener(e -> updateTranslation.run());
            countryList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) updateTranslation.run();
            });

            if (!allLanguages.isEmpty()) languageCombo.setSelectedIndex(0);
            if (!allCountries.isEmpty()) countryList.setSelectedIndex(0);
            updateTranslation.run();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
//            JPanel countryPanel = new JPanel();
//            JTextField countryField = new JTextField(10);
//            countryField.setText("can");
//            countryField.setEditable(false); // we only support the "can" country code for now
//            countryPanel.add(new JLabel("Country:"));
//            countryPanel.add(countryField);
//
//            JPanel languagePanel = new JPanel();
//            JTextField languageField = new JTextField(10);
//            languagePanel.add(new JLabel("Language:"));
//            languagePanel.add(languageField);
//
//            JPanel buttonPanel = new JPanel();
//            JButton submit = new JButton("Submit");
//            buttonPanel.add(submit);
//
//            JLabel resultLabelText = new JLabel("Translation:");
//            buttonPanel.add(resultLabelText);
//            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
//            buttonPanel.add(resultLabel);
//
//
//            // adding listener for when the user clicks the submit button
//            submit.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    String language = languageField.getText();
//                    String country = countryField.getText();
//
//                    // for now, just using our simple translator, but
//                    // we'll need to use the real JSON version later.
//                    Translator translator = new CanadaTranslator();
//
//                    String result = translator.translate(country, language);
//                    if (result == null) {
//                        result = "no translation found!";
//                    }
//                    resultLabel.setText(result);
//
//                }
//
//            });
//
//            JPanel mainPanel = new JPanel();
//            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//            mainPanel.add(countryPanel);
//            mainPanel.add(languagePanel);
//            mainPanel.add(buttonPanel);
//
//            JFrame frame = new JFrame("Country Name Translator");
//            frame.setContentPane(mainPanel);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.pack();
//            frame.setVisible(true);


        });
    }
}

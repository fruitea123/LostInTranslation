package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::createAndShowUI);
    }

    private static void createAndShowUI() {
        // services
        LanguageCodeConverter langConv = new LanguageCodeConverter();
        CountryCodeConverter countryConv = new CountryCodeConverter();
        Translator translator = new JSONTranslator(); 

        // data for UI
        List<String> languages = new ArrayList<>(langConv.getAllLanguageNames());
        List<String> countries = new ArrayList<>(countryConv.getAllCountryNames());
        if (languages.isEmpty()) languages.add("English");
        if (countries.isEmpty()) countries.add("Canada");
        Collections.sort(languages);
        Collections.sort(countries);

        // frame & root
        JFrame frame = new JFrame("Country Name Translator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -------- header: language row (left) + translation row (center) --------
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        // row 1: Language (左对齐)
        JPanel langRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        langRow.add(new JLabel("Language:"));
        JComboBox<String> languageCombo = new JComboBox<>(languages.toArray(new String[0]));
        languageCombo.setPrototypeDisplayValue("Portuguese (Brazil)     ");
        langRow.add(languageCombo);

        // row 2: Translation (居中)
        JPanel translationRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        JLabel translationPrefix = new JLabel("Translation: ");
        JLabel resultLabel = new JLabel(" ");
        resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD));
        translationRow.add(translationPrefix);
        translationRow.add(resultLabel);

        header.add(langRow);
        header.add(Box.createVerticalStrut(6));
        header.add(translationRow);

        root.add(header, BorderLayout.NORTH);

        // -------- center: country list (scrollable) --------
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String c : countries) model.addElement(c);
        JList<String> countryList = new JList<>(model);
        countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        countryList.setVisibleRowCount(14);
        countryList.setFixedCellWidth(220);

        JScrollPane countryScroll = new JScrollPane(countryList);
        root.add(countryScroll, BorderLayout.CENTER);

        frame.setContentPane(root);

        // defaults
        if (!countries.isEmpty()) {
            int idx = countries.indexOf("Canada");
            countryList.setSelectedIndex(idx >= 0 ? idx : 0);
        }
        if (!languages.isEmpty()) languageCombo.setSelectedIndex(0);

        // interaction
        Runnable update = () -> {
            String languageName = (String) languageCombo.getSelectedItem();
            String countryName = countryList.getSelectedValue();
            if (languageName == null || countryName == null) {
                resultLabel.setText(" ");
                return;
            }
            String langCode = langConv.fromLanguage(languageName);
            String alpha3   = countryConv.fromCountry(countryName);

            String text = null;
            if (langCode != null && alpha3 != null) {
                text = translator.translate(alpha3.toLowerCase(), langCode.toLowerCase());
            }
            resultLabel.setText((text == null || text.isBlank()) ? "no translation found!" : text);
        };

        languageCombo.addActionListener(e -> update.run());
        countryList.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) update.run();
        });

        update.run();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

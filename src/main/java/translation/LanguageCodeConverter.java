package translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class LanguageCodeConverter {

    private final Map<String, String> languageCodeToLanguage = new HashMap<>(); // code(lower) -> Name(original)
    private final Map<String, String> languageToLanguageCode = new HashMap<>(); // name(lower) -> code(lower)

    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    public LanguageCodeConverter(String filename) {
        try {
            List<String> lines = Files.readAllLines(
                    Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            Iterator<String> it = lines.iterator();
            if (it.hasNext()) it.next(); // skip header

            while (it.hasNext()) {
                String line = it.next().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\t");
                if (parts.length < 2) continue;

               
                String code = parts[parts.length - 1].trim().toLowerCase();

                StringBuilder nameBuilder = new StringBuilder();
                for (int i = 0; i < parts.length - 1; i++) {
                    String seg = parts[i].trim();
                    if (seg.isEmpty()) continue;
                    if (nameBuilder.length() > 0) nameBuilder.append(" ");
                    nameBuilder.append(seg);
                }
                String languageName = nameBuilder.toString();
                if (languageName.isEmpty()) continue;

     
                languageCodeToLanguage.put(code, languageName);
                languageToLanguageCode.put(languageName.toLowerCase(), code);
            }

        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    
    public String fromLanguageCode(String code) {
        if (code == null) return null;
        return languageCodeToLanguage.get(code.toLowerCase());
    }

 
    public String fromLanguage(String language) {
        if (language == null) return null;
        return languageToLanguageCode.get(language.toLowerCase());
    }


    public int getNumLanguages() {
        return languageCodeToLanguage.size();
    }


    public java.util.Collection<String> getAllLanguageNames() {
        return new TreeSet<>(languageCodeToLanguage.values());
    }
}

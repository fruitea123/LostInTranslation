package translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class provides the service of converting country codes to their names and back.
 */
public class CountryCodeConverter {

    private final Map<String, String> countryCodeToCountry = new HashMap<>(); // code(lower) -> name
    private final Map<String, String> countryToCountryCode = new HashMap<>(); // name(lower) -> code(lower)

    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    public CountryCodeConverter(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            Iterator<String> iterator = lines.iterator();
            if (iterator.hasNext()) iterator.next(); // skip header

            while (iterator.hasNext()) {
                String line = iterator.next().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\t");
                if (parts.length < 3) continue;

                String countryName = parts[0].trim();
                String code = parts[2].trim().toLowerCase();

                if (!countryName.isEmpty() && !code.isEmpty()) {
                    countryCodeToCountry.put(code, countryName);
                    countryToCountryCode.put(countryName.toLowerCase(), code);
                }
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Return the name of the country for the given country code (case-insensitive).
     */
    public String fromCountryCode(String code) {
        if (code == null) return null;
        return countryCodeToCountry.get(code.toLowerCase());
    }

    /**
     * Return the code of the country for the given country name (case-insensitive).
     */
    public String fromCountry(String country) {
        if (country == null) return null;
        return countryToCountryCode.get(country.toLowerCase());
    }

    /**
     * Return how many countries are included in this country code converter.
     */
    public int getNumCountries() {
        return countryCodeToCountry.size();
    }

    /**
     * Return all country names (sorted, unique).
     */
    public Collection<String> getAllCountryNames() {
        return new TreeSet<>(countryCodeToCountry.values());
    }
}

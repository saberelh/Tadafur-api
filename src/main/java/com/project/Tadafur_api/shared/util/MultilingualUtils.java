// File: src/main/java/com/project/Tadafur_api/shared/util/MultilingualUtils.java
package com.project.Tadafur_api.shared.util;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Enhanced utility for handling multilingual text
 * Backward compatible with existing code + new features
 */
@Component
public class MultilingualUtils {

    public static final String DEFAULT_LANGUAGE = "en";
    private static final List<String> SUPPORTED_LANGUAGES = List.of("en", "ar");

    /**
     * Get translation for a specific language with fallback
     * EXISTING METHOD - keeping for backward compatibility
     */
    public static String getTranslation(Map<String, String> translations, String language) {
        if (translations == null || translations.isEmpty()) {
            return null;
        }

        // Try requested language first
        String translation = translations.get(language);
        if (translation != null && !translation.trim().isEmpty()) {
            return translation;
        }

        // Fallback to English
        translation = translations.get(DEFAULT_LANGUAGE);
        if (translation != null && !translation.trim().isEmpty()) {
            return translation;
        }

        // Fallback to Arabic
        translation = translations.get("ar");
        if (translation != null && !translation.trim().isEmpty()) {
            return translation;
        }

        // Return any available translation
        return translations.values().stream()
                .filter(t -> t != null && !t.trim().isEmpty())
                .findFirst()
                .orElse(null);
    }

    /**
     * Validate and normalize language code
     * NEW METHOD needed by StrategyController
     */
    public static String validateLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            return DEFAULT_LANGUAGE;
        }

        String normalizedLang = language.toLowerCase().trim();
        return switch (normalizedLang) {
            case "en", "english" -> "en";
            case "ar", "arabic" -> "ar";
            default -> DEFAULT_LANGUAGE;
        };
    }

    /**
     * Extract language from Accept-Language header
     * NEW METHOD needed by StrategyController
     */
    public static String extractLanguageFromHeader(String acceptLanguageHeader) {
        if (acceptLanguageHeader == null || acceptLanguageHeader.trim().isEmpty()) {
            return DEFAULT_LANGUAGE;
        }

        // Parse Accept-Language header (e.g., "en-US,en;q=0.9,ar;q=0.8")
        String[] languages = acceptLanguageHeader.split(",");
        for (String lang : languages) {
            String cleanLang = lang.split(";")[0].trim();
            if (cleanLang.startsWith("en")) {
                return "en";
            }
            if (cleanLang.startsWith("ar")) {
                return "ar";
            }
        }

        return DEFAULT_LANGUAGE;
    }

    /**
     * Get supported languages
     */
    public static List<String> getSupportedLanguages() {
        return SUPPORTED_LANGUAGES;
    }

    /**
     * Check if language is supported
     */
    public static boolean isLanguageSupported(String language) {
        return SUPPORTED_LANGUAGES.contains(validateLanguage(language));
    }

    /**
     * Enhanced version of getTranslation with validation
     */
    public static String getTranslationSafe(Map<String, String> translations, String language) {
        String validatedLanguage = validateLanguage(language);
        return getTranslation(translations, validatedLanguage);
    }

    /**
     * Create bilingual translation map
     */
    public static Map<String, String> createBilingualTranslation(String english, String arabic) {
        Map<String, String> translations = new HashMap<>();
        translations.put("en", english != null ? english : "");
        translations.put("ar", arabic != null ? arabic : "");
        return translations;
    }

    /**
     * Create English-only translation
     */
    public static Map<String, String> createEnglishTranslation(String text) {
        Map<String, String> translations = new HashMap<>();
        translations.put("en", text != null ? text : "");
        translations.put("ar", "");
        return translations;
    }

    /**
     * Create Arabic-only translation
     */
    public static Map<String, String> createArabicTranslation(String text) {
        Map<String, String> translations = new HashMap<>();
        translations.put("en", "");
        translations.put("ar", text != null ? text : "");
        return translations;
    }

    /**
     * Check if translations are complete (have both languages)
     */
    public static boolean isTranslationComplete(Map<String, String> translations) {
        if (translations == null || translations.isEmpty()) {
            return false;
        }

        String english = translations.get("en");
        String arabic = translations.get("ar");

        return (english != null && !english.trim().isEmpty()) &&
                (arabic != null && !arabic.trim().isEmpty());
    }

    /**
     * Get missing languages for a translation
     */
    public static List<String> getMissingLanguages(Map<String, String> translations) {
        List<String> missing = new ArrayList<>();

        for (String lang : SUPPORTED_LANGUAGES) {
            String text = translations != null ? translations.get(lang) : null;
            if (text == null || text.trim().isEmpty()) {
                missing.add(lang);
            }
        }

        return missing;
    }

    /**
     * Merge two translation maps (second map overwrites first)
     */
    public static Map<String, String> mergeTranslations(Map<String, String> base, Map<String, String> overlay) {
        if (base == null) base = new HashMap<>();
        if (overlay == null) return base;

        Map<String, String> merged = new HashMap<>(base);
        overlay.forEach((key, value) -> {
            if (value != null && !value.trim().isEmpty()) {
                merged.put(key, value);
            }
        });

        return merged;
    }

    /**
     * Convert legacy primary/secondary name format to translation map
     * For backward compatibility with old data structure
     */
    public static Map<String, String> fromLegacyNames(String primaryName, String secondaryName) {
        Map<String, String> translations = new HashMap<>();
        translations.put("en", primaryName != null ? primaryName : "");
        translations.put("ar", secondaryName != null ? secondaryName : "");
        return translations;
    }

    /**
     * Convert legacy primary/secondary description format to translation map
     * For backward compatibility with old data structure
     */
    public static Map<String, String> fromLegacyDescriptions(String primaryDescription, String secondaryDescription) {
        Map<String, String> translations = new HashMap<>();
        translations.put("en", primaryDescription != null ? primaryDescription : "");
        translations.put("ar", secondaryDescription != null ? secondaryDescription : "");
        return translations;
    }
}
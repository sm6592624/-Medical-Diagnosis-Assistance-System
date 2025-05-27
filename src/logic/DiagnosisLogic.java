package src.logic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for medical diagnosis logic.
 */
public class DiagnosisLogic {
    // Prevent instantiation
    private DiagnosisLogic() {}

    /**
     * Diagnoses based on a list of symptoms.
     * @param symptoms List of symptom strings
     * @return Diagnosis and recommendation
     */
    public static String diagnose(List<String> symptoms) {
        String combined = String.join(", ", symptoms).toLowerCase();
        // Extensive map of symptom patterns to diagnosis
        Map<String[], String> diagnosisMap = new HashMap<>();
        diagnosisMap.put(new String[]{"fever", "cough", "shortness of breath"},
                "Possible diagnosis: COVID-19 or Pneumonia.\nRecommendation: Isolate, monitor oxygen, seek medical attention if symptoms worsen.");
        diagnosisMap.put(new String[]{"fever", "cough"},
                "Possible diagnosis: Flu or Common Cold.\nRecommendation: Rest, hydration, and consult a doctor if severe.");
        diagnosisMap.put(new String[]{"headache", "nausea"},
                "Possible diagnosis: Migraine.\nRecommendation: Avoid light/noise, rest, and use migraine-specific medication.");
        diagnosisMap.put(new String[]{"chest pain", "shortness of breath"},
                "Possible diagnosis: Heart condition or Asthma attack.\nRecommendation: Seek immediate medical attention.");
        diagnosisMap.put(new String[]{"chest pain"},
                "Possible diagnosis: Heart condition.\nRecommendation: Seek immediate medical attention.");
        diagnosisMap.put(new String[]{"sore throat", "cough"},
                "Possible diagnosis: Throat infection or Allergy.\nRecommendation: Gargle, avoid irritants, consult a doctor if persistent.");
        diagnosisMap.put(new String[]{"fatigue", "pale skin"},
                "Possible diagnosis: Anemia.\nRecommendation: Get a blood test and consult a healthcare provider.");
        diagnosisMap.put(new String[]{"nausea", "vomiting", "diarrhea"},
                "Possible diagnosis: Food poisoning or Gastroenteritis.\nRecommendation: Hydrate, rest, seek care if severe.");
        diagnosisMap.put(new String[]{"runny nose", "sneezing", "itchy eyes"},
                "Possible diagnosis: Allergic Rhinitis.\nRecommendation: Avoid allergens, consider antihistamines.");
        diagnosisMap.put(new String[]{"shortness of breath"},
                "Possible diagnosis: Asthma or Respiratory issue.\nRecommendation: Use inhaler if prescribed, seek urgent care if severe.");
        diagnosisMap.put(new String[]{"fatigue"},
                "Possible diagnosis: Fatigue (non-specific).\nRecommendation: Rest, hydrate, and monitor for other symptoms.");

        // Track full and partial matches
        StringBuilder possibleConditions = new StringBuilder();
        @SuppressWarnings("unused")
        boolean foundFullMatch = false;
        for (Map.Entry<String[], String> entry : diagnosisMap.entrySet()) {
            boolean match = true;
            int matchCount = 0;
            for (String symptom : entry.getKey()) {
                if (!combined.contains(symptom)) {
                    match = false;
                } else {
                    matchCount++;
                }
            }
            if (match) {
                foundFullMatch = true;
                return entry.getValue();
            } else if (matchCount > 0) {
                // Partial match, suggest as possible
                String diag = entry.getValue().split("\\.\\n")[0];
                possibleConditions.append("- ").append(diag).append("\n");
            }
        }
        // Urgent symptoms override
        if (combined.contains("chest pain") || combined.contains("shortness of breath")) {
            return "Warning: Severe symptoms detected (chest pain or shortness of breath).\nRecommendation: Seek immediate medical attention.";
        }
        // Improved fallback logic for insufficient data
        if (symptoms.isEmpty()) {
            return "No symptoms provided.\nRecommendation: Please select at least one symptom to get a diagnosis.";
        } else if (symptoms.size() == 1) {
            return "Only one symptom provided.\nRecommendation: More symptoms may help provide a more accurate diagnosis. Please consult a healthcare provider.";
        } else if (possibleConditions.length() > 0) {
            return "Possible conditions based on your symptoms:\n" + possibleConditions + "Recommendation: For a precise diagnosis, consult a healthcare provider.";
        } else {
            return "Symptoms do not match known patterns.\nRecommendation: Please consult a healthcare provider for a thorough evaluation.";
        }
    }

    /**
     * Saves the diagnosis history to a file.
     * @param symptoms List of symptoms
     * @param diagnosis Diagnosis result
     */
    public static void saveHistory(List<String> symptoms, String diagnosis) {
        Path historyPath = Paths.get("data", "history.txt");
        try {
            Files.createDirectories(historyPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(historyPath, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {
                writer.write("Symptoms: " + String.join(", ", symptoms) + "\n");
                writer.write("Diagnosis: " + diagnosis + "\n");
                writer.write("----\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }
}
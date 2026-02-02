package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    public static Properties prop = new Properties();

    public static void loadProperties() {

        System.out.println("Loading properties...");

        try (InputStream input = Utils.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null)  {
                System.out.println("Properties file doesn't exist");
                return;
            }

            prop.load(input);
            System.out.println("Properties loaded successfully\n");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double calculateCosineSimiliarity(float[] vectorA, float[] vectorB) {
        if ((vectorA == null) || (vectorB == null)) {
            return 0;
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}

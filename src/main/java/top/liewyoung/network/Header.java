package top.liewyoung.network;

/**
 *
 * @author LiewYoung
 * @since 2025/12/1
 */

public record Header(String apiKey, String model) {
    @Override
    public String toString() {
        return "API Key: " + apiKey + "\nModel: " + model;
    }
}

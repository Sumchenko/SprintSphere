package ru.sfedu.sprintspherepk.util;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class YamlUtil {
    public static Map<String, Object> loadYaml(String path) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = YamlUtil.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new RuntimeException("YAML file not found: " + path);
            }
            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML file: " + e.getMessage());
        }
    }
}
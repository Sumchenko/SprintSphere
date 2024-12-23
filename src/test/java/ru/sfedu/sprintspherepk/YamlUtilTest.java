package ru.sfedu.sprintspherepk;

import org.junit.Test;
import ru.sfedu.sprintspherepk.util.YamlUtil;

import java.util.*;
import static org.junit.Assert.assertEquals;

public class YamlUtilTest {
    @Test
    public void testReadYamlAsListAndMap() {
        Map<String, Object> yamlData = YamlUtil.loadYaml("config.yml");

        // Получение List<String>
        List<String> list = (List<String>) yamlData.get("list");
        assertEquals(Arrays.asList("one", "two", "three"), list);

        // Получение Map<Integer, String>
        Map<String, String> map = (Map<String, String>) yamlData.get("map");
        assertEquals("One", map.get(1));
        assertEquals("Two", map.get(2));
    }
}

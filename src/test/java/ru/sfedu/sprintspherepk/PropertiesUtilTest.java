package ru.sfedu.sprintspherepk;

import org.junit.Test;
import ru.sfedu.sprintspherepk.util.PropertiesUtil;

import java.util.*;
import static org.junit.Assert.assertEquals;

public class PropertiesUtilTest {
    @Test
    public void testReadPropertiesAsListAndMap() {
        Properties properties = PropertiesUtil.loadProperties("src/main/resources/config.properties");

        // Получение List<String>
        String listValue = properties.getProperty("list.value1");
        List<String> list = Arrays.asList(listValue.split(","));
        assertEquals(Arrays.asList("one", "two", "three"), list);

        // Получение Map<Integer, String>
        String mapValue = properties.getProperty("map.key1");
        Map<Integer, String> map = new HashMap<>();
        for (String pair : mapValue.split(",")) {
            String[] keyValue = pair.split(":");
            map.put(Integer.parseInt(keyValue[0]), keyValue[1]);
        }
        assertEquals("One", map.get(1));
        assertEquals("Two", map.get(2));
    }
}

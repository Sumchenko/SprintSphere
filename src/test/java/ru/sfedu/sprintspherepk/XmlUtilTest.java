package ru.sfedu.sprintspherepk;

import org.junit.Test;
import org.w3c.dom.Document;
import ru.sfedu.sprintspherepk.util.XmlUtil;

import java.util.*;
import static org.junit.Assert.assertEquals;

public class XmlUtilTest {
    @Test
    public void testReadXmlAsListAndMap() {
        Document xml = XmlUtil.loadXml("src/main/resources/config.xml");

        // Получение List<String>
        String listValue = xml.getElementsByTagName("list").item(0).getTextContent();
        List<String> list = Arrays.asList(listValue.split(","));
        assertEquals(Arrays.asList("one", "two", "three"), list);

        // Получение Map<Integer, String>
        String mapValue = xml.getElementsByTagName("map").item(0).getTextContent();
        Map<Integer, String> map = new HashMap<>();
        for (String pair : mapValue.split(",")) {
            String[] keyValue = pair.split(":");
            map.put(Integer.parseInt(keyValue[0]), keyValue[1]);
        }
        assertEquals("One", map.get(1));
        assertEquals("Two", map.get(2));
    }
}
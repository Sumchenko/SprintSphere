package ru.sfedu.sprintspherepk;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import ru.sfedu.sprintspherepk.api.MongoDBUtil;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MongoDBUtilTest {

    private MongoDBUtil mongoDBUtil;
    private HistoryContent testHistoryContent;

    @Before
    public void setUp() {
        mongoDBUtil = new MongoDBUtil();

        testHistoryContent = new HistoryContent();
        testHistoryContent.setClassName("TestClass");
        testHistoryContent.setActor("JohnDoe");
        testHistoryContent.setMethodName("someMethod");
        testHistoryContent.setStatus(Status.SUCCESS);
    }

    @After
    public void tearDown() {
        try {
            mongoDBUtil.delete(testHistoryContent.getId());
        } catch (Exception e) {
            System.err.println("Ошибка при очистке данных после теста: " + e.getMessage());
        }

        try {
            if (mongoDBUtil != null) {
                mongoDBUtil.close();
            }
        } catch (Exception e) {
            System.err.println("Ошибка при отключении соединения: " + e.getMessage());
        }
    }

    @Test
    public void testSave() {
        try {
            mongoDBUtil.save(testHistoryContent);

            Optional<HistoryContent> result = mongoDBUtil.findById(testHistoryContent.getId());

            assertTrue(result.isPresent());
            assertEquals(testHistoryContent.getId(), result.get().getId());
            assertEquals(testHistoryContent.getClassName(), result.get().getClassName());
        } catch (Exception e) {
            fail("Произошла ошибка в тесте testSave: " + e.getMessage());
        }
    }

    @Test
    public void testFindByActor() {
        try {
            mongoDBUtil.save(testHistoryContent);

            List<HistoryContent> result = mongoDBUtil.findByActor("JohnDoe");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(testHistoryContent.getActor(), result.get(0).getActor());
        } catch (Exception e) {
            fail("Произошла ошибка в тесте testFindByActor: " + e.getMessage());
        }
    }

    @Test
    public void testFindById() {
        try {
            mongoDBUtil.save(testHistoryContent);

            Optional<HistoryContent> result = mongoDBUtil.findById(testHistoryContent.getId());

            assertTrue(result.isPresent());
            assertEquals(testHistoryContent.getId(), result.get().getId());
        } catch (Exception e) {
            fail("Произошла ошибка в тесте testFindById: " + e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        try {
            mongoDBUtil.save(testHistoryContent);

            testHistoryContent.setClassName("UpdatedClass");
            testHistoryContent.setActor("UpdatedActor");
            testHistoryContent.setMethodName("updatedMethod");
            testHistoryContent.setStatus(Status.FAULT);
            mongoDBUtil.update(testHistoryContent.getId(), testHistoryContent);

            Optional<HistoryContent> result = mongoDBUtil.findById(testHistoryContent.getId());

            assertTrue(result.isPresent());
            assertEquals("UpdatedClass", result.get().getClassName());
            assertEquals("UpdatedActor", result.get().getActor());
            assertEquals("updatedMethod", result.get().getMethodName());
            assertEquals(Status.FAULT, result.get().getStatus());
        } catch (Exception e) {
            fail("Произошла ошибка в тесте testUpdate: " + e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        try {
            mongoDBUtil.save(testHistoryContent);

            Optional<HistoryContent> resultBeforeDelete = mongoDBUtil.findById(testHistoryContent.getId());
            assertTrue(resultBeforeDelete.isPresent());

            mongoDBUtil.delete(testHistoryContent.getId());

            Optional<HistoryContent> resultAfterDelete = mongoDBUtil.findById(testHistoryContent.getId());
            assertFalse(resultAfterDelete.isPresent());
        } catch (Exception e) {
            fail("Произошла ошибка в тесте testDelete: " + e.getMessage());
        }
    }
}

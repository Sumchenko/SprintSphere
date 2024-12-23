package ru.sfedu.sprintspherepk.api;

import ru.sfedu.sprintspherepk.models.HistoryContent;

import java.util.Optional;

public interface IDataProvider {
    void saveRecord(HistoryContent record) throws Exception;
    void deleteRecord(String id) throws Exception;
    Optional<HistoryContent> getRecordById(String id) throws Exception;
    void initDataSource() throws Exception;
}
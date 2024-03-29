package cn.rypacker.productkeymanager.repositories;

import cn.rypacker.productkeymanager.entity.JsonRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JsonRecordRepository extends
        CrudRepository<JsonRecord, Long>,
        PagingAndSortingRepository<JsonRecord, Long>,
        JpaSpecificationExecutor<JsonRecord> {

    JsonRecord findByProductKey(String productKey);
}

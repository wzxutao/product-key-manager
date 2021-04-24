package cn.rypacker.productkeymanager.repositories;

import cn.rypacker.productkeymanager.models.JsonRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JsonRecordRepository extends CrudRepository<JsonRecord, Long> {

    List<JsonRecord> findByProductKey(String productKey);
}

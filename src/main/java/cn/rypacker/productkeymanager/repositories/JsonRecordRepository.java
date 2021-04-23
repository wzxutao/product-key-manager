package cn.rypacker.productkeymanager.repositories;

import cn.rypacker.productkeymanager.models.JsonRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JsonRecordRepository extends CrudRepository<JsonRecord, Long> {

}

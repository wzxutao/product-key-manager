package cn.rypacker.productkeymanager.repositories;

import cn.rypacker.productkeymanager.models.JsonRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JsonRecordRepository extends CrudRepository<JsonRecord, Long> {

    List<JsonRecord> findByProductKey(String productKey);

    @Query(value = "SELECT * FROM json_record WHERE created_milli BETWEEN ?1 and ?2",
        nativeQuery = true)
    List<JsonRecord> findByMilliCreatedBetween(Long from, Long to);

    @Query(value = "SELECT * FROM json_record WHERE created_milli BETWEEN ?1 and ?2 " +
            "AND status == ?3",
            nativeQuery = true)
    List<JsonRecord> findByMilliCreatedBetweenAndStatusEquals(Long from, Long to, int status);
}

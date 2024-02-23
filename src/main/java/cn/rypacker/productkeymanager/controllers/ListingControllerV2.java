package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.common.RecordStatus;
import cn.rypacker.productkeymanager.dto.JsonRecordDto;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.DatetimeUtil;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.auth.AdminAuth;
import cn.rypacker.productkeymanager.services.auth.NormalAccountAuth;
import cn.rypacker.productkeymanager.specification.JsonRecordSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.rypacker.productkeymanager.common.Constants.*;

@RestController
@RequestMapping("/normal/listing/v2")
public class ListingControllerV2 {

    @Autowired
    private JsonRecordRepository jsonRecordRepository;
    @Autowired
    private NormalAccountAuth normalAccountAuth;
    @Autowired
    private AdminAuth adminAuth;

    @GetMapping("/my-today-records")
    public ResponseEntity<?> getMyTodayRecords(@RequestParam(value = "onlyNormal", required = false) Boolean onlyNormal,
                                               @RequestParam(value = "onlyDeleted", required = false) Boolean onlyDeleted){
        var fromS = DatetimeUtil.getTodayEpochSeconds(true);
        var toS = DatetimeUtil.getTodayEpochSeconds(false);

        var specs = JsonRecordSpecs.createdMilliBetween(fromS * 1000, (toS * 1000) + 999);
        if(onlyNormal != null && onlyNormal) {
            specs = specs.and(JsonRecordSpecs.statusEquals(RecordStatus.NORMAL));
        }else if(onlyDeleted != null && onlyDeleted) {
            specs = specs.and(JsonRecordSpecs.statusEquals(RecordStatus.DELETED));
        }
        var authToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if(!adminAuth.isValidToken(authToken)){
            String username = normalAccountAuth.getUsername(authToken);
            if(username == null) return ResponseEntity.badRequest().body("Invalid token");
            specs = specs.and(JsonRecordSpecs.usernameEquals(username));
        }
        var records = jsonRecordRepository.findAll(specs);
        return ResponseEntity.ok(records.stream().map(JsonRecordDto::fromEntity).collect(Collectors.toList()));
    }

    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> batchDeleteMyTodayRecords(@RequestParam("productKey") List<String> productKeys) {
        if(productKeys == null || productKeys.isEmpty())
            return ResponseEntity.badRequest().body("No product keys provided");

        var fromS = DatetimeUtil.getTodayEpochSeconds(true);
        var toS = DatetimeUtil.getTodayEpochSeconds(false);

        var specs = JsonRecordSpecs.createdMilliBetween(fromS * 1000, (toS * 1000) + 999)
                .and(JsonRecordSpecs.statusEquals(RecordStatus.NORMAL))
                .and(JsonRecordSpecs.productKeyIn(productKeys));
        var authToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if(!adminAuth.isValidToken(authToken)){
            String username = normalAccountAuth.getUsername(authToken);
            if(username == null) return ResponseEntity.badRequest().body("Invalid token");
            specs = specs.and(JsonRecordSpecs.usernameEquals(username));
        }
        var records = jsonRecordRepository.findAll(specs);
        records.forEach(r -> r.setStatus(RecordStatus.DELETED));
        jsonRecordRepository.saveAll(records);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-record")
    public ResponseEntity<?> updateRecord(@Valid @RequestBody JsonRecordDto dto) {

        var entityOptional = jsonRecordRepository.findById(dto.getId());
        if(entityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var entity = entityOptional.get();
        String username = JSONUtil.getFirstValue(entity.getJsonString(), RECORD_KEY_USERNAME);

        // normal account access control
        var authToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if(!adminAuth.isValidToken(authToken)) {
            String requestUsername = normalAccountAuth.getUsername(authToken);
            if(requestUsername == null) return ResponseEntity.status(403).body("Invalid token");
            if(!requestUsername.equals(username)) return ResponseEntity.status(400).body("You are not allowed to update this record");

            if(System.currentTimeMillis() - entity.getCreatedMilli() > 1000 * 60 * 60 * 24) {
                return ResponseEntity.status(400).body("You are not allowed to update this record");
            }
        }

        entity.setStatus(RecordStatus.parse(dto.getStatus()));
        dto.getExpandedAllFields().put(RECORD_KEY_USERNAME, username != null ? username : USERNAME_RECORD_VALUE_ADMIN);
        dto.getExpandedAllFields().put(RECORD_KEY_COMMENT, dto.getComment());
        dto.getExpandedAllFields().put(RECORD_KEY_DATE, dto.getProductKey().substring(0, KeyGenerator.DATE_LENGTH));

        entity.setJsonString(JSONUtil.toStringFrom(dto.getExpandedAllFields()));
        jsonRecordRepository.save(entity);

        return ResponseEntity.ok().build();
    }
}

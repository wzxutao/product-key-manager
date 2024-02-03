package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.common.RecordStatus;
import cn.rypacker.productkeymanager.dto.JsonRecordDto;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.DatetimeUtil;
import cn.rypacker.productkeymanager.services.auth.AdminAuth;
import cn.rypacker.productkeymanager.services.auth.NormalAccountAuth;
import cn.rypacker.productkeymanager.specification.JsonRecordSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/normal/listing")
public class ListingControllerV2 {

    @Autowired
    private JsonRecordRepository jsonRecordRepository;
    @Autowired
    private NormalAccountAuth normalAccountAuth;
    @Autowired
    private AdminAuth adminAuth;

    @GetMapping("/my-today-records")
    public ResponseEntity<List<JsonRecordDto>> getMyTodayRecords() {
        var fromS = DatetimeUtil.getTodayEpochSeconds(true);
        var toS = DatetimeUtil.getTodayEpochSeconds(false);

        var specs = JsonRecordSpecs.createdMilliBetween(fromS * 1000, (toS * 1000) + 999)
                .and(JsonRecordSpecs.statusEquals(RecordStatus.NORMAL));
        var authToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if(!adminAuth.isValidToken(authToken)){
            String username = normalAccountAuth.getUsername(authToken);
            specs = specs.and(JsonRecordSpecs.usernameEquals(username));
        }
        var records = jsonRecordRepository.findAll(specs);
        return ResponseEntity.ok(records.stream().map(JsonRecordDto::fromEntity).collect(Collectors.toList()));
    }
}

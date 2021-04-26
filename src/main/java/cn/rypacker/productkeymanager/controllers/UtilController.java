package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.services.DatetimeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

@RequestMapping("/util")
@Controller
public class UtilController {

    @GetMapping("/datetimeToSeconds")
    public ResponseEntity<?> getEpochSeconds(@RequestParam(value = "datetime") String datetime){
        try{
            return new ResponseEntity<>(DatetimeUtil.finalDateToEpochSeconds(datetime), HttpStatus.OK);
        }catch (DateTimeParseException e){
            return new ResponseEntity<>("Illegal format", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/secondsToDatetime")
    public ResponseEntity<?> getDatetime(@RequestParam(value = "seconds") int epochSeconds){
        if(epochSeconds < 0 ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(DatetimeUtil.epochSecondsToFinalDate(epochSeconds), HttpStatus.OK);
    }

    @GetMapping("/datetime-today")
    public ResponseEntity<?> getTodayDatetime(@RequestParam(value = "fromStart") boolean fromStart){
        var ldt = LocalDateTime.of(
                LocalDate.now(),
                fromStart ? LocalTime.of(0, 0, 0) :
                        LocalTime.of(23, 59, 59)
        );
        var zdt = ldt.atZone(ZoneId.systemDefault());
        var seconds = zdt.toEpochSecond();

        return new ResponseEntity<>(DatetimeUtil.epochSecondsToFinalDate(seconds), HttpStatus.OK);
    }

}

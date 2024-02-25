package cn.rypacker.productkeymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoBackupConfigDto {

    private int hour;
    private int minute;

}

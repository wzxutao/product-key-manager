package cn.rypacker.productkeymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyGenStats {

    private Integer keyLength;
    private Long totalKeyCount;
    private Long usedKeyCount;
    private Long blackListedKeyCount;
    private Long remainingKeyCount;
}

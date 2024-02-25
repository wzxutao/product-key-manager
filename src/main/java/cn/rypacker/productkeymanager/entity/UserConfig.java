package cn.rypacker.productkeymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserConfig {

    @Data
    @NoArgsConstructor
    public static class Auth {

        @Data
        @NoArgsConstructor
        public static class Normal {
            private int validDays = 30;
        }

        @Data
        @NoArgsConstructor
        public static class Admin {
            private int validMinutes = 30;
        }

        private Normal normal = new Normal();
        private Admin admin = new Admin();
    }

    /** ! values should not be set directly but via KeyGenerator as it caches the values*/
    @Data
    @NoArgsConstructor
    public static class Key {
        private int length = 15;
        private List<String> blacklist = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class Record {
        private List<String> mandatoryFields = new ArrayList<>();
    }

    /** Should be accessed via NormalAccountRepository */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Account {
        private String username;
        private String hash;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Backup {
        private int hour = 15;
        private int minute;
    }

    private Auth auth = new Auth();
    private Key key = new Key();
    private Record record = new Record();
    private List<Account> accounts = new ArrayList<>();
    private Backup backup = new Backup();
}

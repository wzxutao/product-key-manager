package cn.rypacker.productkeymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Data
    @NoArgsConstructor
    public static class Key {
        private int length = 15;
    }

    @Data
    @NoArgsConstructor
    public static class Record {
        private List<String> mandatoryFields = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Account {
        private String username;
        private String hash;
    }

    private Auth auth = new Auth();
    private Key key = new Key();
    private Record record = new Record();
    private List<Account> accounts = new ArrayList<>();
}

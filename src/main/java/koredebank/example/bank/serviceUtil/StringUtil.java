package koredebank.example.bank.serviceUtil;

public class StringUtil {
    public static boolean isNotBlank(String value) {
        if(value.isBlank() || value.isEmpty()){
            return false;
        }
        return true;
    }
    public static boolean isBlank(String value) {
        if(value.isBlank() || value.isEmpty()){
            return true;
        }
        return false;
    }
}

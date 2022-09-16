package koredebank.example.bank.serviceUtil;

import java.util.UUID;

public class IdGenerator {
    public static String generateId(){
        UUID id=UUID.randomUUID();
        return id.toString();
    }
}

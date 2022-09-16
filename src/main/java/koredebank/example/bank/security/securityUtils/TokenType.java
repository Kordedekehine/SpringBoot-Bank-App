package koredebank.example.bank.security.securityUtils;

public enum TokenType {

    BEARER_TOKEN;

    @Override
    public String toString() {
        switch (this){
            case BEARER_TOKEN:return "Bearer";
            default: return null;
        }

    }
}

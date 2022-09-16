package koredebank.example.bank.security.securityUtils;

public class JWTToken {
    private String accessToken;
    private TokenType tokenType = TokenType.BEARER_TOKEN;


    public JWTToken(String loginToken) {
        accessToken = loginToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

}

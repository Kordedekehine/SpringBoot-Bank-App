package koredebank.example.bank.tool;

import com.mifmif.common.regex.Generex;

import static koredebank.example.bank.util.constants.ACCOUNT_NUMBER_PATTERN_STRING;
import static koredebank.example.bank.util.constants.SORT_CODE_PATTERN_STRING;

public class AccountGenerator {

    Generex sortCodeGenerex = new Generex(SORT_CODE_PATTERN_STRING);
    Generex accountNumberGenerex = new Generex(ACCOUNT_NUMBER_PATTERN_STRING);

    public AccountGenerator() {
    }

   public String generateSortCode(){
        return sortCodeGenerex.random();
   }

   public String generateAccountNumber(){
        return accountNumberGenerex.random();
   }
}

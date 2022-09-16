package koredebank.example.bank.payload;


import koredebank.example.bank.dto.UserCreateAccountRequestDto;
import koredebank.example.bank.dto.UserTransferFundsRequestDto;
import koredebank.example.bank.util.constants;

public class Validator {

    public static boolean isSearchCriteriaValid(UserAccountDto userAccountDto) {
        return constants.SORT_CODE_PATTERN.matcher(userAccountDto.getSortCode()).find() &&
                constants.ACCOUNT_NUMBER_PATTERN.matcher(userAccountDto.getAccountNumber()).find();
    }


    public static boolean isAccountNoValid(String accountNo) {
        return constants.ACCOUNT_NUMBER_PATTERN.matcher(accountNo).find();
    }

    public static boolean isCreateAccountCriteriaValid(UserCreateAccountRequestDto userCreateAccountRequestDto) {
        return (!userCreateAccountRequestDto.getBankName().isBlank() && !userCreateAccountRequestDto.getOwnerName().isBlank());
    }

//    public static boolean isSearchCriteriaValid(AccountInput accountInput) {
//        return constants.SORT_CODE_PATTERN.matcher(accountInput.getSortCode()).find() &&
//                constants.ACCOUNT_NUMBER_PATTERN.matcher(accountInput.getAccountNumber()).find();
//    }

    public static boolean isSearchTransactionValid(TransferDto transferDto) {
        // TODO Add checks for large amounts; consider past history of account holder and location of transfers

        if (!isSearchCriteriaValid(transferDto.getSourceAccount()))
            return false;

        if (!isSearchCriteriaValid(transferDto.getTargetAccount()))
            return false;

        if (transferDto.getSourceAccount().equals(transferDto.getTargetAccount()))
            return false;

        return true;
    }


}

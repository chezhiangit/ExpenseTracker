package paramtech.com.exptracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 324590 on 11/30/2015.
 */
public class LoadAccountsParam extends ParamBaseClass {

    private List<Accounts> accountsList;
    private String accountCode;

    public LoadAccountsParam(String accountCode){
        super();
        this.accountCode=accountCode;
    }

    public List<Accounts> getAccountsList(){
        return accountsList;
    }
    public void setAccountsList(List<Accounts> list){
        accountsList=list;
    }
    public String getAccountCode(){
        return accountCode;
    }
}

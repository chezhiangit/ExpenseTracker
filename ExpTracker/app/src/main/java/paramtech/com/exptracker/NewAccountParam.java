package paramtech.com.exptracker;

/**
 * Created by 324590 on 11/29/2015.
 */
public class NewAccountParam extends ParamBaseClass {

    int accessorCode;
    String accountName;
    String accountCode;


public NewAccountParam(String accountCode,String accountName,int accessorCode){
    this.accountName=accountName;
    this.accessorCode=accessorCode;
    this.accountCode=accountCode;
    }
}

package paramtech.com.exptracker;

/**
 * Created by 324590 on 11/30/2015.
 */
public class EditAccountParam extends ParamBaseClass {

    int adapterPosition;
    String accountName;
    String accountCode;

    public EditAccountParam(String accountCode,String accountName){
        this.accountName=accountName;
        this.accountCode=accountCode;
        this.adapterPosition=0;
    }
}

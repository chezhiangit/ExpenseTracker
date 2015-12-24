package paramtech.com.exptracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 324590 on 11/23/2015.
 */
public class MonthlyExpense  implements Comparable<MonthlyExpense> {
    private String code;
    private String name;
    private int amount;
    private String date;
    private String accountCode;


    public MonthlyExpense(String code,String name,int amount,String accountCode,String date){
        this.code=code;
        this.name=name;
        this.amount=amount;
        this.accountCode=accountCode;
        this.date=date;

//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
//        this.date=sdf.format(new Date());


    }

    public static List<MonthlyExpense> CreateExpenseList(){
        List<MonthlyExpense> tempList = new ArrayList<MonthlyExpense>();

//        for(int i=0;i<10;i++){
//            Accounts t = new Accounts(i,"Test"+i);
//            tempList.add(t);
//            //t=null;
//        }

        return tempList;

    }
    @Override
    public int compareTo(MonthlyExpense acc){
        return this.code.compareTo(acc.code);
    }
    public String getCode(){
        return code;
    }
    public String getName(){
        return name;
    }
    public String getAmt(){
        return amount+"";
    }
    public String getDate(){
        return date;
    }
    public String getAccountCode(){
        return accountCode;
    }
}

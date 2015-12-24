package paramtech.com.exptracker;

import java.util.List;

/**
 * Created by 324590 on 12/8/2015.
 */
public class LoadExpensesParam extends ParamBaseClass {

    private List<MonthlyExpense> expensesList;
    private String accountCode;
    int totalExpense;

    public LoadExpensesParam(String accountCode){
        super();
        this.accountCode=accountCode;
        this.totalExpense=0;
    }

    public List<MonthlyExpense> getExpensesList(){
        return expensesList;
    }
    public void setExpensesList(List<MonthlyExpense> list){
        expensesList=list;
    }
    public String getAccountCode(){
        return accountCode;
    }
}

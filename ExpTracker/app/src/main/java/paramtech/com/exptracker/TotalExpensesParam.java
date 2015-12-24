package paramtech.com.exptracker;

import android.widget.Toast;

/**
 * Created by 324590 on 12/17/2015.
 */
public class TotalExpensesParam extends ParamBaseClass {
    String monthKey;
    int totalExpense;

    public TotalExpensesParam(String monthKey){
        this.monthKey=monthKey;
        this.totalExpense=0;
    }
}

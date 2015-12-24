package paramtech.com.exptracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class MonthView extends AppCompatActivity {

    MonthlyExpenseAdapter mMonthlyExpenseAdapter;
    String accountCode;
    TextView monthviwHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountCode=getIntent().getExtras().getString("accountCode");
        setContentView(R.layout.activity_month__view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mMonthlyExpenseAdapter = new MonthlyExpenseAdapter(MonthlyExpense.CreateExpenseList());
        RecyclerView rcView = (RecyclerView)findViewById(R.id.rvMonthView);
        rcView.setAdapter(mMonthlyExpenseAdapter);
        rcView.setLayoutManager(new LinearLayoutManager(this));

        monthviwHeader=(TextView)findViewById(R.id.monthviewHeader);
        monthviwHeader.setText(getIntent().getExtras().getString("accountName"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addExp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                getExpenses();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMonthlyExpenseAdapter.setOnItemClickListener(new MonthlyExpenseAdapter.OnItemClickListener() {
            @Override
            public void onDeleteButtonClicked(int position) {
                deleteExpense(position);
            }

            @Override
            public void onEditButtonClicked(int position) {
                editExpense(position);
            }
        });

        loadExpenses(accountCode);
    }

    private void getExpenses(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View expEntryDialog = factory.inflate(R.layout.new_expense_dialog, null);
        final MonthView currentActivity=this;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        final String[] expDate=new String[1];
        expDate[0]=sdf.format(new Date()).toString();//.replaceAll("/", "");
        CalendarView calendarView = (CalendarView)expEntryDialog.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Calendar now = new GregorianCalendar(year,month,day);
                DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                expDate[0] = df.format(now.getTime()).toString();
            }
        });

        new AlertDialog.Builder(MonthView.this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.exp_dialog_title)
                .setView(expEntryDialog)
                .setPositiveButton(R.string.exp_dialog_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Collections.sort(accountList);

                                EditText expNameField = (EditText) expEntryDialog.findViewById(R.id.expName);
                                String expName = (String) expNameField.getText().toString();

                                expName=String.valueOf(expName.charAt(0)).toUpperCase()+expName.subSequence(1,expName.length());

                                EditText expAmtField = (EditText) expEntryDialog.findViewById(R.id.expAmt);
                                int expAmt = 0;
                                try {
                                    expAmt = Integer.parseInt(expAmtField.getText().toString());
                                } catch (NumberFormatException nfe) {
                                    System.out.println("could not parse " + nfe);
                                }
                                if ((expName.trim()).isEmpty()) {
                                    return;
                                }

                                InsertExpenseParam insertExpenseParam = new InsertExpenseParam();
                                insertExpenseParam.successFlag = false;
                                insertExpenseParam.currentActivity = currentActivity;
                                insertExpenseParam.context = getApplicationContext();
                                insertExpenseParam.accountCode = accountCode;
                                insertExpenseParam.expenseAmount = expAmt;
                                insertExpenseParam.expenseName = expName;
                                insertExpenseParam.expenseCode = accountCode +"/"+Utility.getRandomNumber(); //mMonthlyExpenseAdapter.getItemCount() + 1;
                                insertExpenseParam.expenseDate = expDate[0];
                                insertExpenseParam.func = "insertExpense";
                                insertExpenseParam.callBack = "insertExpenseHandler";

                                new ExpenseDBTaskHandler().execute(insertExpenseParam);

//                                if (mMonthlyExpenseAdapter.getItemCount() > 0) {
//                                    mMonthlyExpenseAdapter.addNewExpense(new MonthlyExpense(mMonthlyExpenseAdapter.getMaxCode() + 1, expName, expAmt));
//                                    mMonthlyExpenseAdapter.notifyDataSetChanged();
//                                } else {
//                                    mMonthlyExpenseAdapter.addNewExpense(new MonthlyExpense(1, expName, expAmt));
//                                    mMonthlyExpenseAdapter.notifyDataSetChanged();
//                                }

                            }
                        }

                )
                            .

                    setNegativeButton(R.string.exp_dialog_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                                }
                            }

                    ).

                show();//create();
    }
    public void insertExpenseHandler(ParamBaseClass param){

        if(param.successFlag==true){
            Utility.showAlert(this,"Information!","Expense added successfully.");
            loadExpenses(accountCode);
        }
        else{
            Utility.showAlert(this,"Information!","Problem adding Expense. Try Again.");
        }

    }


    public void loadExpenses(String accountCode){
            LoadExpensesParam loadExpensesParam=new LoadExpensesParam(accountCode);
            loadExpensesParam.successFlag=false;
            loadExpensesParam.currentActivity=this;
            loadExpensesParam.context=getApplicationContext();
            loadExpensesParam.func="loadExpenses";
            loadExpensesParam.callBack="loadExpenseHandler";
            loadExpensesParam.setExpensesList(mMonthlyExpenseAdapter.getmMonthlyExpensesList());
            new ExpenseDBTaskHandler().execute(loadExpensesParam);
    }

    public void loadExpenseHandler(ParamBaseClass param){
        if(param.successFlag==true) {
            mMonthlyExpenseAdapter.notifyDataSetChanged();
        }
    }

    public void deleteExpense(int position){
        DeleteExpenseParam dp = new DeleteExpenseParam();
        dp.successFlag=false;
        dp.callBack="deleteExpenseHandler";
        dp.func="deleteExpense";
        dp.currentActivity=this;
        dp.context=getApplicationContext();
        dp.expCode=mMonthlyExpenseAdapter.getmMonthlyExpensesList().get(position).getCode();
        dp.accountCode=mMonthlyExpenseAdapter.getmMonthlyExpensesList().get(position).getAccountCode();

        new ExpenseDBTaskHandler().execute(dp);
    }

    public void deleteExpenseHandler(ParamBaseClass param){
        if(param.successFlag==true){
            Utility.showAlert(this,"Confirmation!","Expense deleted successfully.");
            DeleteExpenseParam dp = (DeleteExpenseParam)param;
            loadExpenses(dp.accountCode);
        }
        else{
            Utility.showAlert(this,"Error","Problem delete expense. Try again.");
        }
    }
    public void editExpense(final int position){

        LayoutInflater factory = LayoutInflater.from(this);
        final Activity currentActivity = this;
        final View textEntryView = factory.inflate(R.layout.new_expense_dialog, null);
        EditText newExpenseNameField = (EditText)textEntryView.findViewById(R.id.expName);
        EditText newExpenseAmtField = (EditText)textEntryView.findViewById(R.id.expAmt);
        newExpenseNameField.setText(mMonthlyExpenseAdapter.getmMonthlyExpensesList().get(position).getName());
        newExpenseAmtField.setText(mMonthlyExpenseAdapter.getmMonthlyExpensesList().get(position).getAmt());

        final String[] expDate=new String[1];
        expDate[0]=mMonthlyExpenseAdapter.getmMonthlyExpensesList().get(position).getDate();

        CalendarView calendarView = (CalendarView)textEntryView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Calendar now = new GregorianCalendar(year, month, day);
                DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                expDate[0] = df.format(now.getTime()).toString();
            }
        });

        AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.exp_dialog_title)
                .setView(textEntryView)
                .setPositiveButton(R.string.exp_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Collections.sort(accountList);

                        EditText expName = (EditText) textEntryView.findViewById(R.id.expName);
                        String newExpName = (String) expName.getText().toString();
                        newExpName=String.valueOf(newExpName.charAt(0)).toUpperCase()+newExpName.subSequence(1,newExpName.length());

                        EditText expAmt=(EditText) textEntryView.findViewById(R.id.expAmt);
                        String newExpAmt = (String) expAmt.getText().toString();

                        if ((newExpName.trim()).isEmpty()) return;

                        EditExpenseParam ep = new EditExpenseParam();
                        ep.successFlag=false;
                        ep.callBack="editExpenseHandler";
                        ep.func="editExpense";
                        ep.expCode=mMonthlyExpenseAdapter.getmMonthlyExpensesList().get(position).getCode();
                        ep.expName=newExpName;
                        ep.expAmt=newExpAmt;
                        ep.expDate=expDate[0];
                        ep.currentActivity=currentActivity;
                        ep.context=getApplicationContext();
                        ep.accountCode=mMonthlyExpenseAdapter.getmMonthlyExpensesList().get(position).getAccountCode();

                        new ExpenseDBTaskHandler().execute(ep);

                    }
                })
                .setNegativeButton(R.string.account_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                    }

                }).create();
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        alertDialog.show();
    }

    public void editExpenseHandler(ParamBaseClass param){
        if(param.successFlag==true){
            EditExpenseParam ep = (EditExpenseParam)param;
            Utility.showAlert(this,"Confirmation!","Expense edited successfully.");
            loadExpenses(ep.accountCode);
        }
        else
        {
            Utility.showAlert(this,"Error","Problem edit expense. Try again.");
        }
    }

}

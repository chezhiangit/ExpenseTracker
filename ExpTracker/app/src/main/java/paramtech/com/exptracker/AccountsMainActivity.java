package paramtech.com.exptracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class AccountsMainActivity extends AppCompatActivity {

    ArrayList<Accounts> accountList=new ArrayList<Accounts>();
    TextView handsOnText;
    TextView handsOnLabel;
    TextView addHandsOnLabel;
    EditText addHandsOnAmt;
    ImageButton addHandOnAmtBtn;
    ImageButton doneHandOnAmtBtn;
    AccountsAdapter mAccountsAdapter;
    SharedPreferenceManager sharedPreferenceManager=null;
    int monthIndex=0;
    TextView headerTextView;
    TextView totalExpenseAmt;
    TextView balanceAmt;

    BackupManager backupManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handsOnText = (TextView)findViewById(R.id.handsOnAmt);
        handsOnLabel = (TextView)findViewById(R.id.HandsOnLabel);
        addHandsOnLabel=(TextView)findViewById(R.id.addHandsOnLabel);
        addHandsOnAmt = (EditText)findViewById(R.id.addHhandsOnAmt);
        totalExpenseAmt=(TextView)findViewById(R.id.TotalExpAmt);
        balanceAmt=(TextView)findViewById(R.id.BalAmt);

        RecyclerView rcView = (RecyclerView)findViewById(R.id.rvAccounts);

      mAccountsAdapter = new AccountsAdapter();
        rcView.setAdapter(mAccountsAdapter);



        mAccountsAdapter.SetOnItemClickListener(new AccountsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent monthActivity = new Intent(getApplicationContext(), MonthView.class);
                monthActivity.putExtra("accountCode",mAccountsAdapter.getAccountList().get(position).getCode());
                monthActivity.putExtra("accountName",mAccountsAdapter.getAccountList().get(position).getName());
                startActivity(monthActivity);
            }

            @Override
            public void onEditButtonClick(final int position) {
                editAccountName(position);
            }

            @Override
            public void onDeleteButtonClick(final int position) {
                deleteAccount(position);
            }
        });

        rcView.setLayoutManager(new LinearLayoutManager(this));



        FloatingActionButton addNewAccount = (FloatingActionButton) findViewById(R.id.addNewAccount);
        addNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAccountName();
            }
        });

        addHandOnAmtBtn = (ImageButton)findViewById(R.id.addHandsOnBtn);
        doneHandOnAmtBtn = (ImageButton)findViewById(R.id.doneHandsOnBtn);
        addHandOnAmtBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if((handsOnLabel.getVisibility()==View.VISIBLE)&&(handsOnText.getVisibility()==View.VISIBLE)){

                    handsOnLabel.setVisibility(View.INVISIBLE);
                    handsOnText.setVisibility(View.INVISIBLE);
                    addHandOnAmtBtn.setVisibility(View.INVISIBLE);

                    addHandsOnAmt.setText(handsOnText.getText().toString());
                    addHandsOnLabel.setVisibility(View.VISIBLE);
                    addHandsOnAmt.setVisibility(View.VISIBLE);
                    doneHandOnAmtBtn.setVisibility(View.VISIBLE);
                    addHandsOnAmt.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(addHandsOnAmt, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        doneHandOnAmtBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if((addHandsOnLabel.getVisibility()==View.VISIBLE)&&(addHandsOnAmt.getVisibility()==View.VISIBLE)){

                    handsOnText.setText(addHandsOnAmt.getText().toString());
                    handsOnLabel.setVisibility(View.VISIBLE);
                    handsOnText.setVisibility(View.VISIBLE);
                    addHandOnAmtBtn.setVisibility(View.VISIBLE);

                    addHandsOnLabel.setVisibility(View.INVISIBLE);
                    addHandsOnAmt.setVisibility(View.INVISIBLE);
                   // addHandsOnAmt.setFocusable(false);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    doneHandOnAmtBtn.setVisibility(View.INVISIBLE);

                    int amt=Integer.parseInt(handsOnText.getText().toString());
                    updateHandsOnAmount(amt);
                }
            }
        });

        setMonth();
        sharedPreferenceManager = new SharedPreferenceManager(this);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
        String date=sdf.format(new Date()).toString().replaceAll("/", "");
        loadAccounts(date);
        getHandsOnAmount();
        backupManager = new BackupManager(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTotalExpense();

    }

    private void getAccountName(){
        LayoutInflater factory = LayoutInflater.from(this);
        final Activity currentActivity = this;
        final View textEntryView = factory.inflate(R.layout.new_account_dialog, null);
        AlertDialog alertDialog=new AlertDialog.Builder(AccountsMainActivity.this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.alert_dialog_text_entry)
                .setView(textEntryView)
                .setPositiveButton(R.string.account_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Collections.sort(accountList);

                        EditText newAccNameField = (EditText) textEntryView.findViewById(R.id.newAccountName);
                        String newAccName = (String) newAccNameField.getText().toString();
                        newAccName=String.valueOf(newAccName.charAt(0)).toUpperCase()+newAccName.subSequence(1, newAccName.length());

                        if ((newAccName.trim()).isEmpty()) return;

                        int accessorCode=sharedPreferenceManager.getAccessCode();

//                        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
//                        String date=sdf.format(new Date()).toString().replaceAll("/", "");

                        String date = getMonth();

                        String newAccountCode = date+accessorCode+Utility.getRandomNumber();//(mAccountsAdapter.getItemCount()+1);

                        ParamBaseClass newAccountParam = new NewAccountParam(newAccountCode,newAccName,accessorCode);
                        newAccountParam.func="addNewAccount";
                        newAccountParam.callBack="createAccountHandler";
                        newAccountParam.context=getApplicationContext();
                        newAccountParam.currentActivity=currentActivity;
                        new ExpenseDBTaskHandler().execute(newAccountParam);
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


    private void editAccountName(final int postion){
        LayoutInflater factory = LayoutInflater.from(this);
        final Activity currentActivity = this;
        final View textEntryView = factory.inflate(R.layout.new_account_dialog, null);
        EditText newAccNameField = (EditText)textEntryView.findViewById(R.id.newAccountName);
        newAccNameField.setText(mAccountsAdapter.getAccountList().get(postion).getName());
        AlertDialog alertDialog=new AlertDialog.Builder(AccountsMainActivity.this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.alert_dialog_text_entry)
                .setView(textEntryView)
                .setPositiveButton(R.string.account_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Collections.sort(accountList);

                        EditText newAccNameField = (EditText) textEntryView.findViewById(R.id.newAccountName);
                        String newAccName = (String) newAccNameField.getText().toString();
                        newAccName=String.valueOf(newAccName.charAt(0)).toUpperCase()+newAccName.subSequence(1,newAccName.length());

                        if ((newAccName.trim()).isEmpty()) return;

                        int accessorCode=sharedPreferenceManager.getAccessCode();

//                        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
//                        String date=sdf.format(new Date()).toString().replaceAll("/", "");

                        String newAccountCode = mAccountsAdapter.getAccountList().get(postion).getCode();

                        EditAccountParam editAccountParam = new EditAccountParam(newAccountCode,newAccName);
                        editAccountParam.func="editAccount";
                        editAccountParam.callBack="editAccountHandler";
                        editAccountParam.context=getApplicationContext();
                        editAccountParam.currentActivity=currentActivity;
                        editAccountParam.successFlag=false;
                        editAccountParam.adapterPosition=postion;

                        new ExpenseDBTaskHandler().execute(editAccountParam);
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

    public void editAccountHandler(ParamBaseClass param){
        EditAccountParam ep=(EditAccountParam)param;
        if(param.successFlag==true){
            mAccountsAdapter.getAccountList().get(ep.adapterPosition).setName(ep.accountName);
            mAccountsAdapter.notifyDataSetChanged();
            Utility.showAlert(this, "Information", "Account edited Successfully.");
            backupManager.dataChanged();
        }
        else
        {
            Utility.showAlert(this,"Information","Could not edit account. Pls Try again.");
        }

    }
    public void createAccountHandler(ParamBaseClass p){

        if(p.successFlag==true) {
            Utility.showAlert(this, "Information", "Account Created Successfully.");
            String date = headerTextView.getText().toString().replaceAll(" ","");
            loadAccounts(date);
            backupManager.dataChanged();
        }
        else{
            Utility.showAlert(this,"Information","Could not create account. Pls Try again.");
        }

    }

    public void loadAccounts(String date){
        int accessCode = sharedPreferenceManager.getAccessCode();
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
//        String date=sdf.format(new Date()).toString().replaceAll("/", "");
        LoadAccountsParam loadAccountParam = new LoadAccountsParam(date+accessCode+"%");
        loadAccountParam.currentActivity=this;
        loadAccountParam.context=getApplicationContext();
        loadAccountParam.func="loadAllAccounts";
        loadAccountParam.callBack="loadAccountHandler";
        loadAccountParam.successFlag=false;
        loadAccountParam.setAccountsList(mAccountsAdapter.getAccountList());

        new ExpenseDBTaskHandler().execute(loadAccountParam);

    }
    public void loadAccountHandler(ParamBaseClass p){
        if(p.successFlag==true) {
            mAccountsAdapter.notifyDataSetChanged();
            backupManager.dataChanged();
        }
        else{
            Utility.showAlert(this,"Information","Problem fetching accounts");
        }
    }

//    public void editAccountName(int position){
//
//        Utility.showAlert(this,"Information","Account edited successfully");
//    }
    public void deleteAccount(int position){
        DeleteAccountParam deleteAccountParam = new DeleteAccountParam();
        deleteAccountParam.accountCode=mAccountsAdapter.getAccountList().get(position).getCode();
        deleteAccountParam.adapterPosition=position;
        deleteAccountParam.context=getApplicationContext();
        deleteAccountParam.currentActivity=this;
        deleteAccountParam.func="deleteAccount";
        deleteAccountParam.callBack="deleteAccountHandler";
        deleteAccountParam.successFlag=false;

        new ExpenseDBTaskHandler().execute(deleteAccountParam);

       // Utility.showAlert(this,"Information","Account deleted successfully");
    }

    public void deleteAccountHandler(ParamBaseClass param){
        DeleteAccountParam dp = (DeleteAccountParam)param;
        if(param.successFlag==true) {
            mAccountsAdapter.getAccountList().remove(dp.adapterPosition);
            mAccountsAdapter.notifyDataSetChanged();
            Utility.showAlert(this, "Information", "Account deleted successfully");
            getTotalExpense();
            backupManager.dataChanged();
        }
        else {
            Utility.showAlert(this,"Information","Failed to delete account. Pls try again.");
        }
    }

    public void showDatePicker(View v){
        DatePicker dp = new DatePicker();

        dp.show(getFragmentManager(), "datepicker");
    }
public void setMonth() {
    Date today = new Date();
    DateFormat df = new SimpleDateFormat("MMM yyyy");
    String date = df.format(today).toString();
    headerTextView = (TextView) this.findViewById(R.id.headerDate);
    headerTextView.setText(date);
    headerTextView.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
            //String date=sdf.format(new Date()).toString().replaceAll("/", "");
            String date = s.toString().replaceAll(" ", "");
            loadAccounts(date);
        }
    });
    }

    public String getMonth(){
        String Date = headerTextView.getText().toString().replaceAll(" ","");
        return Date;
    }

    public void getTotalExpense(){
        String monthKey=getMonth()+String.valueOf(sharedPreferenceManager.getAccessCode());
        TotalExpensesParam tp = new TotalExpensesParam(monthKey);
        tp.successFlag=false;
        tp.currentActivity=this;
        tp.context=getApplicationContext();
        tp.func="getTotalExpenseAmount";
        tp.callBack="getTotalExpenseHandler";

        new ExpenseDBTaskHandler().execute(tp);

    }

    public void getTotalExpenseHandler(ParamBaseClass param){
        if(param.successFlag==true) {
            TotalExpensesParam tp = (TotalExpensesParam) param;
            totalExpenseAmt.setText(String.valueOf(tp.totalExpense));
            String handsOnAmt = handsOnText.getText().toString();
            int balance = Integer.parseInt(handsOnAmt) - tp.totalExpense;
            balanceAmt.setText(String.valueOf(balance));
        }
    }

    public void getHandsOnAmount(){
        HandsOnAmountParam hp=new HandsOnAmountParam();
        hp.successFlag=false;
        hp.context=getApplicationContext();
        hp.currentActivity=this;
        hp.month=getMonth()+String.valueOf(sharedPreferenceManager.getAccessCode());
        hp.amount=0;
        hp.func="getHandonAmount";
        hp.callBack="getHandsOnAmountHandler";

        new ExpenseDBTaskHandler().execute(hp);
    }

    public void getHandsOnAmountHandler(ParamBaseClass param){
        if(param.successFlag==true){
            HandsOnAmountParam hp=(HandsOnAmountParam)param;
            handsOnText.setText(String.valueOf(hp.amount));
        }
    }

    public void updateHandsOnAmount(int amount){
        HandsOnAmountParam hp=new HandsOnAmountParam();
        hp.successFlag=false;
        hp.context=getApplicationContext();
        hp.currentActivity=this;
        String handsOnAmt = handsOnText.getText().toString();
        int amt = Integer.parseInt(handsOnAmt);
        hp.month=getMonth()+String.valueOf(sharedPreferenceManager.getAccessCode());
        hp.amount=amt;
        hp.func="updateHandsonAmount";
        hp.callBack="updateHandsOnAmountHandler";

        new ExpenseDBTaskHandler().execute(hp);
    }

    public void updateHandsOnAmountHandler(ParamBaseClass param){
        if(param.successFlag==true){
            getTotalExpense();
            backupManager.dataChanged();
        }

    }

//    public void insertHandsonAmount(){
//        HandsOnAmountParam hp=new HandsOnAmountParam();
//        hp.successFlag=false;
//        hp.context=getApplicationContext();
//        hp.currentActivity=this;
//        String handsOnAmt = handsOnText.getText().toString();
//        int amt = Integer.parseInt(handsOnAmt);
//        hp.month=getMonth()+String.valueOf(sharedPreferenceManager.getAccessCode());
//        hp.amount=amt;
//        hp.func="updateHandsonAmount";
//        hp.callBack="updateHandsOnAmountHandler";
//
//        new ExpenseDBTaskHandler().execute(hp);
//    }


}

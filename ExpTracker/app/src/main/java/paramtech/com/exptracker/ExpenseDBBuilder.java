package paramtech.com.exptracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 324590 on 11/25/2015.
 */
public class ExpenseDBBuilder extends SQLiteOpenHelper {

    private static ExpenseDBBuilder expenseDBBuilder;

    public static final String EXPENSE_DATABASE_NAME = "ExpenseTracker.db";
    //Accounts Table
    public static final String ACCOUNTS_TABLE_NAME = "AccountsTable";
    public static final String ACCOUNTS_TABLE_ID="id";
    public static final String ACCOUNTS_TABLE_ACC_CODE="AccountCode";
    public static final String ACCOUNTS_TABLE_ACC_NAME="AccountName";
    public static final String ACCOUNTS_TABLE_ACCESS_CODE="AccessCode";

    //Expense Table
    public static final String EXPENSE_TABLE_NAME="ExpensesTable";
    public static final String EXPENSE_TABLE_ID="id";
    public static final String EXPENSE_TABLE_ACC_CODE="AccountCode";
    public static final String EXPENSE_TABLE_EXPENSE_CODE="ExpenseCode";
    public static final String EXPENSE_TABLE_EXPENSE_NAME="ExpenseName";
    public static final String EXPENSE_TABLE_EXPENSE_DATE="ExpenseDate";
    public static final String EXPENSE_TABLE_EXPENSE_AMOUNT="ExpenseAmt";

    //Access code table
    public static final String ACCESS_CODE_TABLE_NAME="AccessCodeTable";
    public static final String ACCESS_CODE_TABLE_ID="id";
    public static final String ACCESS_CODE = "AccessCode";
    public static final String ACCESSOR_NAME="AccessorName";

    //Handson amount table
    public static final String HANDSON_AMOUNT_TABLE_NAME="HandsOnTable";
    public static final String HANDSON_AMOUNT_TABLE_ID="id";
    public static final String HANDSON_AMOUNT_TABLE_MONTH="MonthKey";
    public static final String HANDSON_AMOUNT_AMOUNT="Amount";



    public ExpenseDBBuilder(Context context){
        super(context,EXPENSE_DATABASE_NAME,null,11);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try {
            db.execSQL("create table " + ACCESS_CODE_TABLE_NAME + " (" + ACCESS_CODE_TABLE_ID + " integer auto," + ACCESS_CODE + " integer primary key," + ACCESSOR_NAME + " text)");
            db.execSQL("create table " + ACCOUNTS_TABLE_NAME + " (" + ACCOUNTS_TABLE_ID + " integer auto," + ACCOUNTS_TABLE_ACC_CODE + " text," + ACCOUNTS_TABLE_ACC_NAME + " text," + ACCOUNTS_TABLE_ACCESS_CODE + " integer)");
            db.execSQL("create table "+EXPENSE_TABLE_NAME+" ("+EXPENSE_TABLE_ID+" integer auto,"+EXPENSE_TABLE_ACC_CODE+" text,"+EXPENSE_TABLE_EXPENSE_CODE+" text,"+EXPENSE_TABLE_EXPENSE_NAME+" text,"+EXPENSE_TABLE_EXPENSE_DATE+" text,"+EXPENSE_TABLE_EXPENSE_AMOUNT+" integer)");
            db.execSQL("create table " + HANDSON_AMOUNT_TABLE_NAME + " (" + HANDSON_AMOUNT_TABLE_ID + " integer auto," + HANDSON_AMOUNT_TABLE_MONTH + " text," + HANDSON_AMOUNT_AMOUNT + " integer default 0)");
        }
        catch (Exception E){
        int a=0;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        try {
            db.execSQL("DROP TABLE IF EXISTS " + ACCESS_CODE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + HANDSON_AMOUNT_TABLE_NAME);
            onCreate(db);
        }
        catch (Exception e){
        }
    }

    public static ExpenseDBBuilder getSQLBuilderInstance(Context context){
        if(expenseDBBuilder == null){
            expenseDBBuilder= new ExpenseDBBuilder(context);
        }
        return expenseDBBuilder;
    }


    /**
     * public methods to work with AccessCodeTable
     */
    public Boolean validateUser(ParamBaseClass param){
        LoginParam lp=(LoginParam)param;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ACCESS_CODE_TABLE_NAME+" where "+ACCESS_CODE+"="+lp.accessCode+"", null );
        if((res!=null)&&(res.getCount()==1)){
            res.close();
            return true;
        }
        res.close();
        db.close();
        return false;
    }

    public Boolean insertUser(ParamBaseClass param){
        InsertUserParam ip = (InsertUserParam)param;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCESS_CODE, ip.accessCode);
        values.put(ACCESSOR_NAME, ip.userName);

        // Inserting Row
        long res = db.insert(ACCESS_CODE_TABLE_NAME, null, values);
        db.close(); // Closing database connection
        if((res>0)){

            return true;
        }

        return false;
    }

    public Boolean addNewAccount(ParamBaseClass param){
        NewAccountParam np = (NewAccountParam)param;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNTS_TABLE_ACC_CODE,np.accountCode);
        values.put(ACCOUNTS_TABLE_ACC_NAME,np.accountName);
        values.put(ACCOUNTS_TABLE_ACCESS_CODE, np.accessorCode);

        long res = db.insert(ACCOUNTS_TABLE_NAME,null,values);

        if((res>0)){
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    public Boolean loadAllAccounts(ParamBaseClass param){
        try {
            LoadAccountsParam lp = (LoadAccountsParam) param;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + ACCOUNTS_TABLE_NAME + " where " + ACCOUNTS_TABLE_ACC_CODE + " LIKE '" + lp.getAccountCode()+"'", null);
            if (res != null)  {

                if(res.getCount()== -1) return true;

                res.moveToFirst();
                lp.getAccountsList().clear();

                while (res.isAfterLast() == false) {
                        int codeIndex = res.getColumnIndex(ACCOUNTS_TABLE_ACC_CODE);
                        int nameIndex = res.getColumnIndex(ACCOUNTS_TABLE_ACC_NAME);
                        String accountCode = res.getString(codeIndex);
                        String accountName = res.getString(nameIndex);
                        lp.getAccountsList().add(new Accounts(accountCode, accountName));
                        res.moveToNext();
                }
                res.close();
                db.close();
                return true;
            }
            res.close();
            db.close();
        }catch (Exception e){
           int a=0;
        }


        return false;
    }

    public Boolean deleteAccount(ParamBaseClass param){
        try {
            DeleteAccountParam dp = (DeleteAccountParam) param;
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("delete from " + ACCOUNTS_TABLE_NAME + " where " + ACCOUNTS_TABLE_ACC_CODE + "='" + dp.accountCode + "'");
            db.close();
            deleteExpenses(dp.accountCode);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private Boolean deleteExpenses(String accountCode){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("delete from " + EXPENSE_TABLE_NAME + " where " + EXPENSE_TABLE_EXPENSE_CODE + " LIKE \'" + accountCode + "%\'");
            db.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean editAccount(ParamBaseClass param){

        try {
            EditAccountParam ep=(EditAccountParam)param;
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("update " + ACCOUNTS_TABLE_NAME + " set "+ACCOUNTS_TABLE_ACC_NAME+"='"+ep.accountName+"' where "+ACCOUNTS_TABLE_ACC_CODE+"='"+ep.accountCode+"'");
            db.close();
            return true;
        }catch (Exception e){
            int a=0;
            return false;
        }
    }

    public Boolean insertExpense(ParamBaseClass param){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            InsertExpenseParam iep = (InsertExpenseParam)param;
            values.put(EXPENSE_TABLE_ACC_CODE,iep.accountCode);
            values.put(EXPENSE_TABLE_EXPENSE_CODE,iep.expenseCode);
            values.put(EXPENSE_TABLE_EXPENSE_NAME, iep.expenseName);
            values.put(EXPENSE_TABLE_EXPENSE_DATE, iep.expenseDate);
            values.put(EXPENSE_TABLE_EXPENSE_AMOUNT, iep.expenseAmount);

            long res = db.insert(EXPENSE_TABLE_NAME, null, values);

            if((res>0)){
                db.close();
                return true;
            }
            db.close();
            return false;

        }
        catch (Exception e){
            int a=0;
        }

        return false;
    }

    public Boolean loadExpenses(ParamBaseClass param){
        try {
            LoadExpensesParam lp = (LoadExpensesParam) param;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + EXPENSE_TABLE_NAME + " where " + EXPENSE_TABLE_ACC_CODE + "='" + lp.getAccountCode() + "'", null);
            if (res != null)  {

                if(res.getCount()== -1) return true;

                res.moveToFirst();
                lp.getExpensesList().clear();

                while (res.isAfterLast() == false) {
                    int accountCodeIndex = res.getColumnIndex(EXPENSE_TABLE_ACC_CODE);
                    int expCodeIndex = res.getColumnIndex(EXPENSE_TABLE_EXPENSE_CODE);
                    int expNameIndex = res.getColumnIndex(EXPENSE_TABLE_EXPENSE_NAME);
                    int expDateIndex= res.getColumnIndex(EXPENSE_TABLE_EXPENSE_DATE);
                    int expAmountIndex = res.getColumnIndex(EXPENSE_TABLE_EXPENSE_AMOUNT);
                    String accountCode = res.getString(accountCodeIndex);
                    String expName = res.getString(expNameIndex);
                    String expCode = res.getString(expCodeIndex);
                    String expDate = res.getString(expDateIndex);
                    int expAmt = res.getInt(expAmountIndex);

                    lp.getExpensesList().add(new MonthlyExpense(expCode,expName, expAmt,accountCode,expDate));
                    res.moveToNext();
                }
                res.close();
                db.close();
                return true;
            }
            res.close();
            db.close();
        }catch (Exception e){
            int a=0;
        }


        return false;
    }

    public Boolean editExpense(ParamBaseClass param){

        try {
            EditExpenseParam ep=(EditExpenseParam)param;
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("update " + EXPENSE_TABLE_NAME + " set " + EXPENSE_TABLE_EXPENSE_NAME + "='" + ep.expName + "'," + EXPENSE_TABLE_EXPENSE_AMOUNT + "='" + ep.expAmt + "'," + EXPENSE_TABLE_EXPENSE_DATE + "='" + ep.expDate + "' where " + EXPENSE_TABLE_EXPENSE_CODE + "='" + ep.expCode + "'");
            db.close();
            return true;
        }catch (Exception e){
            int a=0;
            return false;
        }
    }

    public Boolean deleteExpense(ParamBaseClass param){
        try {
            DeleteExpenseParam dp = (DeleteExpenseParam) param;
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("delete from " + EXPENSE_TABLE_NAME + " where " + EXPENSE_TABLE_EXPENSE_CODE + "=\'" + dp.expCode + "\'");
            db.close();
            // db.execSQL("commit");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean getTotalExpenseAmount(ParamBaseClass param){
        try {
            TotalExpensesParam te=(TotalExpensesParam)param;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + EXPENSE_TABLE_NAME + " where " + EXPENSE_TABLE_EXPENSE_CODE + " LIKE \'" + te.monthKey + "%\'", null);
           // Cursor res = db.rawQuery("select * from " + EXPENSE_TABLE_NAME, null);
            if (res != null)  {

                if(res.getCount()== -1) return true;

                res.moveToFirst();

                while (res.isAfterLast() == false) {
                    int expAmountIndex = res.getColumnIndex(EXPENSE_TABLE_EXPENSE_AMOUNT);
                    int expAmt = res.getInt(expAmountIndex);
                    te.totalExpense=te.totalExpense+expAmt;

                    res.moveToNext();
                }
                res.close();
                db.close();
                return true;
            }
            res.close();
            db.close();
        }catch (Exception e){
            int a=0;
        }
        return false;
    }

    public Boolean updateHandsonAmount(ParamBaseClass param){
        try {
            HandsOnAmountParam hp=(HandsOnAmountParam)param;

            SQLiteDatabase sdb = this.getReadableDatabase();
            Cursor res = sdb.rawQuery("select * from " + HANDSON_AMOUNT_TABLE_NAME + " where " + HANDSON_AMOUNT_TABLE_MONTH + " = '" + hp.month + "'", null);
            if(res.getCount()==0) {
                if(insertHandsonAmount(param)==true) {
                    sdb.close();
                    return true;
                }

            }
            else if(res.getCount()==1){
                sdb.close();
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(HANDSON_AMOUNT_AMOUNT, hp.amount);
                String condition = HANDSON_AMOUNT_TABLE_MONTH + "='" + hp.month + "'";
                int c = db.update(HANDSON_AMOUNT_TABLE_NAME, cv, condition, null);
                //db.execSQL("update " + HANDSON_AMOUNT_TABLE_NAME + " set " + HANDSON_AMOUNT_AMOUNT + "='" + hp.amount + "' where " + HANDSON_AMOUNT_TABLE_MONTH + "='" + hp.month + "'");
                db.close();
                if (c <= 0) {
                    return false;
                }
                return true;
            }
                }catch (Exception e){
                    int a=0;

                }
        return false;
    }

    public Boolean getHandonAmount(ParamBaseClass param){
        try {
            HandsOnAmountParam hp=(HandsOnAmountParam)param;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + HANDSON_AMOUNT_TABLE_NAME + " where " + HANDSON_AMOUNT_TABLE_MONTH + " = '" + hp.month + "'", null);
            if (res != null)  {

                if(res.getCount()== -1) return false;

                res.moveToFirst();

                while (res.isAfterLast() == false) {
                    int AmountIndex = res.getColumnIndex(HANDSON_AMOUNT_AMOUNT);
                    int amt = res.getInt(AmountIndex);
                    hp.amount=hp.amount+amt;

                    res.moveToNext();
                }
                res.close();
                db.close();
                return true;
            }
            res.close();
            db.close();
        }catch (Exception e){
            int a=0;
        }
        return false;
    }

    public Boolean insertHandsonAmount(ParamBaseClass param){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            HandsOnAmountParam hp= (HandsOnAmountParam)param;
            values.put(HANDSON_AMOUNT_TABLE_MONTH,hp.month);
            values.put(HANDSON_AMOUNT_AMOUNT,hp.amount);

            long res = db.insert(HANDSON_AMOUNT_TABLE_NAME, null, values);

            if((res>0)){
                db.close();
                return true;
            }
            db.close();
            return false;

        }
        catch (Exception e){
            int a=0;
        }

        return false;
    }



}

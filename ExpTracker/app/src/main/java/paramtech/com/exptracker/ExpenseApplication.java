package paramtech.com.exptracker;

import android.app.Application;
import android.support.annotation.RequiresPermission;
import android.util.Log;

/**
 * Created by 324590 on 12/21/2015.
 */
public class ExpenseApplication extends Application {


    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    private Thread.UncaughtExceptionHandler handler=new Thread.UncaughtExceptionHandler(){
        public void uncaughtException(Thread thread,Throwable ex){
            Log.e("Expense Application", "Uncaught Exception is: ", ex);
            uncaughtExceptionHandler.uncaughtException(thread,ex);
        }
    };

    @Override
     public void onCreate() {
     super.onCreate();
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

}
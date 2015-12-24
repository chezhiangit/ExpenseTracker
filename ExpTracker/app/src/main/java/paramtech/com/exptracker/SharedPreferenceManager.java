package paramtech.com.exptracker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 324590 on 12/4/2015.
 */
public class SharedPreferenceManager {

    public SharedPreferences sharedPreferences;
    public SharedPreferenceManager(Context context){
           sharedPreferences=context.getSharedPreferences("expenseData",0 );
    }

    public int getAccessCode(){
        int accessCode=sharedPreferences.getInt("AccessCode",0);
        return accessCode;
    }

    public void setAccessCode(int accessCode){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("AccessCode",accessCode);
        editor.commit();
    }
}

package paramtech.com.exptracker;

import android.app.Activity;
import android.content.Context;

/**
 * Created by 324590 on 11/26/2015.
 */
public class InsertUserParam extends ParamBaseClass {
    int accessCode;
    String userName;


    public InsertUserParam(int code,String userName,Context context,Activity currentActivity){
        this.accessCode=code;
        this.userName=userName;
        this.context=context;
        this.currentActivity=currentActivity;
    }
}

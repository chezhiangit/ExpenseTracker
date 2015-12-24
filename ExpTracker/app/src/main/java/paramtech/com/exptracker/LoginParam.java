package paramtech.com.exptracker;

import android.app.Activity;
import android.content.Context;

/**
 * Created by 324590 on 11/26/2015.
 */
public class LoginParam extends ParamBaseClass{

    int accessCode;

    public LoginParam(int code,Context context,Activity currentActivity){
        this.accessCode=code;
        this.context=context;
        this.currentActivity=currentActivity;
        this.successFlag=false;
    }
}


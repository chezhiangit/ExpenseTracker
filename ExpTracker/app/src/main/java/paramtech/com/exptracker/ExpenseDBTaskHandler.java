package paramtech.com.exptracker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.Method;


/**
 * Created by 324590 on 11/26/2015.
 */
public class ExpenseDBTaskHandler extends AsyncTask<ParamBaseClass, String, ParamBaseClass> {

    @Override
    protected void onPreExecute(){
        super.onPreExecute();

        //To show progress dialog
    }

    @Override
    protected ParamBaseClass doInBackground(ParamBaseClass... param){
        ExpenseDBBuilder db = ExpenseDBBuilder.getSQLBuilderInstance(param[0].context);
        try {
            Method method = ExpenseDBBuilder.class.getMethod(param[0].func,ParamBaseClass.class);
            param[0].successFlag=(Boolean)method.invoke(db,param[0]);

        }
        catch (Exception nm){
        int a=0;
        }
        return param[0];
    }

    @Override
    protected void onPostExecute(ParamBaseClass result){
        try {

            Method method = result.currentActivity.getClass().getMethod(result.callBack,ParamBaseClass.class);
            method.invoke(result.currentActivity,result);
        }
        catch (Exception nm){

        }

    }

}


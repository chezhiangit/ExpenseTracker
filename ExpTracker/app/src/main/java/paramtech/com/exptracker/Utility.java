package paramtech.com.exptracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Random;

/**
 * Created by 324590 on 11/26/2015.
 */
public class Utility {

    public static void showAlert(Context context,String title,String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setPositiveButton("Done", null);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        alertDialog.show();

    }
    public static int getRandomNumber(){
        int min = 10;
        int max = 999;
        int random = (new Random()).nextInt((max - min) + 1) + min;
        return random;
    }
}



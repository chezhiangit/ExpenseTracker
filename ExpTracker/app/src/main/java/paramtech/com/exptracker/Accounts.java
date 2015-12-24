package paramtech.com.exptracker;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 324590 on 10/30/2015.
 */
public class Accounts implements Comparable<Accounts> {
    private String code;
    private String name;
    private int accessorCode;


    public Accounts(String code,String name){
        this.code=code;
        this.name=name;

    }

    public static List<Accounts> CreateAccountsList(){
        List<Accounts> tempList = new ArrayList<Accounts>();

//        for(int i=0;i<10;i++){
//            Accounts t = new Accounts(i,"Test"+i);
//            tempList.add(t);
//            //t=null;
//        }

        return tempList;

    }
    @Override //To do
    public int compareTo(Accounts acc){
        return this.code.compareTo(acc.code);
    }
    public String getCode(){
        return code;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
}

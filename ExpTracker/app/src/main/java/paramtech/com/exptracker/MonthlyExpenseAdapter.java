package paramtech.com.exptracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by 324590 on 11/23/2015.
 */
public class MonthlyExpenseAdapter extends RecyclerView.Adapter<MonthlyExpenseAdapter.MonthlyExpenseViewHolder> {



    public static class MonthlyExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView expName;
        public TextView expAmt;
        public TextView expDate;
        public ImageButton editButton;
        public ImageButton deleteButton;

        public MonthlyExpenseViewHolder(View itemView){
            super(itemView);
            expName = (TextView)itemView.findViewById(R.id.expName);
            expAmt = (TextView)itemView.findViewById(R.id.expAmount);
            expDate = (TextView)itemView.findViewById(R.id.expDate);
            editButton = (ImageButton)itemView.findViewById(R.id.expEditButton);
            deleteButton = (ImageButton)itemView.findViewById(R.id.expDeleteButton);
        }

        @Override
        public void onClick(View v){
            //To Do
        }
    }
    private static OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(final OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }
    public interface OnItemClickListener{
        void onDeleteButtonClicked(int position);
        void onEditButtonClicked(int position);
    }
    private List<MonthlyExpense> mMonthlyExpensesList;

    public MonthlyExpenseAdapter(List<MonthlyExpense> pMonthlyExpense){
        mMonthlyExpensesList=pMonthlyExpense;
    }
    public List<MonthlyExpense> getmMonthlyExpensesList(){
        return mMonthlyExpensesList;
    }
    @Override
    public MonthlyExpenseAdapter.MonthlyExpenseViewHolder onCreateViewHolder(ViewGroup parent,int viewType ){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View monthlyView = inflater.inflate(R.layout.monthview_exp, parent, false);

        MonthlyExpenseViewHolder viewHolder = new MonthlyExpenseViewHolder(monthlyView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(MonthlyExpenseAdapter.MonthlyExpenseViewHolder viewHolder, final int position){
        MonthlyExpense expense = mMonthlyExpensesList.get(position);

        TextView expenseName = viewHolder.expName;
        expenseName.setText(expense.getName());
        TextView expenseAmt = viewHolder.expAmt;
        expenseAmt.setText(expense.getAmt().toString());
        TextView expenseDate = viewHolder.expDate;
        expenseDate.setText(expense.getDate());
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOnItemClickListener.onEditButtonClicked(position);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                alertDialogBuilder.setTitle("Confirmation!");
                alertDialogBuilder.setMessage("Do you want to edit expense name?");

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.show();

            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOnItemClickListener.onDeleteButtonClicked(position);
                    }
                });
                alertDialogBuilder.setNegativeButton("No",null);
                alertDialogBuilder.setTitle("Confirmation!");
                alertDialogBuilder.setMessage("Do you want to delete expense?");

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                alertDialog.show();

            }
        });

    }
    @Override
    public int getItemCount(){
        return mMonthlyExpensesList.size();
    }

    public void addNewExpense(MonthlyExpense newExpense){
        mMonthlyExpensesList.add(newExpense);
    }
    public String getMaxCode(){
        return mMonthlyExpensesList.get(mMonthlyExpensesList.size()-1).getCode();
    }
}

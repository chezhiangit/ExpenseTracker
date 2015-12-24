package paramtech.com.exptracker;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 324590 on 11/12/2015.
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>  {


    private static OnItemClickListener mOnItemClickListener;


    public static class AccountsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView accountName;
        public ImageButton editButton;
        public ImageButton deleteButton;

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v, getAdapterPosition());
        }

        public AccountsViewHolder(View itemView){
            super(itemView);

            accountName = (TextView)itemView.findViewById(R.id.accountName);
            editButton=(ImageButton)itemView.findViewById(R.id.editButton);
            deleteButton=(ImageButton)itemView.findViewById(R.id.deleteButton);
            itemView.setOnClickListener(this);

        }
    }
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onEditButtonClick(int position);
        void onDeleteButtonClick(int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;

    }
    private List<Accounts> mAccountsList;

    public AccountsAdapter(){
        mAccountsList=new ArrayList<Accounts>();
    }
    public List<Accounts> getAccountList(){
        return mAccountsList;
    }
    @Override
    public AccountsAdapter.AccountsViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View accountView = inflater.inflate(R.layout.account_list_mainpage, parent, false);

        AccountsViewHolder viewHolder = new AccountsViewHolder(accountView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AccountsAdapter.AccountsViewHolder viewHolder, final int position){
        Accounts account = mAccountsList.get(position);

        TextView accountName = viewHolder.accountName;
        accountName.setText(account.getName());
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOnItemClickListener.onDeleteButtonClick(position);
                    }
                });
                alertDialogBuilder.setNegativeButton("No",null);
                alertDialogBuilder.setTitle("Confirmation!");
                alertDialogBuilder.setMessage("Do you want to delete account name?");

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                alertDialog.show();

            }
        });

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOnItemClickListener.onEditButtonClick(position);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                alertDialogBuilder.setTitle("Confirmation!");
                alertDialogBuilder.setMessage("Do you want to edit account name?");

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount(){
        return mAccountsList.size();
    }

//    public void addNewAccout(Accounts newAccount){
//        mAccountsList.add(newAccount);
//    }
//    public int getMaxCode(){
//        return mAccountsList.get(mAccountsList.size()-1).getCode();
//    }
}

package com.example.spendwise.Views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spendwise.R;

public  class ExpenseViewHolder extends  RecyclerView.ViewHolder{

    View mExpenseView;

    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        mExpenseView=itemView;
    }

    public void setExpenseType(String type){
        TextView mtype=mExpenseView.findViewById(R.id.type_Expense_ds);
        mtype.setText(type);
    }

    public void setExpenseAmount(int amount){
        TextView mAmount=mExpenseView.findViewById(R.id.amount_Expense_ds);
        String strAmount=String.valueOf(amount);
        mAmount.setText(strAmount);

    }

    public void setExpenseDate(String date){
        TextView mDate=mExpenseView.findViewById(R.id.date_Expense_ds);
        mDate.setText(date);
    }
}
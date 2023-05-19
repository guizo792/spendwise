package com.example.spendwise.Views;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spendwise.R;

public class IncomeViewHolder extends RecyclerView.ViewHolder{
    View mIncomeView;
    public IncomeViewHolder(@NonNull View itemView) {
        super(itemView);
        mIncomeView=itemView;
    }

    public void setIncomeType(String type){
        TextView mtype=mIncomeView.findViewById(R.id.type_Income_ds);
        Log.i("TYPE", type);
        mtype.setText(type);
    }

    public void setIncomeAmount(int amount){
        TextView mAmount=mIncomeView.findViewById(R.id.amount_Income_ds);
        String strAmount=String.valueOf(amount);
        Log.i("AMOUNT", strAmount);
        mAmount.setText(strAmount);
    }

    public void setIncomeDate(String date){
        TextView mDate=mIncomeView.findViewById(R.id.date_Income_ds);
        Log.i("DATE", date);
        mDate.setText(date);
    }

}



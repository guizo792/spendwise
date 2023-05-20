package com.example.spendwise.Views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spendwise.R;

public  class ViewHolderFragmentExpense extends RecyclerView.ViewHolder{

    View mView;

    public ViewHolderFragmentExpense(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setType(String type){
        TextView mType = mView.findViewById(R.id.type_txt_expense);
        mType.setText(type);
    }

    public void setNote(String note){
        TextView mNote = mView.findViewById(R.id.note_txt_expense);
        mNote.setText(note);
    }

    public void setDate(String date){
        TextView mDate = mView.findViewById(R.id.date_txt_expense);
        mDate.setText(date);
    }

    public void setAmount(float amount){
        TextView mAmount = mView.findViewById(R.id.amount_txt_expense);
        String smAmount = String.valueOf(amount);
        mAmount.setText(smAmount);
    }

}
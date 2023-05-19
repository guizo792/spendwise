package com.example.spendwise.services;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.spendwise.R;
import com.example.spendwise.model.Data;

import java.text.DateFormat;
import java.util.Date;

public class DataService {


    public void insertIncomeData(){

        Fragment myFragment =new Fragment(
                R.layout.fragment_dashboard);

        AlertDialog.Builder mydialog= new AlertDialog.Builder(myFragment.getActivity());
        LayoutInflater inflater=LayoutInflater.from(myFragment.getActivity());

        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);
        final AlertDialog dialog=mydialog.create();
        dialog.setCancelable(false);

        EditText edtamount=myview.findViewById(R.id.amount);
        EditText edtType=myview.findViewById(R.id.type_edt);
        EditText edtNote=myview.findViewById(R.id.note_edt);

        Button saveBtn=myview.findViewById(R.id.btnSave);
        Button cancelBtn=myview.findViewById(R.id.btnCancel);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=edtType.getText().toString().trim();
                String amount=edtamount.getText().toString().trim();
                String note=edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)){
                    edtType.setError("Please Enter A Type");
                    return;
                }
                if(TextUtils.isEmpty(amount)){
                    edtamount.setError("Please Enter Amount");
                    return;
                }
                if(TextUtils.isEmpty(note)){
                    edtNote.setError("Please Enter A Note");
                    return;
                }
                int amountInInt=Integer.parseInt(amount);

                //Create random ID inside database
                String id= incomeDb.push().getKey();

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(amountInInt, type, note, id, mDate);

                incomeDb.child(id).setValue(data);

                Toast.makeText(getActivity(), "Transaction Added Successfully!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                floatingButtonAnimation();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}

package com.example.spendwise.services;


import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.spendwise.Anim.ButtonAnimation;
import com.example.spendwise.R;
import com.example.spendwise.model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.Date;

public class DataService {






//    public static Fragment myFragment =new Fragment(
//            R.layout.fragment_dashboard);

    public static void insertIncomeData( Fragment myFragment,DatabaseReference incomeDb,boolean isOpen,FloatingActionButton fab_plus, FloatingActionButton fab_minus, TextView fab_plus_text,TextView fab_minus_text){

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

                Toast.makeText(myFragment.getActivity(), "Transaction Added Successfully!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                ButtonAnimation.floatingButtonAnimation( myFragment, isOpen,fab_plus,fab_minus,fab_plus_text,fab_minus_text);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonAnimation.floatingButtonAnimation(myFragment, isOpen,fab_plus,fab_minus,fab_plus_text,fab_minus_text);
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public static void insertExpenseData(Fragment myFragment , DatabaseReference expenseDb, boolean isOpen,FloatingActionButton fab_plus, FloatingActionButton fab_minus, TextView fab_plus_text,TextView fab_minus_text ){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(myFragment.getActivity());
        LayoutInflater inflater=LayoutInflater.from(myFragment.getActivity());

        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);

        final AlertDialog dialog=mydialog.create();
        dialog.setCancelable(false);
        EditText edtamount=myview.findViewById(R.id.amount);
        EditText edttype=myview.findViewById(R.id.type_edt);
        EditText edtnote=myview.findViewById(R.id.note_edt);

        Button saveBtn=myview.findViewById(R.id.btnSave);
        Button cancelBtn=myview.findViewById(R.id.btnCancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount=edtamount.getText().toString().trim();
                String type=edttype.getText().toString().trim();
                String note=edtnote.getText().toString().trim();

                if(TextUtils.isEmpty(type)){
                    edttype.setError("Please Enter A Type");
                    return;
                }
                if(TextUtils.isEmpty(amount)){
                    edtamount.setError("Please Enter Amount");
                    return;
                }
                if(TextUtils.isEmpty(note)){
                    edtnote.setError("Please Enter A Note");
                    return;
                }
                int amountInInt= Integer.parseInt(amount);

                //Create random ID inside database
                String id= expenseDb.push().getKey();

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(amountInInt, type, note, id, mDate);

                expenseDb.child(id).setValue(data);

                Toast.makeText(myFragment.getActivity(), "Transaction Added Successfully!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                ButtonAnimation.floatingButtonAnimation(myFragment, isOpen,fab_plus,fab_minus,fab_plus_text,fab_minus_text);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ButtonAnimation.floatingButtonAnimation(myFragment, isOpen,fab_plus,fab_minus,fab_plus_text,fab_minus_text);
            }
        });
        dialog.show();
    }


    public static void updateDataItem(Fragment fragment,String type, String note, float amount ,String post_key, DatabaseReference mExpenseDatabase){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(fragment.getActivity());
        LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
        View myview = inflater.inflate(R.layout.update_data_item, null);
        mydialog.setView(myview);

        EditText edtAmount = myview.findViewById(R.id.amount);
        EditText edtNote = myview.findViewById(R.id.note_edt);
        EditText edtType = myview.findViewById(R.id.type_edt);

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        Button btnUpdate = myview.findViewById(R.id.btnUpdUpdate);
        Button btnDelete = myview.findViewById(R.id.btnUpdDelete);

        final AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = edtType.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                String stamount;
                stamount = edtAmount.getText().toString().trim();
                float intamount = Float.parseFloat(stamount);

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(intamount, type, note, post_key, mDate);

                mExpenseDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpenseDatabase.child(post_key).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();
    }



}
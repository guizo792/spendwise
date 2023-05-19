package com.example.spendwise.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.spendwise.Anim.ButtonAnimation;
import com.example.spendwise.R;
import com.example.spendwise.model.Data;

import com.example.spendwise.services.DataService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class DashboardFragment extends Fragment {


    //Floating Buttons
    private FloatingActionButton fab_minus_plus;


    //pluss btn
    private FloatingActionButton fab_plus;
    private TextView fab_plus_text;

    // minus btn
    private FloatingActionButton fab_minus;
    private  TextView fab_minus_text;





    private  boolean  isOpen=false;


    // animation class objects
    private Animation fadeOpen, fadeClose;

    //Dashboard income and expense result

    private TextView totalIncomResult;
    private TextView totalExpenseResult;
    private TextView totalDash ;


    // Firebase
    private FirebaseAuth authenticationFirebase;
    private DatabaseReference incomeDb;
    private DatabaseReference expenseDb;

    //Recycler view
    private RecyclerView recyclerIncome;
    private RecyclerView recyclerExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dashboard, container, false);

        authenticationFirebase =FirebaseAuth.getInstance();

        FirebaseUser mUser= authenticationFirebase.getCurrentUser();
        String uid=mUser.getUid();

        incomeDb = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        expenseDb =FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        incomeDb.keepSynced(true);
        expenseDb.keepSynced(true);
        //Connect Floating Button to layout

        fab_minus_plus =myview.findViewById(R.id.fb_main_plus_btn);
        fab_plus =myview.findViewById(R.id.income_ft_btn);
        fab_minus =myview.findViewById(R.id.expense_ft_btn);

        // Connect floating text
        fab_plus_text =myview.findViewById(R.id.income_ft_text);
        fab_minus_text =myview.findViewById(R.id.expense_ft_text);

        //Total income and expense

        totalIncomResult = myview.findViewById(R.id.income_set_result);
        totalExpenseResult = myview.findViewById(R.id.expense_set_result);
        totalDash =myview.findViewById(R.id.total) ;

        //Recycler

        recyclerIncome = myview.findViewById(R.id.recycler_income);
        recyclerExpense = myview.findViewById(R.id.recycler_expense);

        //Animations

        fadeOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_minus_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                ButtonAnimation.floatingButtonAnimation(isOpen,fab_plus,fab_minus,fab_plus_text,fab_minus_text);
            }
        });

        //Calculate total income

        incomeDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int total = 0;

                for(DataSnapshot mysnap: snapshot.getChildren()){
                    Data data = mysnap.getValue(Data.class);

                    total += data.getAmount();

                    String stResult = String.valueOf(total);

                    totalIncomResult.setText(stResult+".00");


                    String newTotalDash =String.valueOf(total-Float.parseFloat((String) totalExpenseResult.getText())) ;

                    totalDash.setText(newTotalDash);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Calculate total expense

        expenseDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int total= 0;

                for(DataSnapshot mysnap: snapshot.getChildren()){

                    Data data = mysnap.getValue(Data.class);

                    total += data.getAmount();

                    String stResult = String.valueOf(total);

                    totalExpenseResult.setText(stResult+".00");

                    String newTotalDash =String.valueOf(Float.parseFloat((String) totalIncomResult.getText())-total) ;

                    totalDash.setText(newTotalDash);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Recycler

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        recyclerIncome.setHasFixedSize(true);
        recyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        recyclerExpense.setHasFixedSize(true);
        recyclerExpense.setLayoutManager(layoutManagerExpense);


        return myview;
    }
    //Floating button animation



    private void addData(){
        //Fab Button Income
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataService.insertIncomeData(incomeDb, isOpen, fab_plus,  fab_minus,  fab_plus_text, fab_minus_text);

            }
        });

        fab_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataService.insertExpenseData(expenseDb,isOpen, fab_plus,  fab_minus,  fab_plus_text, fab_minus_text);
            }
        });
    }




    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter=new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(
                Data.class,
                R.layout.dashboard_income,
                DashboardFragment.IncomeViewHolder.class,
                incomeDb
        ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data data, int i) {
                incomeViewHolder.setIncomeType(data.getType());
                incomeViewHolder.setIncomeAmount(data.getAmount());
                incomeViewHolder.setIncomeDate(data.getDate());

            }
        };
        recyclerIncome.setAdapter(incomeAdapter);

        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> expenseAdapter= new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(
                Data.class,
                R.layout.dashboard_expense,
                DashboardFragment.ExpenseViewHolder.class,
                expenseDb
        ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder expenseViewHolder, Data data, int i) {
                expenseViewHolder.setExpenseType(data.getType());
                expenseViewHolder.setExpenseAmount(data.getAmount());
                expenseViewHolder.setExpenseDate(data.getDate());
            }
        };
        recyclerExpense.setAdapter(expenseAdapter);
    }

    // For income Data

    public static class IncomeViewHolder extends RecyclerView.ViewHolder{
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

    // For expense Data

    public static class ExpenseViewHolder extends  RecyclerView.ViewHolder{

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
}
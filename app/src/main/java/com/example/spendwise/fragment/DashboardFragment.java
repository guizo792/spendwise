package com.example.spendwise.fragment;

import android.app.AlertDialog;
import android.graphics.drawable.Animatable;
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
import com.example.spendwise.Views.ExpenseViewHolder;
import com.example.spendwise.Views.IncomeViewHolder;
import com.example.spendwise.model.Data;
import com.example.spendwise.services.DataService;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;


public class DashboardFragment extends Fragment {


    //Floating Button

    private FloatingActionButton fab_main;
    private FloatingActionButton fab_income;
    private FloatingActionButton fab_expense;



    private TextView fab_income_text;
    private  TextView fab_expense_text;


    private  boolean  isOpen=false;


    // animation class objects
    private Animation fadeOpen, fadeClose;

    //Dashboard income and expense result

    private TextView totalIncomResult;
    private TextView totalExpenseResult;
    private TextView totalDash ;


    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    //Recycler view
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        mIncomeDatabase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);
        //Connect Floating Button to layout

        fab_main=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income=myview.findViewById(R.id.income_ft_btn);
        fab_expense=myview.findViewById(R.id.expense_ft_btn);

        // Connect floating text
        fab_income_text=myview.findViewById(R.id.income_ft_text);
        fab_expense_text=myview.findViewById(R.id.expense_ft_text);

        //Total income and expense

        totalIncomResult = myview.findViewById(R.id.income_set_result);
        totalExpenseResult = myview.findViewById(R.id.expense_set_result);
        totalDash =myview.findViewById(R.id.total) ;

        //Recycler

        mRecyclerIncome = myview.findViewById(R.id.recycler_income);
        mRecyclerExpense = myview.findViewById(R.id.recycler_expense);

        //Animations

        fadeOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                ButtonAnimation.floatingButtonAnimation(DashboardFragment.this,isOpen,fab_income,fab_expense,fab_income_text,fab_expense_text);
            }
        });

        //Calculate total income

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
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

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
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
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);


        return myview;
    }
    //Floating button animation



    private void addData(){
        //Fab Button Income
        fab_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataService.insertIncomeData(DashboardFragment.this, mIncomeDatabase, isOpen, fab_income, fab_expense, fab_income_text, fab_expense_text);
            }
        });

        fab_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataService.insertExpenseData(DashboardFragment.this , mExpenseDatabase, isOpen, fab_income, fab_expense, fab_income_text, fab_expense_text);
            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Data> options1 =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter =
                new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull IncomeViewHolder holder, int position, @NonNull Data model) {
                        System.out.println("data: " + model);
                        holder.setIncomeType(model.getType());
                        holder.setIncomeAmount(model.getAmount());
                        holder.setIncomeDate(model.getDate());
                    }

                    @NonNull
                    @Override
                    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income, parent, false);
                        return new IncomeViewHolder(view);
                    }
                };

        mRecyclerIncome.setAdapter(incomeAdapter);

        FirebaseRecyclerOptions<Data> options2 =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase, Data.class)
                        .build();


        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> expenseAdapter =
                new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Data model) {
                        holder.setExpenseType(model.getType());
                        holder.setExpenseAmount(model.getAmount());
                        holder.setExpenseDate(model.getDate());
                    }

                    @NonNull
                    @Override
                    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_expense, parent, false);
                        return new ExpenseViewHolder(view);
                    }
                };

        mRecyclerExpense.setAdapter(expenseAdapter);

        incomeAdapter.startListening();
        expenseAdapter.startListening();

    }



}
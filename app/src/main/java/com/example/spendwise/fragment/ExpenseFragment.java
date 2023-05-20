package com.example.spendwise.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.spendwise.R;
import com.example.spendwise.Views.ViewHolderFragmentExpense;
import com.example.spendwise.model.Data;
import com.example.spendwise.services.DataService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //Firebase database

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;

    //Recyclerview

    private RecyclerView recyclerView;

    //TextView

    private TextView expenseSumResult;


    //Edit data item




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview =  inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        expenseSumResult = myview.findViewById(R.id.expense_txt_result);

        recyclerView = myview.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalvalue = 0;

                for(DataSnapshot mysnapshot: snapshot.getChildren()){
                    Data data = mysnapshot.getValue(Data.class);

                    totalvalue += data.getAmount();

                    String stotal = String.valueOf(totalvalue);
                    expenseSumResult.setText(stotal+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myview;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, ViewHolderFragmentExpense> adapter =
                new FirebaseRecyclerAdapter<Data, ViewHolderFragmentExpense>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderFragmentExpense holder, int position, @NonNull Data model) {
                        holder.setType(model.getType());
                        holder.setNote(model.getNote());
                        holder.setDate(model.getDate());
                        holder.setAmount(model.getAmount());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int adapterPosition = holder.getAdapterPosition();
                                if (adapterPosition != RecyclerView.NO_POSITION) {
                                    String post_key = getRef(adapterPosition).getKey();
                                    String type = model.getType();
                                    String note = model.getNote();
                                    float amount = model.getAmount();

                                    DataService.updateDataItem (ExpenseFragment.this
                                    ,type,note ,amount,post_key,mExpenseDatabase);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ViewHolderFragmentExpense onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data, parent, false);
                        return new ViewHolderFragmentExpense(view);
                    }
                };

        recyclerView.setAdapter(adapter);

        adapter.startListening();

    }





}

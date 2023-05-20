package com.example.spendwise.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

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
import com.example.spendwise.Views.ViewHolderFragmentIncome;
import com.example.spendwise.model.Data;
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

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public IncomeFragment() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
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



    // firebase sb
    private FirebaseAuth firebaseAuth;
    private DatabaseReference incomeDB;


    private TextView incomeTotalSum;
    private EditText editAmount;
    private EditText editType;
    private EditText editNote;


    private RecyclerView recyclerView;

    private String type;
    private String note;
    private float amount;
    private String post_key;

    private Button btnUpdate;
    private Button btnDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview =  inflater.inflate(R.layout.fragment_income, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        String uid = mUser.getUid();
        incomeDB = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalSum = myview.findViewById(R.id.income_txt_result);


        recyclerView = myview.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        incomeDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalvalue = 0;

                for (DataSnapshot mysnapshot: snapshot.getChildren()){
                    Data data = mysnapshot.getValue(Data.class);
                    totalvalue += data.getAmount();

                    String sTotalvalue = String.valueOf(totalvalue);

                    incomeTotalSum.setText(sTotalvalue+".00");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myview;


    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(incomeDB, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, ViewHolderFragmentIncome> adapter = new FirebaseRecyclerAdapter<Data, ViewHolderFragmentIncome>(options) {

            @NonNull
            @Override
            public ViewHolderFragmentIncome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false);
                return new ViewHolderFragmentIncome(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderFragmentIncome viewHolder, int position, @NonNull Data model) {

                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());
                viewHolder.setAmount(model.getAmount());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = viewHolder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            String post_key = getRef(adapterPosition).getKey();
                        }

                        type = model.getType();
                        note = model.getNote();
                        amount = model.getAmount();

                        //updateDataItem();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);

        adapter.startListening();


    }



    private void updateDataItem() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.update_data_item, null);
        mydialog.setView(myview);

        editAmount = myview.findViewById(R.id.amount);
        editType = myview.findViewById(R.id.type_edt);
        editNote = myview.findViewById(R.id.note_edt);

        // Set data to edit text

        editType.setText(type);
        editType.setSelection(type.length());

        editNote.setText(note);
        editNote.setSelection(note.length());

        editAmount.setText(String.valueOf(amount));
        editAmount.setSelection(String.valueOf(amount).length());

        btnUpdate = myview.findViewById(R.id.btnUpdUpdate);
        btnDelete = myview.findViewById(R.id.btnUpdDelete);

        final AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = editType.getText().toString().trim();
                note = editNote.getText().toString().trim();

                String mdamount = String.valueOf(amount);
                mdamount = editAmount.getText().toString().trim();

                int myAmount = Integer.parseInt(mdamount);

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(myAmount, type, note, post_key, mDate);

                incomeDB.child(post_key).setValue(data);
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                incomeDB.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();

    }



}
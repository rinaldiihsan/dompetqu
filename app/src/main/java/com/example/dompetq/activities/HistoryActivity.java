package com.example.dompetq.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.dompetq.R;
import com.example.dompetq.adapters.TransactionAdapter;
import com.example.dompetq.database.TransactionDao;
import com.example.dompetq.models.Transaction;
import com.example.dompetq.utils.Constants;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvHistory;
    private TransactionAdapter adapter;
    private TransactionDao transactionDao;
    private TextView tvTotalSaldo;
    private AutoCompleteTextView spinnerFilter;
    private LinearLayout emptyState;
    private NumberFormat currencyFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        transactionDao = new TransactionDao(this);
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

        initViews();
        setupFilter();
        setupRecyclerView();
        updateSaldo();
    }

    private void initViews() {
        rvHistory = findViewById(R.id.rvHistory);
        tvTotalSaldo = findViewById(R.id.tvTotalSaldo);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        emptyState = findViewById(R.id.emptyState);
    }

    private void setupFilter() {
        String[] filters = {"Semua", "Pemasukan", "Pengeluaran"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                filters
        );
        spinnerFilter.setAdapter(adapter);
        spinnerFilter.setText(filters[0], false);

        spinnerFilter.setOnItemClickListener((parent, view, position, id) -> {
            loadTransactions(position);
        });
    }

    private void setupRecyclerView() {
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(new ArrayList<>());
        rvHistory.setAdapter(adapter);
        loadTransactions(0); // Load semua transaksi
    }

    private void loadTransactions(int filterPosition) {
        transactionDao.open();
        List<Transaction> transactions;

        switch (filterPosition) {
            case 1: // Pemasukan
                transactions = transactionDao.getTransactionsByType(Constants.TYPE_INCOME);
                break;
            case 2: // Pengeluaran
                transactions = transactionDao.getTransactionsByType(Constants.TYPE_EXPENSE);
                break;
            default: // Semua
                transactions = transactionDao.getAllTransactions();
        }
        transactionDao.close();

        adapter.updateData(transactions);
        updateEmptyState(transactions.isEmpty());
    }

    private void updateSaldo() {
        transactionDao.open();
        double saldo = transactionDao.getBalance();
        transactionDao.close();

        String formattedSaldo = currencyFormat.format(saldo);
        tvTotalSaldo.setText(formattedSaldo);
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            rvHistory.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rvHistory.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (transactionDao != null) {
            transactionDao.close();
        }
    }
}
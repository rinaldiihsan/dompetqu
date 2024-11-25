package com.example.dompetq.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.dompetq.R;
import com.example.dompetq.database.TransactionDao;
import com.google.android.material.card.MaterialCardView; // Pastikan untuk mengimpor ini

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvSaldo;
    private MaterialCardView btnTambahPemasukan, btnTambahPengeluaran, btnHistory; // Ubah tipe menjadi MaterialCardView
    private TransactionDao transactionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi database
        transactionDao = new TransactionDao(this);

        // Inisialisasi views
        initViews();

        // Setup click listeners
        setupClickListeners();
    }

    private void initViews() {
        tvSaldo = findViewById(R.id.tvSaldo);
        btnTambahPemasukan = findViewById(R.id.btnTambahPemasukan);
        btnTambahPengeluaran = findViewById(R.id.btnTambahPengeluaran);
        btnHistory = findViewById(R.id.btnHistory);
    }

    private void setupClickListeners() {
        btnTambahPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddIncomeActivity.class);
                startActivity(intent);
            }
        });

        btnTambahPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSaldo();
    }

    private void updateSaldo() {
        transactionDao.open();
        double saldo = transactionDao.getBalance();
        transactionDao.close();

        // Format saldo ke format Rupiah
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        String formattedSaldo = formatRupiah.format(saldo);

        tvSaldo.setText(formattedSaldo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (transactionDao != null) {
            transactionDao.close();
        }
    }
}

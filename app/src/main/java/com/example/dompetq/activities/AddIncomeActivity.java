package com.example.dompetq.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.example.dompetq.R;
import com.example.dompetq.database.CategoryDao;
import com.example.dompetq.database.TransactionDao;
import com.example.dompetq.models.Category;
import com.example.dompetq.models.Transaction;
import com.example.dompetq.utils.Constants;
import com.example.dompetq.utils.DateFormatter;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddIncomeActivity extends AppCompatActivity {
    private TextInputEditText etJumlah, etCatatan;
    private AutoCompleteTextView spinnerKategori;
    private MaterialButton btnSimpan;
    private MaterialButton btnAddCategory;
    private TransactionDao transactionDao;
    private CategoryDao categoryDao;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        transactionDao = new TransactionDao(this);
        categoryDao = new CategoryDao(this);

        initViews();
        setupSpinner();
        setupClickListeners();
    }

    private void initViews() {
        etJumlah = findViewById(R.id.etJumlah);
        spinnerKategori = findViewById(R.id.spinnerKategori);
        etCatatan = findViewById(R.id.etCatatan);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnAddCategory = findViewById(R.id.btnAddCategory);
    }

    private void setupSpinner() {
        categoryDao.open();
        categories = categoryDao.getCategoriesByType(Constants.TYPE_INCOME);
        categoryDao.close();

        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,
                categoryNames
        );
        spinnerKategori.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSimpan.setOnClickListener(v -> {
            if (validateInput()) {
                saveTransaction();
            }
        });

        btnAddCategory.setOnClickListener(v -> showAddCategoryDialog());
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_category, null);

        TextInputEditText etNamaKategori = view.findViewById(R.id.etNamaKategori);
        MaterialButton btnBatal = view.findViewById(R.id.btnBatal);
        MaterialButton btnSimpan = view.findViewById(R.id.btnSimpan);

        AlertDialog dialog = builder.setView(view)
                .setCancelable(false)
                .create();

        btnBatal.setOnClickListener(v -> dialog.dismiss());

        btnSimpan.setOnClickListener(v -> {
            String categoryName = etNamaKategori.getText().toString().trim();
            if (!categoryName.isEmpty()) {
                saveNewCategory(categoryName);
                dialog.dismiss();
            } else {
                etNamaKategori.setError(Constants.ERROR_CATEGORY_NAME_EMPTY);
            }
        });

        dialog.show();
    }

    private void saveNewCategory(String categoryName) {
        Category newCategory = new Category();
        newCategory.setName(categoryName);
        newCategory.setType(Constants.TYPE_INCOME);

        categoryDao.open();
        long result = categoryDao.addCategory(newCategory);
        categoryDao.close();

        if (result != -1) {
            Toast.makeText(this, Constants.SUCCESS_CATEGORY_ADDED, Toast.LENGTH_SHORT).show();
            setupSpinner();
            // Select the newly added category
            spinnerKategori.setText(categoryName, false);
        } else {
            Toast.makeText(this, Constants.ERROR_CATEGORY_FAILED, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {
        boolean isValid = true;

        if (etJumlah.getText().toString().trim().isEmpty()) {
            etJumlah.setError(Constants.ERROR_AMOUNT_EMPTY);
            isValid = false;
        }

        String selectedCategory = spinnerKategori.getText().toString();
        if (selectedCategory.isEmpty()) {
            spinnerKategori.setError(Constants.ERROR_CATEGORY_EMPTY);
            isValid = false;
        }

        return isValid;
    }

    private void saveTransaction() {
        try {
            double amount = Double.parseDouble(etJumlah.getText().toString());
            String category = spinnerKategori.getText().toString();
            String note = etCatatan.getText().toString();

            Transaction transaction = new Transaction();
            transaction.setType(Constants.TYPE_INCOME);
            transaction.setAmount(amount);
            transaction.setCategory(category);
            transaction.setNote(note);
            transaction.setDate(DateFormatter.getCurrentDate());

            transactionDao.open();
            long result = transactionDao.addTransaction(transaction);
            transactionDao.close();

            if (result != -1) {
                Toast.makeText(this, Constants.SUCCESS_INCOME_SAVED, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, Constants.ERROR_TRANSACTION_FAILED, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            etJumlah.setError(Constants.ERROR_AMOUNT_INVALID);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (categoryDao != null) {
            categoryDao.close();
        }
        if (transactionDao != null) {
            transactionDao.close();
        }
    }
}
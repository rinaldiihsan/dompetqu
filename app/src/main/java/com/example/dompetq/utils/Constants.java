package com.example.dompetq.utils;

import com.example.dompetq.models.Category;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    // Transaction Types
    public static final String TYPE_INCOME = "INCOME";
    public static final String TYPE_EXPENSE = "EXPENSE";

    // Date Formats
    public static final String DATE_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DISPLAY = "dd MMM yyyy, HH:mm";

    // Default Categories
    public static List<Category> getDefaultIncomeCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Gaji", TYPE_INCOME));
        categories.add(new Category("Bonus", TYPE_INCOME));
        categories.add(new Category("Hadiah", TYPE_INCOME));
        categories.add(new Category("Investasi", TYPE_INCOME));
        categories.add(new Category("Lainnya", TYPE_INCOME));
        return categories;
    }

    public static List<Category> getDefaultExpenseCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Makanan & Minuman", TYPE_EXPENSE));
        categories.add(new Category("Transportasi", TYPE_EXPENSE));
        categories.add(new Category("Belanja", TYPE_EXPENSE));
        categories.add(new Category("Tagihan", TYPE_EXPENSE));
        categories.add(new Category("Hiburan", TYPE_EXPENSE));
        categories.add(new Category("Kesehatan", TYPE_EXPENSE));
        categories.add(new Category("Pendidikan", TYPE_EXPENSE));
        categories.add(new Category("Lainnya", TYPE_EXPENSE));
        return categories;
    }

    // Error Messages
    public static final String ERROR_AMOUNT_EMPTY = "Jumlah harus diisi";
    public static final String ERROR_CATEGORY_EMPTY = "Kategori harus dipilih";
    public static final String ERROR_AMOUNT_INVALID = "Format jumlah tidak valid";

    // Success Messages
    public static final String SUCCESS_INCOME_SAVED = "Pemasukan berhasil disimpan";
    public static final String SUCCESS_EXPENSE_SAVED = "Pengeluaran berhasil disimpan";
    public static final String ERROR_TRANSACTION_FAILED = "Gagal menyimpan transaksi";

    // Di class Constants
    public static final String SUCCESS_CATEGORY_ADDED = "Kategori berhasil ditambahkan";
    public static final String ERROR_CATEGORY_FAILED = "Gagal menambahkan kategori";
    public static final String ERROR_CATEGORY_NAME_EMPTY = "Nama kategori tidak boleh kosong";
}
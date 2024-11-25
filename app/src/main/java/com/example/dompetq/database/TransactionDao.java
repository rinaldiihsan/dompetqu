package com.example.dompetq.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import com.example.dompetq.models.Transaction;
import com.example.dompetq.utils.Constants;

public class TransactionDao {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public TransactionDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Menambah transaksi baru
    public long addTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TYPE, transaction.getType());
        values.put(DatabaseHelper.COLUMN_AMOUNT, transaction.getAmount());
        values.put(DatabaseHelper.COLUMN_CATEGORY, transaction.getCategory());
        values.put(DatabaseHelper.COLUMN_NOTE, transaction.getNote());
        values.put(DatabaseHelper.COLUMN_DATE, transaction.getDate());

        return database.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);
    }

    // Mengambil semua transaksi
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_TRANSACTIONS,
                new String[]{
                        DatabaseHelper.COLUMN_ID,
                        DatabaseHelper.COLUMN_TYPE,
                        DatabaseHelper.COLUMN_AMOUNT,
                        DatabaseHelper.COLUMN_CATEGORY,
                        DatabaseHelper.COLUMN_NOTE,
                        DatabaseHelper.COLUMN_DATE
                },
                null, null, null, null,
                DatabaseHelper.COLUMN_DATE + " DESC"
        );

        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = cursorToTransaction(cursor);
                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return transactions;
    }

    // Mengambil transaksi berdasarkan ID
    public Transaction getTransactionById(long id) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_TRANSACTIONS,
                new String[]{
                        DatabaseHelper.COLUMN_ID,
                        DatabaseHelper.COLUMN_TYPE,
                        DatabaseHelper.COLUMN_AMOUNT,
                        DatabaseHelper.COLUMN_CATEGORY,
                        DatabaseHelper.COLUMN_NOTE,
                        DatabaseHelper.COLUMN_DATE
                },
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        Transaction transaction = null;
        try {
            if (cursor.moveToFirst()) {
                transaction = cursorToTransaction(cursor);
            }
        } finally {
            cursor.close();
        }

        return transaction;
    }

    // Update transaksi
    public int updateTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TYPE, transaction.getType());
        values.put(DatabaseHelper.COLUMN_AMOUNT, transaction.getAmount());
        values.put(DatabaseHelper.COLUMN_CATEGORY, transaction.getCategory());
        values.put(DatabaseHelper.COLUMN_NOTE, transaction.getNote());
        values.put(DatabaseHelper.COLUMN_DATE, transaction.getDate());

        return database.update(
                DatabaseHelper.TABLE_TRANSACTIONS,
                values,
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(transaction.getId())}
        );
    }

    // Hapus transaksi
    public int deleteTransaction(long id) {
        return database.delete(
                DatabaseHelper.TABLE_TRANSACTIONS,
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    // Mendapatkan total saldo
    public double getBalance() {
        double income = getTotalByType(Constants.TYPE_INCOME);
        double expense = getTotalByType(Constants.TYPE_EXPENSE);
        return income - expense;
    }

    // Mendapatkan total pemasukan
    public double getTotalIncome() {
        return getTotalByType(Constants.TYPE_INCOME);
    }

    // Mendapatkan total pengeluaran
    public double getTotalExpense() {
        return getTotalByType(Constants.TYPE_EXPENSE);
    }

    // Helper method untuk mengambil total berdasarkan tipe
    private double getTotalByType(String type) {
        String query = "SELECT SUM(" + DatabaseHelper.COLUMN_AMOUNT + ") FROM " +
                DatabaseHelper.TABLE_TRANSACTIONS +
                " WHERE " + DatabaseHelper.COLUMN_TYPE + "=?";

        Cursor cursor = database.rawQuery(query, new String[]{type});
        double total = 0;
        try {
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
        } finally {
            cursor.close();
        }
        return total;
    }

    // Helper method untuk mengubah Cursor menjadi Transaction
    private Transaction cursorToTransaction(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
        transaction.setType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE)));
        transaction.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT)));
        transaction.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY)));
        transaction.setNote(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE)));
        transaction.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)));
        return transaction;
    }

    // Mengambil transaksi berdasarkan tipe
    public List<Transaction> getTransactionsByType(String type) {
        List<Transaction> transactions = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_TRANSACTIONS,
                null,
                DatabaseHelper.COLUMN_TYPE + "=?",
                new String[]{type},
                null, null,
                DatabaseHelper.COLUMN_DATE + " DESC"
        );

        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = cursorToTransaction(cursor);
                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return transactions;
    }
}
package com.example.dompetq.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.dompetq.models.Category;
import com.example.dompetq.utils.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DompetQ.db";
    private static final int DATABASE_VERSION = 1;

    // Nama tabel Transactions
    public static final String TABLE_TRANSACTIONS = "transactions";

    // Nama kolom Transactions
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type"; // 'INCOME' atau 'EXPENSE'
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";

    // Nama tabel Categories
    public static final String TABLE_CATEGORIES = "categories";

    // Nama kolom Categories
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_TYPE = "type";

    // Query pembuatan tabel Transactions
    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TYPE + " TEXT NOT NULL, "
            + COLUMN_AMOUNT + " REAL NOT NULL, "
            + COLUMN_CATEGORY + " TEXT NOT NULL, "
            + COLUMN_NOTE + " TEXT, "
            + COLUMN_DATE + " TEXT NOT NULL)";

    // Query pembuatan tabel Categories
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_NAME + " TEXT NOT NULL, "
            + COLUMN_CATEGORY_TYPE + " TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat kedua tabel
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_CATEGORIES);

        // Insert kategori default
        insertDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop semua tabel jika ada upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public int deleteTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(transactionId)});
        db.close();
        return transactionId;
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        // Insert kategori pemasukan default
        for (Category category : Constants.getDefaultIncomeCategories()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, category.getName());
            values.put(COLUMN_CATEGORY_TYPE, category.getType());
            db.insert(TABLE_CATEGORIES, null, values);
        }

        // Insert kategori pengeluaran default
        for (Category category : Constants.getDefaultExpenseCategories()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, category.getName());
            values.put(COLUMN_CATEGORY_TYPE, category.getType());
            db.insert(TABLE_CATEGORIES, null, values);
        }
    }
}
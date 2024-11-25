package com.example.dompetq.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import com.example.dompetq.models.Category;
import com.example.dompetq.utils.Constants;

public class CategoryDao {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public CategoryDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Menambah kategori baru
    public long addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.getName());
        values.put(DatabaseHelper.COLUMN_CATEGORY_TYPE, category.getType());

        return database.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
    }

    // Mengambil semua kategori berdasarkan tipe (INCOME/EXPENSE)
    public List<Category> getCategoriesByType(String type) {
        List<Category> categories = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CATEGORIES,
                new String[]{
                        DatabaseHelper.COLUMN_CATEGORY_ID,
                        DatabaseHelper.COLUMN_CATEGORY_NAME,
                        DatabaseHelper.COLUMN_CATEGORY_TYPE
                },
                DatabaseHelper.COLUMN_CATEGORY_TYPE + "=?",
                new String[]{type},
                null, null,
                DatabaseHelper.COLUMN_CATEGORY_NAME + " ASC"
        );

        try {
            if (cursor.moveToFirst()) {
                do {
                    Category category = cursorToCategory(cursor);
                    categories.add(category);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return categories;
    }

    // Mengambil kategori berdasarkan ID
    public Category getCategoryById(int id) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CATEGORIES,
                new String[]{
                        DatabaseHelper.COLUMN_CATEGORY_ID,
                        DatabaseHelper.COLUMN_CATEGORY_NAME,
                        DatabaseHelper.COLUMN_CATEGORY_TYPE
                },
                DatabaseHelper.COLUMN_CATEGORY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        Category category = null;
        try {
            if (cursor.moveToFirst()) {
                category = cursorToCategory(cursor);
            }
        } finally {
            cursor.close();
        }

        return category;
    }

    // Update kategori
    public int updateCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.getName());
        values.put(DatabaseHelper.COLUMN_CATEGORY_TYPE, category.getType());

        return database.update(
                DatabaseHelper.TABLE_CATEGORIES,
                values,
                DatabaseHelper.COLUMN_CATEGORY_ID + "=?",
                new String[]{String.valueOf(category.getId())}
        );
    }

    // Hapus kategori
    public int deleteCategory(int id) {
        return database.delete(
                DatabaseHelper.TABLE_CATEGORIES,
                DatabaseHelper.COLUMN_CATEGORY_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    // Helper method untuk mengubah Cursor menjadi Category
    private Category cursorToCategory(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)));
        category.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)));
        category.setType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_TYPE)));
        return category;
    }

    // Mengecek apakah ada kategori default, jika tidak ada akan diinsert
    public void checkAndInsertDefaultCategories() {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CATEGORIES,
                new String[]{DatabaseHelper.COLUMN_CATEGORY_ID},
                null, null, null, null, null
        );

        try {
            if (cursor.getCount() == 0) {
                // Insert kategori default untuk pemasukan
                for (Category category : Constants.getDefaultIncomeCategories()) {
                    addCategory(category);
                }

                // Insert kategori default untuk pengeluaran
                for (Category category : Constants.getDefaultExpenseCategories()) {
                    addCategory(category);
                }
            }
        } finally {
            cursor.close();
        }
    }
}
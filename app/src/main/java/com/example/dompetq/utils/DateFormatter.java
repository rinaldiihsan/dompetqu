package com.example.dompetq.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private static final SimpleDateFormat dbFormat =
            new SimpleDateFormat(Constants.DATE_FORMAT_DB, Locale.getDefault());

    private static final SimpleDateFormat displayFormat =
            new SimpleDateFormat(Constants.DATE_FORMAT_DISPLAY, new Locale("id"));

    // Mengubah Date ke format database
    public static String toDbFormat(Date date) {
        return dbFormat.format(date);
    }

    // Mengubah Date ke format display
    public static String toDisplayFormat(Date date) {
        return displayFormat.format(date);
    }

    // Mengubah string dari database ke Date
    public static Date fromDbFormat(String dateStr) throws ParseException {
        return dbFormat.parse(dateStr);
    }

    // Mengubah string dari database ke format display
    public static String dbToDisplay(String dbDateStr) {
        try {
            Date date = fromDbFormat(dbDateStr);
            return toDisplayFormat(date);
        } catch (ParseException e) {
            return dbDateStr; // Return original string if parsing fails
        }
    }

    // Mendapatkan current date dalam format database
    public static String getCurrentDate() {
        return toDbFormat(new Date());
    }
}
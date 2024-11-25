package com.example.dompetq.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dompetq.R;
import com.example.dompetq.database.DatabaseHelper;
import com.example.dompetq.models.Transaction;
import com.example.dompetq.utils.Constants;
import com.example.dompetq.utils.DateFormatter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private NumberFormat currencyFormat;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Transaction transaction = transactions.get(position);

        holder.tvCategory.setText(transaction.getCategory());
        holder.tvDate.setText(DateFormatter.dbToDisplay(transaction.getDate()));
        holder.tvNote.setText(transaction.getNote().isEmpty() ? "-" : transaction.getNote());

        // Format jumlah uang
        String amount = currencyFormat.format(transaction.getAmount());
        if (transaction.getType().equals(Constants.TYPE_EXPENSE)) {
            amount = "- " + amount;
            holder.tvAmount.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.holo_red_dark));
        } else {
            amount = "+ " + amount;
            holder.tvAmount.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.holo_green_dark));
        }
        holder.tvAmount.setText(amount);

        // Tambahkan onClick listener untuk menampilkan detail dan opsi
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactionDetail(holder.itemView.getContext(), transaction, position);
            }
        });
    }

    private void showTransactionDetail(final Context context, final Transaction transaction, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_transaction_detail, null);

        TextView tvDetailType = view.findViewById(R.id.tvDetailType);
        TextView tvDetailAmount = view.findViewById(R.id.tvDetailAmount);
        TextView tvDetailCategory = view.findViewById(R.id.tvDetailCategory);
        TextView tvDetailDate = view.findViewById(R.id.tvDetailDate);
        TextView tvDetailNote = view.findViewById(R.id.tvDetailNote);

        // Set nilai
        String type = transaction.getType().equals(Constants.TYPE_EXPENSE) ? "Pengeluaran" : "Pemasukan";
        tvDetailType.setText(type);
        tvDetailAmount.setText(currencyFormat.format(transaction.getAmount()));
        tvDetailCategory.setText(transaction.getCategory());
        tvDetailDate.setText(DateFormatter.dbToDisplay(transaction.getDate()));
        tvDetailNote.setText((transaction.getNote().isEmpty() ? "-" : transaction.getNote()));

        // Set warna jumlah
        if (transaction.getType().equals(Constants.TYPE_EXPENSE)) {
            tvDetailAmount.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tvDetailAmount.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        builder.setView(view)
                .setTitle("Detail Transaksi")
                .setPositiveButton("Tutup", null)
                .setNegativeButton("Hapus", (dialog, which) -> {
                    // Hapus dari database
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    int rowsDeleted = dbHelper.deleteTransaction(transaction.getId()); // Menghapus transaksi berdasarkan ID

                    if (rowsDeleted > 0) {
                        // Hapus dari daftar dan perbarui tampilan
                        transactions.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, transactions.size());
                        Toast.makeText(context, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Gagal menghapus transaksi", Toast.LENGTH_SHORT).show();
                    }
                });

        // Tampilkan dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateData(List<Transaction> newTransactions) {
        this.transactions = newTransactions;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvDate, tvNote, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
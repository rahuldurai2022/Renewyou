package com.example.renewyou.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renewyou.R;
import com.example.renewyou.model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.VH> {

    public interface Listener {
        void onEdit(Feedback item, int position);
        void onDelete(Feedback item, int position);
    }

    private final List<Feedback> items = new ArrayList<>();
    private final Listener listener;

    public FeedbackAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<Feedback> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    public Feedback getItem(int position) {
        return items.get(position);
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Feedback f = items.get(position);
        String nameEmail = (f.getName() != null ? f.getName() : "") + " (" + (f.getEmail() != null ? f.getEmail() : "") + ")";
        h.tvNameEmail.setText(nameEmail);
        h.tvRating.setText("Rating: " + f.getRating());
        h.tvFeedbackText.setText(f.getFeedback());
        h.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(f, h.getBindingAdapterPosition());
        });
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(f, h.getBindingAdapterPosition());
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNameEmail, tvRating, tvFeedbackText;
        Button btnEdit, btnDelete;
        VH(@NonNull View itemView) {
            super(itemView);
            tvNameEmail = itemView.findViewById(R.id.tvNameEmail);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvFeedbackText = itemView.findViewById(R.id.tvFeedbackText);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

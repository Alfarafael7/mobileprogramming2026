package com.learntospell.myapplication;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class OpsiAdapter extends RecyclerView.Adapter<OpsiAdapter.OpsiViewHolder> {

    public interface OnOpsiClickListener {
        void onOpsiClick(int position);
    }

    private final ArrayList<String> opsi = new ArrayList<>();
    private final OnOpsiClickListener listener;

    private int posisiBenar = RecyclerView.NO_POSITION;
    private int posisiDipilih = RecyclerView.NO_POSITION;
    private boolean terkunci = false;


    public OpsiAdapter(OnOpsiClickListener listener) {
        this.listener = listener;
    }

    public void setOpsi(List<String> opsiBaru, int posisiBenar) {
        opsi.clear();
        opsi.addAll(opsiBaru);
        this.posisiBenar = posisiBenar;
        posisiDipilih = RecyclerView.NO_POSITION;
        terkunci = false;
        notifyDataSetChanged();
    }

    public void tandaiJawaban(int posisi) {
        posisiDipilih = posisi;
        terkunci = true;
        notifyItemChanged(posisi);
        if (posisi != posisiBenar) {
            notifyItemChanged(posisiBenar);
        }
    }

    public void tandaiWaktuHabis() {
        terkunci = true;
        notifyItemChanged(posisiBenar);
    }

    @NonNull
    @Override
    public OpsiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("VocabQuiz", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_opsi, parent, false);
        return new OpsiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpsiViewHolder holder, int position) {
        Log.d("VocabQuiz", "onBindViewHolder posisi " + position);
        holder.teksOpsi.setText(opsi.get(position));

        Context ctx = holder.itemView.getContext();
        boolean sudahDijawab = posisiDipilih != RecyclerView.NO_POSITION;

        if (terkunci && position == posisiBenar) {
            holder.kartu.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.opsi_benar));
        } else if (terkunci && position == posisiDipilih) {
            holder.kartu.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.opsi_salah));
        } else {
            holder.kartu.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.opsi_netral));
        }
    }

    @Override
    public int getItemCount() {
        return opsi.size();
    }

    class OpsiViewHolder extends RecyclerView.ViewHolder {
        final MaterialCardView kartu;
        final TextView teksOpsi;

        OpsiViewHolder(@NonNull View itemView) {
            super(itemView);
            kartu = itemView.findViewById(R.id.kartuOpsi);
            teksOpsi = itemView.findViewById(R.id.teksOpsi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && !terkunci) {
                        listener.onOpsiClick(pos);
                    }
                }
            });
        }
    }
}
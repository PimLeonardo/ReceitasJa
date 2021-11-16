package com.example.receitasja.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.model.Lista;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.MyViewHolder> {

    private List<Lista> minhaLista;
    private Context context;

    public ListaAdapter(List<Lista> minhaLista, Context context) {
        this.minhaLista = minhaLista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista, parent, false);
        return new ListaAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Lista lista = minhaLista.get(position);

        holder.textViewNomeReceitaLista.setText(lista.getNomeReceita());
        Glide.with(context).load(lista.getFotoPostagem()).into(holder.imageFotoReceitaLista);
    }

    @Override
    public int getItemCount() {
        return minhaLista.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFotoReceitaLista;
        TextView textViewNomeReceitaLista;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFotoReceitaLista = itemView.findViewById(R.id.imageFotoReceitaLista);
            textViewNomeReceitaLista = itemView.findViewById(R.id.textViewNomeReceitaLista);
        }
    }
}

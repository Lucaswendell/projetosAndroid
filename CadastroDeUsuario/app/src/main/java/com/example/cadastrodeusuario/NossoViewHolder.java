package com.example.cadastrodeusuario;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NossoViewHolder extends RecyclerView.ViewHolder {
    final TextView nome;
    final TextView telefone;
    final ImageButton edita;
    final ImageButton delete;
    public NossoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.nome = (TextView) itemView.findViewById(R.id.name);
        this.telefone = (TextView) itemView.findViewById(R.id.telelefone);
        this.edita = (ImageButton) itemView.findViewById(R.id.iBEdit);
        this.delete = (ImageButton) itemView.findViewById(R.id.iBEDel);
    }
}

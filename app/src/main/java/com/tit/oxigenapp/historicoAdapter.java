package com.tit.oxigenapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class historicoAdapter extends FirestoreRecyclerAdapter<historico, historicoAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public historicoAdapter(@NonNull  FirestoreRecyclerOptions<historico> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull  historicoAdapter.ViewHolder holder, int position, @NonNull  historico historico) {
        holder.textViewPorcentaje.setText(String.valueOf(historico.getPorcentaje()));
        holder.textViewHipoxia.setText(historico.getHipoxia());
        holder.textViewFecha.setText(String.valueOf(historico.getFecha()));
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_historico, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewPorcentaje;
        TextView textViewHipoxia;
        TextView textViewFecha;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            textViewPorcentaje=itemView.findViewById(R.id.textViewPorcentaje);
            textViewHipoxia= itemView.findViewById(R.id.textViewHipoxia);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);


        }
    }


}

package cm.franckmouanji.labores.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import cm.franckmouanji.labores.R;

public class ReservationAdapter extends ArrayAdapter<String> {
    Context context;
    private final String[] localDateSet;
    private final String[] localNomProfSet;
    private final String[] localHoraire;



    public ReservationAdapter(Context m_context, String[] nomSet,  String[] dateSet, String[] horaire) {
        super(m_context, R.layout.item_model, dateSet);
        context = m_context;
        localDateSet = dateSet;
        localNomProfSet = nomSet;
        localHoraire = horaire;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.item_model, parent, false);
        TextView date = view.findViewById(R.id.date);
        TextView nom_prof = view.findViewById(R.id.nom_prof);
        TextView plage_horaire = view.findViewById(R.id.plage_horaire);

        date.setText(localDateSet[position]);
        nom_prof.setText(localNomProfSet[position]);
        plage_horaire.setText(localHoraire[position]);

        return view;
    }




}

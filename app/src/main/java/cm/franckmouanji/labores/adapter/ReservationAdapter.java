package cm.franckmouanji.labores.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cm.franckmouanji.labores.R;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private final String[] localDateSet;
    private final String[] localNomProfSet;
    private final String[] localHoraire;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView nom_prof;
        private final TextView plage_horaire;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            date = view.findViewById(R.id.date);
            nom_prof = view.findViewById(R.id.nom_prof);
            plage_horaire = view.findViewById(R.id.plage_horaire);
        }

        public TextView getdate() {
            return date;
        }
        public TextView getPlageHoraire() {
            return plage_horaire;
        }
        public TextView getnom_prof() {
            return nom_prof;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dateSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public ReservationAdapter(String[] nomSet,  String[] dateSet, String[] horaire) {

        localDateSet = dateSet;
        localNomProfSet = nomSet;
        localHoraire = horaire;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_model, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getdate().setText(localDateSet[position]);
        viewHolder.getnom_prof().setText(localNomProfSet[position]);
        viewHolder.getPlageHoraire().setText(localHoraire[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDateSet.length;
    }



}

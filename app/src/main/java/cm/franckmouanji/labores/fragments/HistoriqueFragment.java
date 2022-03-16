package cm.franckmouanji.labores.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.ListenerRegistration;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.activities.ShowItem;
import cm.franckmouanji.labores.databinding.FragmentHistoriqueBinding;
import cm.franckmouanji.labores.systeme.ActionAboutReservation;
import cm.franckmouanji.labores.systeme.Controller;

public class HistoriqueFragment extends Fragment {

    private FragmentHistoriqueBinding binding;
    private ListView item_list_historique;
    private TextView emptyOld;
    ListenerRegistration registration;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHistoriqueBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews(root);

        if(getContext()!=null) {
            registration = ActionAboutReservation.getReservationCollection().addSnapshotListener((queryDocumentSnapshots, e) -> ActionAboutReservation.chargeOldReservationData(getContext(), item_list_historique, emptyOld, Controller.listOldReservation));
        }

        item_list_historique.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = view.findViewById(R.id.id_prof);
                Intent intent = new Intent(getContext(), ShowItem.class);
                intent.putExtra("idR", textView.getText().toString());
                intent.putExtra("source", "history");
                requireActivity().startActivity(intent);
            }
        });
        return root;
    }

    private void initViews(View root) {
        item_list_historique = root.findViewById(R.id.item_list_historique);
        emptyOld = root.findViewById(R.id.emptyOld);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        registration = ActionAboutReservation.getReservationCollection().addSnapshotListener((queryDocumentSnapshots, e) -> ActionAboutReservation.chargeOldReservationData(getContext(), item_list_historique, emptyOld, Controller.listOldReservation));
    }
}
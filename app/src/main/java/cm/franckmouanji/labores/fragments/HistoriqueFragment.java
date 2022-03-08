package cm.franckmouanji.labores.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.ListenerRegistration;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.databinding.FragmentHistoriqueBinding;
import cm.franckmouanji.labores.system.ActionAboutReservation;
import cm.franckmouanji.labores.system.Controller;

public class HistoriqueFragment extends Fragment {

    private FragmentHistoriqueBinding binding;
    private RecyclerView item_list_historique;
    private TextView emptyOld;
    ListenerRegistration registration;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHistoriqueBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews(root);
        registration = ActionAboutReservation.getReservationCollection().addSnapshotListener((queryDocumentSnapshots, e) -> ActionAboutReservation.chargeOldReservationData(getContext(), item_list_historique, emptyOld, Controller.listOldReservation));

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
}
package cm.franckmouanji.labores.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.activities.ShowItem;
import cm.franckmouanji.labores.databinding.FragmentHomeBinding;
import cm.franckmouanji.labores.model.Reservation;
import cm.franckmouanji.labores.system.ActionAboutReservation;
import cm.franckmouanji.labores.system.Controller;
import cm.franckmouanji.labores.system.DialogInformAdd;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView item_list;
    private TextView empty;
    ListenerRegistration registration;
    private FloatingActionButton addReservation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        initViews(root);


        registration = ActionAboutReservation.getReservationCollection().addSnapshotListener((queryDocumentSnapshots, e) -> ActionAboutReservation.chargeNewReservationData(getContext(), item_list, empty, Controller.listReservation));


        addReservation.setOnClickListener(view -> DialogInformAdd.addReservation(getContext()));

        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ShowItem.class);
                intent.putExtra("idR", String.valueOf(i));
                intent.putExtra("source", "home");
                requireActivity().startActivity(intent);
                requireActivity().finish();
            }
        });

        return root;
    }

    private void initViews(View root){
        item_list = root.findViewById(R.id.item_list);
        empty = root.findViewById(R.id.empty);
        addReservation = root.findViewById(R.id.add_reservation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        registration = ActionAboutReservation.getReservationCollection().addSnapshotListener((queryDocumentSnapshots, e) -> ActionAboutReservation.chargeNewReservationData(getContext(), item_list, empty, Controller.listReservation));
    }
}
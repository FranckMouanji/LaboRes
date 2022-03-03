package cm.franckmouanji.labores.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.activities.HomeActivity;
import cm.franckmouanji.labores.adapter.ReservationAdapter;
import cm.franckmouanji.labores.databinding.FragmentHomeBinding;
import cm.franckmouanji.labores.system.ActionAboutReservation;
import cm.franckmouanji.labores.system.Controller;
import cm.franckmouanji.labores.system.DialogInformAdd;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView item_list;
    private TextView empty;
    ListenerRegistration registration;
    private FloatingActionButton addReservation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        registration = ActionAboutReservation.getReservationCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ActionAboutReservation.chargeNewReservationData(getContext(), item_list, empty, Controller.listReservation);
            }
        });

        initViews(root);


        addReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInformAdd.addReservation(getContext());
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
}
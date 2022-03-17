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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.ListenerRegistration;

import java.io.FileNotFoundException;
import java.text.ParseException;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.activities.ShowItem;
import cm.franckmouanji.labores.databinding.FragmentHomeBinding;
import cm.franckmouanji.labores.systeme.ActionAboutReservation;
import cm.franckmouanji.labores.systeme.Controller;
import cm.franckmouanji.labores.systeme.DialogInformAdd;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView item_list;
    private TextView empty;
    ListenerRegistration registration;
    private FloatingActionButton addReservation;
    private FloatingActionButton show_program;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        initViews(root);


        if(getContext()!=null){
            registration = ActionAboutReservation.getReservationCollection().addSnapshotListener((queryDocumentSnapshots, e) -> ActionAboutReservation.chargeNewReservationData(getContext(), item_list, empty, Controller.listReservation));
        }

        addReservation.setOnClickListener(view -> DialogInformAdd.addReservation(getContext()));

        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = view.findViewById(R.id.id_prof);
                Intent intent = new Intent(getContext(), ShowItem.class);
                intent.putExtra("idR", textView.getText().toString());
                intent.putExtra("source", "home");
                requireActivity().startActivity(intent);
            }
        });

        show_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Controller.createPdf("program", Controller.listReservation, getContext());
                } catch (FileNotFoundException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void initViews(View root){
        item_list = root.findViewById(R.id.item_list);
        empty = root.findViewById(R.id.empty);
        addReservation = root.findViewById(R.id.add_reservation);
        show_program = root.findViewById(R.id.show_program);

        String user = Controller.take_information_of_file_users(getContext());
        if(user.equalsIgnoreCase("fs")){
            addReservation.setVisibility(View.GONE);
        }
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
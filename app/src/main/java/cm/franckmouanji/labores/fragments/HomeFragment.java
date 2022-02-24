package cm.franckmouanji.labores.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.adapter.ReservationAdapter;
import cm.franckmouanji.labores.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView item_list;
    private Spinner type_reservation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] list= getResources().getStringArray(R.array.types_reservations);
        ArrayAdapter adapter1 = new ArrayAdapter(getContext(),
                R.layout.spinner_item, list);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);

        initViews(root);

        type_reservation.setAdapter(adapter1);


        String []data = {"21/12/2019","21/12/2019","21/12/2019","21/12/2019","21/12/2019","21/12/2019","21/12/2019","21/12/2019"};
        String []heure = {"12h-13h","12h-13h","12h-13h","12h-13h","12h-13h","12h-13h","12h-13h","12h-13h"};
        String []nom = {"Dr um","Mr Njeumen","Mr Ndje","Pr Bowong","Pr Fono","Mr Ndje","Dr Mosko","Mr Franck"};

        ReservationAdapter adapter = new ReservationAdapter(nom,data,heure);

        item_list.setLayoutManager(new LinearLayoutManager(root.getContext()));
        item_list.setAdapter(adapter);

        return root;
    }

    private void initViews(View root){
        item_list = root.findViewById(R.id.item_list);
        type_reservation = root.findViewById(R.id.type_reservation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
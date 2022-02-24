package cm.franckmouanji.labores.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.adapter.ReservationAdapter;
import cm.franckmouanji.labores.databinding.FragmentHistoriqueBinding;

public class HistoriqueFragment extends Fragment {

    private FragmentHistoriqueBinding binding;
    private RecyclerView item_list_historique;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHistoriqueBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        item_list_historique = root.findViewById(R.id.item_list_historique);

        String []data = {"21/12/2019","21/12/2019","21/12/2019","21/12/2019","21/12/2019","21/12/2019","21/12/2019","21/12/2019"};
        String []heure = {"12h-13h","12h-13h","12h-13h","12h-13h","12h-13h","12h-13h","12h-13h","12h-13h"};
        String []nom = {"Dr um","Mr Njeumen","Mr Ndje","Pr Bowong","Pr Fono","Mr Ndje","Dr Mosko","Mr Franck"};

        ReservationAdapter adapter = new ReservationAdapter(nom,data,heure);

        item_list_historique.setLayoutManager(new LinearLayoutManager(root.getContext()));
        item_list_historique.setAdapter(adapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
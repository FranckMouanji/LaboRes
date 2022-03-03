package cm.franckmouanji.labores.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.adapter.ReservationAdapter;
import cm.franckmouanji.labores.databinding.FragmentHomeBinding;
import cm.franckmouanji.labores.system.DialogInformAdd;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView item_list;
    private TextView empty;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        initViews(root);


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
        empty = root.findViewById(R.id.empty);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package cm.franckmouanji.labores.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.systeme.Controller;
import cm.franckmouanji.labores.systeme.DialogInformAdd;

public class MonCompte extends Fragment {

    public static MonCompte newInstance() {
        return new MonCompte();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mon_compte, container, false);

        /*
          declaration
         */

        //textView
        TextView plage = root.findViewById(R.id.plage);
        TextView logout = root.findViewById(R.id.logout);
        TextView update_password = root.findViewById(R.id.update_password);
        TextView add_user_account = root.findViewById(R.id.add_user_account);

        //views
        View view_plage = root.findViewById(R.id.view_plage);
        View view_add_user_account = root.findViewById(R.id.view_add_user_account);
        View view_update_password = root.findViewById(R.id.view_update_password);


        String user = Controller.take_information_of_file_users(requireContext());
        if(user.equalsIgnoreCase(Controller.ADMIN_ACCOUNT)){
            add_user_account.setVisibility(View.VISIBLE);
            plage.setVisibility(View.VISIBLE);
            view_plage.setVisibility(View.VISIBLE);
            view_add_user_account.setVisibility(View.VISIBLE);
        }

        if(user.equalsIgnoreCase("fs")){
            update_password.setVisibility(View.GONE);
            view_update_password.setVisibility(View.GONE);
        }

        plage.setOnClickListener(view -> DialogInformAdd.addPlageDialog(getContext()));

        update_password.setOnClickListener(view -> DialogInformAdd.updatePassword(getContext()));

        add_user_account.setOnClickListener(view -> DialogInformAdd.addAccount(getContext()));

        logout.setOnClickListener(view -> Controller.deconnexion(getContext()));

        return root;
    }

}
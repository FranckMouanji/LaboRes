package cm.franckmouanji.labores.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cm.franckmouanji.labores.BuildConfig;
import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.databinding.FragmentAProposBinding;

public class APropos extends Fragment {

    private FragmentAProposBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAProposBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView version_app = root.findViewById(R.id.version_app);

        version_app.setText(BuildConfig.VERSION_NAME);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
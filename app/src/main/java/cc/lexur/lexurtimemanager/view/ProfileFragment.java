package cc.lexur.lexurtimemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.databinding.FragmentProfileBinding;


/**
 * 设置页面，更改软件相关设置
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        binding.cvLabelManager.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), LabelManagerActivity.class);
            startActivity(intent);
            Log.d("test", "onViewCreated: 点击了");
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
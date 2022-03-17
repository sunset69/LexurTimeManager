package cc.lexur.lexurtimemanager.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.chip.Chip;

import java.util.List;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.ActivityLabelManagerBinding;
import cc.lexur.lexurtimemanager.room.Label;

public class LabelManagerActivity extends AppCompatActivity {

    ActivityLabelManagerBinding binding;
    TaskViewModel taskViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_label_manager);
        taskViewModel = new ViewModelProvider(this,new SavedStateViewModelFactory(getApplication(),this)).get(TaskViewModel.class);
        binding.setLifecycleOwner(this);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(){
        LiveData<List<Label>> allLabelsLive = taskViewModel.getAllLabelsLive();
        allLabelsLive.observe(this, labels -> {
            binding.tvNumber.setText(String.valueOf(labels.size()));
            for (int i = 0; i < labels.size(); i++) {
                Label label = labels.get(i);
                Chip chip = new Chip(this);
                chip.setText(label.getName());
                chip.setBackgroundColor(label.getColor().toArgb());
                binding.cgLabelManager.addView(chip);
            }
        });

        binding.btnAdd.setOnClickListener(view -> {
        });
    }
}
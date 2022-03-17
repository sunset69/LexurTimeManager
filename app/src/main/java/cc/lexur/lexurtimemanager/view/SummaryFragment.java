package cc.lexur.lexurtimemanager.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.zzhoujay.markdown.MarkDown;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.FragmentSummaryBinding;
import cc.lexur.lexurtimemanager.room.Label;
import cc.lexur.lexurtimemanager.utils.Text2Markdown;
import cc.lexur.lexurtimemanager.utils.TimePicker;


/**
 * 总结页面，提供任务相关总结
 */
public class SummaryFragment extends Fragment {

    FragmentSummaryBinding binding;
    TaskViewModel taskViewModel;

    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false);
        taskViewModel = new ViewModelProvider(requireActivity(), new SavedStateViewModelFactory(requireActivity().getApplication(), requireActivity())).get(TaskViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                TimePicker.showDatePickerDialog(getContext());
            }
        });

        LiveData<List<Label>> labelsLive = taskViewModel.getAllLabelsLive();
        labelsLive.observe(this, new Observer<List<Label>>() {
            @Override
            public void onChanged(List<Label> labels) {
                Log.d("test", "onChanged: 数据变化");
                for (int i = 0; i < labels.size(); i++) {
                    Log.d("test", "onChanged: " + labels.get(i).getName());
                }
            }
        });

        // 测试label数据库
        binding.button.setOnClickListener(view1 -> {
            Label label = new Label();
            label.setName("生活");
            taskViewModel.insertLabels(label);
        });
        binding.button2.setOnClickListener(view1 -> {
            Label label = new Label();
            int id = labelsLive.getValue().get(0).getId();
            label.setId(id);
            label.setName("修改");
            taskViewModel.updateLabels(label);
        });
        binding.button3.setOnClickListener(view1 -> {
            Label label = new Label();
            int id = labelsLive.getValue().get(0).getId();
            label.setId(id);
            taskViewModel.deleteLabels(label);

        });

    }


}
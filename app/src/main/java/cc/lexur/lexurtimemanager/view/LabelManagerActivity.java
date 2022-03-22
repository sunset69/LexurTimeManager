package cc.lexur.lexurtimemanager.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Date;
import java.util.List;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.ActivityLabelManagerBinding;
import cc.lexur.lexurtimemanager.room.Label;
import cc.lexur.lexurtimemanager.utils.ChipUtils;
import top.defaults.colorpicker.ColorPickerPopup;

public class LabelManagerActivity extends AppCompatActivity {

    ActivityLabelManagerBinding binding;
    TaskViewModel taskViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_label_manager);
        taskViewModel = new ViewModelProvider(this,new SavedStateViewModelFactory(getApplication(),this)).get(TaskViewModel.class);
        binding.setLifecycleOwner(this);
        init();

    }

    private void init(){
        LiveData<List<Label>> allLabelsLive = taskViewModel.getAllLabelsLive();
        allLabelsLive.observe(this, labels -> {
            binding.tvNumber.setText(String.valueOf(labels.size()));
            binding.cgLabelManager.removeAllViews();
            for (int i = 0; i < labels.size(); i++) {
                Label label = labels.get(i);
                Chip chip = new Chip(this);
                chip.setText(label.getName());
                chip.setChipBackgroundColor(ChipUtils.setChipColor(label.getColor()));
                binding.cgLabelManager.addView(chip);
            }
        });



        binding.btnAdd.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.create();
            dialog.show();

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_label,null);
            Button btnAdd = dialogView.findViewById(R.id.btnAdd);
            Button btnColor = dialogView.findViewById(R.id.btnColor);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);
            builder = new AlertDialog.Builder(this);
            builder.setTitle("添加标签")
                    .setView(dialogView);

            // 选择颜色
            btnColor.setOnClickListener(v -> {

                // 选择颜色对话框
                ColorPickerPopup colorPickerPopup = new ColorPickerPopup.Builder(LabelManagerActivity.this)
                        .initialColor(Color.RED) // Set initial color
//                        .enableBrightness(true) // Enable brightness slider or not
//                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("选择")
                        .cancelTitle("取消")
//                        .showIndicator(true)
//                        .showValue(true)
                        .build();
                        colorPickerPopup.show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                v.setBackgroundColor(color);
//                                Color labelColor = Color.valueOf(color);
//                                v.setTag(labelColor);
                                v.setTag(new Integer(color));
                                ((Button)v).setText("#"+Integer.toHexString(color));
                            }
                        });

            });

            // 添加
            btnAdd.setOnClickListener(view1 -> {
                Log.d("test", "init: hello");
                EditText etName = dialogView.findViewById(R.id.etLabelName);
                String name = etName.getText().toString();
                // 检测是否为空
                if (name == null || name == ""){
                    Toast.makeText(getApplicationContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 检测是否存在
                if (taskViewModel.isLabelExisted(name)){
                    Toast.makeText(getApplicationContext(), "已存在！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Label label = new Label();
                Integer color;
                if(btnColor.getTag() == null){
                    Log.d("test", "init: 未选择颜色");
                    color = Color.GRAY;
                }else {
                    color = (Integer) btnColor.getTag();
                }
                label.setName(name);
                if (color != null){
                    label.setColor(color);
                }
                label.setCreateTime(new Date().toString());

                if (name == null){
                    Toast.makeText(getApplicationContext(), "请输入标签名称！", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("test", "init: label:"+label.toString());
                    taskViewModel.insertLabels(label);
                }
                Toast.makeText(getApplicationContext(), "已添加" + label.getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            });

            // 取消
            btnCancel.setOnClickListener(v -> {
                dialog.dismiss();
            });

            dialog.getWindow().setContentView(dialogView);// 设置自定义View
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);// 打开输入法
        });
    }
}
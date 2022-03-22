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
        taskViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(TaskViewModel.class);
        binding.setLifecycleOwner(this);
        init();

    }

    private void init() {
        LiveData<List<Label>> allLabelsLive = taskViewModel.getAllLabelsLive();
        allLabelsLive.observe(this, labels -> {
            binding.tvNumber.setText(String.valueOf(labels.size()));
            binding.cgLabelManager.removeAllViews();
            for (int i = 0; i < labels.size(); i++) {
                Label label = labels.get(i);
                Chip chip = new Chip(this);
//                chip.setCheckable(true);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(view -> {
                    new AlertDialog.Builder(this)
                            .setTitle("删除标签")
                            .setMessage("是否删除" + label.getName() + "!")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    taskViewModel.deleteLabels(label);
                                    Toast.makeText(getApplicationContext(), "已删除" + label.getName(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                });

                /**
                 * 长按跳出界面
                 */
                chip.setLongClickable(true);
                chip.setOnLongClickListener(v -> {

                    Label labelModify = label;

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_label, null);
                    Button btnAdd = dialogView.findViewById(R.id.btnAdd);
                    Button btnColor = dialogView.findViewById(R.id.btnColor);
                    EditText etName = dialogView.findViewById(R.id.etLabelName);
                    Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                    btnAdd.setText("modify");
                    etName.setText(label.getName());
                    btnColor.setBackgroundColor(label.getColor());

                    // 选择颜色
                    btnColor.setOnClickListener(view -> {
                        ColorPickerPopup colorPickerPopup = new ColorPickerPopup.Builder(v.getContext())
                                .initialColor(Color.RED) // Set initial color
                                .okTitle("选择")
                                .cancelTitle("取消")
                                .build();
                        colorPickerPopup.show(view, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                view.setBackgroundColor(color);
                                view.setTag(new Integer(color));
                                ((Button) v).setText("#" + Integer.toHexString(color));
                            }
                        });

                    });

                    // 添加
                    btnAdd.setOnClickListener(view1 -> {

                        // 获取标签名称
                        String name = etName.getText().toString();
                        Toast.makeText(getApplicationContext(), "名称："+name+".", Toast.LENGTH_SHORT).show();
                        // 检测是否为空
                        if (name.equals("")) {
                            Toast.makeText(getApplicationContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 获取颜色，默认设置为灰色
                        Integer color;
                        if (btnColor.getTag() == null) {
                            color = label.getColor();
                        } else {
                            color = (Integer) btnColor.getTag();
                        }

                        // 创建Label对象
                        Label labelUpdate = new Label();
                        labelUpdate.setId(label.getId());
                        labelUpdate.setName(name);
                        labelUpdate.setColor(color);
                        labelUpdate.setCreateTime(new Date().toString());

                        // 插入
                        taskViewModel.updateLabels(labelUpdate);
                        Toast.makeText(getApplicationContext(), "已添加" + label.getName(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    });

                    // 取消
                    btnCancel.setOnClickListener(view -> {
                        dialog.dismiss();
                    });

                    dialog.getWindow().setContentView(dialogView);
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                    return false;
                });
                chip.setText(label.getName());
                chip.setChipBackgroundColor(ChipUtils.setChipColor(label.getColor()));
                binding.cgLabelManager.addView(chip);
            }
        });


        binding.btnAdd.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.create();
            dialog.show();

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_label, null);
            Button btnAdd = dialogView.findViewById(R.id.btnAdd);
            Button btnColor = dialogView.findViewById(R.id.btnColor);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);
            builder = new AlertDialog.Builder(this);
            builder.setTitle("添加标签")
                    .setView(dialogView);

            // 选择颜色
            btnColor.setOnClickListener(v -> {
                ColorPickerPopup colorPickerPopup = new ColorPickerPopup.Builder(LabelManagerActivity.this)
                        .initialColor(Color.RED) // Set initial color
                        .okTitle("选择")
                        .cancelTitle("取消")
                        .build();
                colorPickerPopup.show(v, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        v.setBackgroundColor(color);
                        v.setTag(new Integer(color));
                        ((Button) v).setText("#" + Integer.toHexString(color));
                    }
                });

            });

            // 添加
            btnAdd.setOnClickListener(view1 -> {

                // 获取标签名称
                EditText etName = dialogView.findViewById(R.id.etLabelName);
                String name = etName.getText().toString();
                // 检测是否为空
                if (name == null || name == "") {
                    Toast.makeText(getApplicationContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 检测是否存在
                if (taskViewModel.isLabelExisted(name)) {
                    Toast.makeText(getApplicationContext(), "已存在！", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 获取颜色，默认设置为灰色
                Integer color;
                if (btnColor.getTag() == null) {
                    Log.d("test", "init: 未选择颜色");
                    color = Color.GRAY;
                } else {
                    color = (Integer) btnColor.getTag();
                }

                // 创建Label对象
                Label label = new Label();
                label.setName(name);
                label.setColor(color);
                label.setCreateTime(new Date().toString());

                // 插入
                taskViewModel.insertLabels(label);
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
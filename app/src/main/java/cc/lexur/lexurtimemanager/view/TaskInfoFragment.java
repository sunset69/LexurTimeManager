package cc.lexur.lexurtimemanager.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.FragmentTaskInfoBinding;
import cc.lexur.lexurtimemanager.room.Task;


public class TaskInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "TASK_ID";

    private int taskId;
    TaskViewModel taskViewModel;
    private Task task;
    FragmentTaskInfoBinding binding;

    public TaskInfoFragment() {
        // Required empty public constructor
    }


    public static TaskInfoFragment newInstance(int taskId) {
        TaskInfoFragment fragment = new TaskInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_task_info,container,false);
        taskViewModel = new ViewModelProvider(requireActivity(), new SavedStateViewModelFactory(getActivity().getApplication(), requireActivity())).get(TaskViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
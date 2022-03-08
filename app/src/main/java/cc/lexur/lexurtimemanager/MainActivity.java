package cc.lexur.lexurtimemanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cc.lexur.lexurtimemanager.databinding.ActivityMainBinding;
import cc.lexur.lexurtimemanager.view.MyFragmentPagerAdapter;
import cc.lexur.lexurtimemanager.view.ProfileFragment;
import cc.lexur.lexurtimemanager.view.SummaryFragment;
import cc.lexur.lexurtimemanager.view.TaskFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        taskViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(TaskViewModel.class);
        mainBinding.setData(taskViewModel);
        mainBinding.setLifecycleOwner(this);

        init();
    }

    /**
     * 初始化页面
     */
    private void init() {
        // 创建页面
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TaskFragment());
        fragments.add(new SummaryFragment());
        fragments.add(new ProfileFragment());

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        // 禁止ViewPager滑动切换
        mainBinding.viewPager.setUserInputEnabled(false);
        mainBinding.viewPager.setAdapter(pagerAdapter);
        // 绑定tab点击事件
        mainBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainBinding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 绑定页面滚动事件
        mainBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mainBinding.tabLayout.selectTab(mainBinding.tabLayout.getTabAt(position));
            }
        });

    }
}
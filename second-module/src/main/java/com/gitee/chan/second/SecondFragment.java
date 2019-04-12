package com.gitee.chan.second;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gitee.chan.common.library.tab.ITabPage;
import com.google.auto.service.AutoService;


/**
 * ================================================
 * Description:  Tab 所需的Fragment.
 *
 * Created by JohnnyChan on 2018/8/1
 *
 * ================================================
 */

@ITabPage(tabName = "工作", iconName = "tab_work")
@AutoService(Fragment.class)
public class SecondFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.second_fragment, container, false);
    }

}

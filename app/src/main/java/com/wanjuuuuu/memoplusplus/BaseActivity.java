package com.wanjuuuuu.memoplusplus;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseActivity<VB extends ViewDataBinding, VM extends ViewModel> extends AppCompatActivity {

    protected VB mBinding;
    protected VM mViewModel;

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract Class<VM> getViewModel();

    protected abstract int getBindingVariable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (mViewModel == null) {
            mViewModel = new ViewModelProvider(this).get(getViewModel());
        }
//        mBinding.setVariable(getBindingVariable(), mViewModel);
//        mBinding.executePendingBindings();
    }


}

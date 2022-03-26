package com.example.social_media.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.social_media.fragment.LoginFragment;
import com.example.social_media.fragment.RegisterFragment;

public class LoginAdapter extends FragmentStateAdapter {
    int totalTabs;

    public LoginAdapter(@NonNull FragmentActivity fragmentActivity, int totalTabs) {
        super(fragmentActivity);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Log.i("position", position + "");
                Fragment loginFragment = new LoginFragment();
                return loginFragment;
            case 1:
                Log.i("position", position + "");
                Fragment registerFragment = new RegisterFragment();
                return registerFragment;
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}

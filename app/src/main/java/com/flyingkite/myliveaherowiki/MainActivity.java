package com.flyingkite.myliveaherowiki;

import android.os.Bundle;

import com.flyingkite.myliveaherowiki.util.BackPage;

import androidx.fragment.app.Fragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadHero();
    }

    @Override
    public void onBackPressed() {
        Fragment f = findFragmentById(R.id.heroFragmentArea);
        // dismiss shown dialogs of WebPage
        // dismiss the sort filter of main
        if (f instanceof BackPage) {
            BackPage p = (BackPage) f;
            if (p.onBackPressed()) {
                return;
            }
        }
    }

    private void loadHero() {
        HeroFragment hf = new HeroFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.heroFragmentArea, hf)
                .commitAllowingStateLoss();
    }
}
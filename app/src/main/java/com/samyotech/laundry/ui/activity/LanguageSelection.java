package com.samyotech.laundry.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samyotech.laundry.ModelClass.LanguageDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.adapter.AdapterLanguage;

import java.util.ArrayList;

public class LanguageSelection extends AppCompatActivity {
    private static final String TAG = LanguageSelection.class.getSimpleName();
    private SharedPrefrence sharedPrefrence;
    private Context mContext;
    private static String SKIP = "2";
    private AdapterLanguage adapterLanguage;
    private ArrayList<LanguageDTO> languageDTOList;
    private RecyclerView rvLanguage;
    public int flag = 0;
    ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
        mContext = LanguageSelection.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        init();
    }

    private void init() {
        rvLanguage = (RecyclerView) findViewById(R.id.rvLanguage);
        shoLAnguage();
    }

    private void shoLAnguage() {
        languageDTOList = new ArrayList<>();
        languageDTOList.add(new LanguageDTO("English"));
        languageDTOList.add(new LanguageDTO("Hindi"));
        GridLayoutManager gLayout = new GridLayoutManager(mContext, 2);
        adapterLanguage = new AdapterLanguage(languageDTOList, LanguageSelection.this);
        rvLanguage.setLayoutManager(gLayout);
        rvLanguage.setHasFixedSize(true);
        rvLanguage.setLayoutManager(gLayout);
        rvLanguage.setAdapter(adapterLanguage);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

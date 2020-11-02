package com.samyotech.laundry.ui.adapter;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.samyotech.laundry.ModelClass.LanguageDTO;
import com.samyotech.laundry.R;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.Dashboard;
import com.samyotech.laundry.ui.activity.LanguageSelection;
import com.samyotech.laundry.ui.activity.WelcomeScreens;

import java.util.ArrayList;
import java.util.Locale;


public class AdapterLanguage extends RecyclerView.Adapter<AdapterLanguage.LanguageHolder> {
    private ArrayList<LanguageDTO> datas = new ArrayList<>();
    private LanguageSelection mContext;
    private SharedPrefrence prefrence;
    private String half, second_half;
    ActivityOptions options;

    public AdapterLanguage(ArrayList<LanguageDTO> datas, LanguageSelection mContext) {
        this.datas = datas;
        this.mContext = mContext;
        prefrence = SharedPrefrence.getInstance(mContext);
    }

    @Override
    public LanguageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_language_holder, parent, false);
        LanguageHolder categoryHolder = new LanguageHolder(view);

        return categoryHolder;
    }

    @Override
    public void onBindViewHolder(LanguageHolder holder, final int position) {

        second_half = datas.get(position).getLanguage_name().substring(datas.get(position).getLanguage_name().length() / 2);
        half = datas.get(position).getLanguage_name().substring(0, datas.get(position).getLanguage_name().length() / 2);
        String next = "<font color='#FFFFFF'>" + half + "</font>";
        String last = "<font color='#FFFFFF'>" + second_half + "</font>";
        String complete_word = next + last;
        holder.tvLanguage.setText(Html.fromHtml(complete_word));
        final String language = datas.get(position).getLanguage_name();


        holder.lllanguagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefrence.getBooleanValue(Consts.IS_REGISTERED)) {
                    Intent in = new Intent(mContext, Dashboard.class);
                    mContext.startActivity(in);
                    mContext.finish();
                    mContext.overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                }else {
                    mContext.startActivity(new Intent(mContext, WelcomeScreens.class));
                    mContext.finish();
                    mContext.overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                }

                if (datas.get(position).getLanguage_name().equals("English")) {
                    language("en");
                } else if (datas.get(position).getLanguage_name().equals("Hindi")) {
                    language("hi");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class LanguageHolder extends RecyclerView.ViewHolder {
        public LinearLayout lllanguagelayout;
        public TextView tvLanguage;

        public LanguageHolder(View itemView) {
            super(itemView);
            tvLanguage = (TextView) itemView.findViewById(R.id.tvLanguage);
            lllanguagelayout = (LinearLayout) itemView.findViewById(R.id.lllanguagelayout);
        }

    }

    public void language(String language) {
        String languageToLoad = language; // your language
        prefrence.setValue(Consts.LANGUAGE,language);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());

    }

}

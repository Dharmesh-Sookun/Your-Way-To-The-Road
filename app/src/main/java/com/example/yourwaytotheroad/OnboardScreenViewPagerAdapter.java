package com.example.yourwaytotheroad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class OnboardScreenViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<OnboardScreenItem> onboardScreenItems;

    public OnboardScreenViewPagerAdapter(Context mContext, List<OnboardScreenItem> onboardScreenItems) {
        this.mContext = mContext;
        this.onboardScreenItems = onboardScreenItems;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_intro_onboard_screen, null);

        ImageView screenImage = layoutScreen.findViewById(R.id.intro_imgImageView);
        TextView screenTitle = layoutScreen.findViewById(R.id.intro_titleTextView);
        TextView screenDescription = layoutScreen.findViewById(R.id.intro_descTextView);

        screenTitle.setText(onboardScreenItems.get(position).getScreenTitle());
        screenImage.setImageResource(onboardScreenItems.get(position).getScreenImg());
        screenDescription.setText(onboardScreenItems.get(position).getScreenDescription());

        container.addView(layoutScreen);

        return layoutScreen;

    }

    @Override
    public int getCount() {
        return onboardScreenItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }
}

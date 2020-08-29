package com.example.yourwaytotheroad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList signDesc;
    private ArrayList imagesSign;
    private Context context;
    private onItemListener mOnItemListener;
    public static TextToSpeech mTTS;

    public RecyclerViewAdapter(Context context, ArrayList signDesc, ArrayList imagesSign, onItemListener OnItemListener) {
        this.signDesc = signDesc;
        this.imagesSign = imagesSign;
        this.context = context;
        this.mOnItemListener = OnItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_categories, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

      /*  Glide.with(context)
                .asBitmap()
                .load(imagesSign.get(position))
                .into(holder.image)   ; */

        mTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS)
                {
                    mTTS.setLanguage(Locale.UK);
                    if(mTTS.setLanguage(Locale.UK) == TextToSpeech.LANG_MISSING_DATA || mTTS.setLanguage(Locale.UK) == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("TTS", "Language not Supported!!");
                    }
                }
                else
                {
                    Log.e("TTS", "Initialisation failed!!");
                }

            }
        });

        holder.imageDesc.setText((CharSequence) signDesc.get(position));
        holder.image.setImageResource((int)imagesSign.get(position));

        holder.toSpeechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = signDesc.get(position).toString();
                mTTS.setPitch(1f);
                mTTS.setSpeechRate(1f);
                mTTS.speak(description, TextToSpeech.QUEUE_FLUSH, null, null);

            }
        });

  /*      holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(context, (CharSequence) signDesc.get(position), Toast.LENGTH_SHORT).show();
            }
        }); */

    }



    @Override
    public int getItemCount() {
        return imagesSign.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView image;
        TextView imageDesc;
        ImageButton toSpeechBtn;
        RelativeLayout parentLayout;

        onItemListener OnItemListener;

        public ViewHolder(@NonNull View itemView, onItemListener OnItemListener) {
            super(itemView);

            image = itemView.findViewById(R.id.categoryImg);
            imageDesc = itemView.findViewById(R.id.ImgDescTextView);
            toSpeechBtn = itemView.findViewById(R.id.toSpeechBtn);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.OnItemListener = OnItemListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            OnItemListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface onItemListener{
        void onItemClicked(int position);
    }
}

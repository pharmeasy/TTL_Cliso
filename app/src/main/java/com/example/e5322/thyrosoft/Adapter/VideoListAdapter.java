package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.VideosResponseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    public String TAG = getClass().getSimpleName();
    Activity activity;
    ArrayList<VideosResponseModel.Outputlang> VideosArylist;
    GlobalClass globalClass;
    OnItemClickListener onItemClickListener;
    boolean moreflag = false;

    public VideoListAdapter(Activity activity, ArrayList<VideosResponseModel.Outputlang> VideosArylist) {
        this.activity = activity;
        this.VideosArylist = VideosArylist;
        globalClass = new GlobalClass(activity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.videolist_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        myViewHolder.tv_title.setText(VideosArylist.get(position).getTitle());

        if (!TextUtils.isEmpty(VideosArylist.get(position).getDescription())) {
            myViewHolder.tv_description.setText(VideosArylist.get(position).getDescription());
            myViewHolder.tv_description.post(new Runnable() {
                @Override
                public void run() {
                    int linecount = myViewHolder.tv_description.getLineCount();
                   // Log.e(TAG, "Number of lines is " + linecount);
                    if (myViewHolder.tv_description.getLineCount() >= 3) {
                        myViewHolder.tv_show.setText(Html.fromHtml("<u>" + activity.getResources().getString(R.string.more) + "</u>"));
                        myViewHolder.tv_show.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.tv_show.setVisibility(View.GONE);
                    }
                }
            });

        }

        myViewHolder.tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!moreflag) {
                    myViewHolder.tv_description.setMaxLines(100);
                    myViewHolder.tv_show.setText(Html.fromHtml("<u>" + activity.getResources().getString(R.string.less) + "</u>"));
                    moreflag = true;
                } else {
                    myViewHolder.tv_description.setMaxLines(3);
                    myViewHolder.tv_show.setText(Html.fromHtml("<u>" + activity.getResources().getString(R.string.more) + "</u>"));
                    moreflag = false;
                }
            }
        });

        myViewHolder.tv_description.setText(VideosArylist.get(position).getDescription());

        if (VideosArylist.get(position).isVideoPlaying()) {
            myViewHolder.tv_description.setMaxLines(3);
            myViewHolder.tv_title.setTextColor(activity.getResources().getColor(R.color.maroon));
            myViewHolder.tv_description.setTextColor(activity.getResources().getColor(R.color.maroon));
            myViewHolder.tv_nowPlaying.setVisibility(View.VISIBLE);

            if (VideosArylist.get(position).isVideoPaused()) {
                myViewHolder.tv_nowPlaying.setText("Paused");
                myViewHolder.GIF_VideoPlaying.setVisibility(View.GONE);
            } else {
                myViewHolder.tv_nowPlaying.setText("Now Playing..");
                myViewHolder.GIF_VideoPlaying.setVisibility(View.VISIBLE);
            }

        } else {
            myViewHolder.tv_title.setTextColor(activity.getResources().getColor(R.color.black));
            myViewHolder.tv_description.setTextColor(activity.getResources().getColor(R.color.black));
            myViewHolder.GIF_VideoPlaying.setVisibility(View.GONE);
            myViewHolder.tv_nowPlaying.setVisibility(View.GONE);
        }

        globalClass.DisplayVideoImage(activity, VideosArylist.get(position).getImageurl(), myViewHolder.img_thumbnail);

        myViewHolder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < VideosArylist.size(); i++) {
                    if (i == position) {
                        VideosArylist.get(i).setVideoPlaying(true);
                    } else {
                        VideosArylist.get(i).setVideoPlaying(false);
                    }
                }

                if (onItemClickListener != null) {
                    onItemClickListener.OnVideoItemSelected(VideosArylist, VideosArylist.get(position));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return VideosArylist.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnVideoItemSelected(ArrayList<VideosResponseModel.Outputlang> VideoArrylist, VideosResponseModel.Outputlang SelectedVideo);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rel_main;
        ImageView img_thumbnail;
        TextView tv_title, tv_nowPlaying, tv_description, tv_show;
        GifImageView GIF_VideoPlaying;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rel_main = (RelativeLayout) itemView.findViewById(R.id.rel_main);
            img_thumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_nowPlaying = (TextView) itemView.findViewById(R.id.tv_nowPlaying);
            GIF_VideoPlaying = (GifImageView) itemView.findViewById(R.id.GIF_VideoPlaying);
            tv_show = (TextView) itemView.findViewById(R.id.tv_show);
        }
    }

}

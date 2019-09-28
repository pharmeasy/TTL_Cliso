package com.example.e5322.thyrosoft.Activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LeggyVideoAdapter extends RecyclerView.Adapter<LeggyVideoAdapter.ViewHolder> {
    private int flag = -1;
    private Context context;
    ArrayList<LeggyVideoListModel> list;
    LeggyVideo_Activity mLeggyVideo_Activity;
    ProgressDialog progressDialog;



    public LeggyVideoAdapter(LeggyVideo_Activity pActivity, Context context, ArrayList<LeggyVideoListModel> list) {
        this.mLeggyVideo_Activity = pActivity;
        this.context = context;
        this.list = list;
    }

    public LeggyVideoAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.leggyvideo, viewGroup, false);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
//        progressDialog.show();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (flag == position) {
            viewHolder.videoName.setTextColor(Color.parseColor("#B00020"));

        } else {
            viewHolder.videoName.setTextColor(Color.BLACK);
        }


        viewHolder.videoName.setText(list.get(position).getTitle());
        viewHolder.leggyVideoListModel = list.get(position);
        /*setVideo(list.get(position).getPath(),viewHolder.videoImage);*/
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.loading3.setVisibility(View.VISIBLE);
                flag = position;
                progressDialog.dismiss();
                notifyDataSetChanged();
                mLeggyVideo_Activity.ShowVideo(list.get(position));
                mLeggyVideo_Activity.videoName.setText(list.get(position).getTitle());
                Constants.videoName = list.get(position).getTitle();
                if (mLeggyVideo_Activity.video.getVisibility()==View.VISIBLE) {
                    viewHolder.loading3.setVisibility(View.GONE);
                }

            }
        });

        try {
            Bitmap bitmap = null;
            try {
                bitmap = retriveVideoFrameFromVideo(viewHolder.leggyVideoListModel.getPath(),viewHolder.leggyVideoListModel.getThumbtime());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if (bitmap != null) {
                bitmap = Bitmap.createScaledBitmap(bitmap, 250, 250, false);
                viewHolder.videoImage.setImageBitmap(bitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LeggyVideoListModel leggyVideoListModel;

        public TextView videoName;
        public ImageView videoImage;
        public CardView cardView;
        public ProgressBar loading3;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            videoName = view.findViewById(R.id.videoName);
            videoImage = view.findViewById(R.id.videoImage);
            loading3 = view.findViewById(R.id.loading3);
        }
    }

    public Bitmap retriveVideoFrameFromVideo(String videoPath, String time)
            throws Throwable {

        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        LeggyVideoListModel leggyVideoListModel1 = new
                LeggyVideoListModel();

        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
//                time = (leggyVideoListModel1.getThubtime());
//                Log.e("TIME ", " " + time);

            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new
                        HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            bitmap =
                    mediaMetadataRetriever.getFrameAtTime(Long.parseLong(time),mediaMetadataRetriever.OPTION_CLOSEST);
            Log.e("THUMB"," "+time);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Thumbnail Exception : "+ e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
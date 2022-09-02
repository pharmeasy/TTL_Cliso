package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Models.MenusModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class HomeMenusAdapter extends RecyclerView.Adapter<HomeMenusAdapter.ViewHolder> {

    Activity activity;
    private ArrayList<MenusModel> menusList;
    private OnItemClickListener onItemClickListener;

    public HomeMenusAdapter(Activity activity, ArrayList<MenusModel> arrayList) {
        this.activity = activity;
        this.menusList = arrayList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HomeMenusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_menu_item, viewGroup, false);
        return new HomeMenusAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMenusAdapter.ViewHolder viewHolder, final int position) {
        Global.setTextview(viewHolder.tv_menuName, menusList.get(position).getMenuName());
        viewHolder.iv_menuIcon.setImageResource(menusList.get(position).getMenuIcon());
        viewHolder.rel_mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMenuClickListener(menusList.get(position).getMenuPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_menuName;
        ImageView iv_menuIcon;
        RelativeLayout rel_mainMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_menuName = itemView.findViewById(R.id.tv_menuName);
            iv_menuIcon = itemView.findViewById(R.id.iv_menuIcon);
            rel_mainMenu = itemView.findViewById(R.id.rel_mainMenu);
        }
    }

    public interface OnItemClickListener {
        void onMenuClickListener(int menuPosition);
    }
}

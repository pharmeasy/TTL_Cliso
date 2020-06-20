package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class ShowChildTestNamesAdapter  extends RecyclerView.Adapter<ShowChildTestNamesAdapter.ViewHolder> {
    Context context1;
    ArrayList<String> testname;
    @NonNull
    @Override

    public ShowChildTestNamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.singletestlayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ShowChildTestNamesAdapter.ViewHolder vh = new ShowChildTestNamesAdapter.ViewHolder(v);
        return vh;

    }
    public ShowChildTestNamesAdapter(Context context, ArrayList<String> testRateMasterModel) {
        this.testname = testRateMasterModel;
        this.context1=context;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowChildTestNamesAdapter.ViewHolder holder, int position) {

        if(testname.size()!=0){
            holder.showlist.setText(testname.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return testname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showlist;
        public ViewHolder(View itemView) {
            super(itemView);
            showlist=(TextView)itemView.findViewById(R.id.show_test_name);

        }
    }
}

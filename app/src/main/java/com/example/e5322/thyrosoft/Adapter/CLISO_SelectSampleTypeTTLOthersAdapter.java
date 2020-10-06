package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;

import java.util.ArrayList;

public class CLISO_SelectSampleTypeTTLOthersAdapter extends RecyclerView.Adapter<CLISO_SelectSampleTypeTTLOthersAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<Outlabdetails_OutLab> bmcBaseModelArrayList;
    ArrayList<ScannedBarcodeDetails> BMC_TTLOthersBarcodeDetailsList;
    String sampleType;
    private ArrayList<String> sampletypeslist;
    private CLISO_SelectSampleTypeTTLOthersAdapter.OnItemClickListener onItemClickListener;

    public CLISO_SelectSampleTypeTTLOthersAdapter(Activity activity, ArrayList<Outlabdetails_OutLab> TTLOthersSelectedList, ArrayList<ScannedBarcodeDetails> BMC_TTLOthersBarcodeDetailsList) {
        this.activity = activity;
        this.bmcBaseModelArrayList = TTLOthersSelectedList;
        this.BMC_TTLOthersBarcodeDetailsList = BMC_TTLOthersBarcodeDetailsList;
    }


    public void setOnItemClickListener(CLISO_SelectSampleTypeTTLOthersAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CLISO_SelectSampleTypeTTLOthersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bmc_view_sample_type_ttlothers_item, viewGroup, false);
        return new CLISO_SelectSampleTypeTTLOthersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CLISO_SelectSampleTypeTTLOthersAdapter.MyViewHolder myViewHolder, final int position) {
        myViewHolder.sample_type.setText(bmcBaseModelArrayList.get(position).getProduct());

        sampletypeslist = new ArrayList<>();
        sampletypeslist.add("Select sample type");

            if (bmcBaseModelArrayList.get(position).getSampletype() != null) {
                for (int j = 0; j < bmcBaseModelArrayList.get(position).getSampletype().length; j++) {
                    sampletypeslist.add(bmcBaseModelArrayList.get(position).getSampletype()[j].getOutlabsampletype().trim());
                }
            }


      /*  Set<String> hashSet = new HashSet<>();
        hashSet.addAll(sampletypeslist);
        sampletypeslist.clear();
        sampletypeslist.addAll(hashSet);*/

        myViewHolder.sample_type_spinner.setAdapter(new ArrayAdapter<>(activity, R.layout.spinnerproperty, sampletypeslist));

        myViewHolder.sample_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    if (myViewHolder.sample_type_spinner.getSelectedItemPosition() == 0)
                        sampleType = "";
                    else
                        sampleType = myViewHolder.sample_type_spinner.getSelectedItem().toString().trim();

                    if (onItemClickListener != null) {
                        onItemClickListener.onItemSelected(bmcBaseModelArrayList.get(position).getProduct(), sampleType);
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return bmcBaseModelArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemSelected(String product, String sample_type);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sample_type_linear;
        Spinner sample_type_spinner;
        TextView sample_type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sample_type_linear = (LinearLayout) itemView.findViewById(R.id.sample_type_linear);
            sample_type_spinner = (Spinner) itemView.findViewById(R.id.sample_type_spinner);
            sample_type = (TextView) itemView.findViewById(R.id.sample_type);
        }
    }
}
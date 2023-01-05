package com.example.e5322.thyrosoft.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.e5322.thyrosoft.Adapter.ExpandablenameListAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.billingDetailsModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BillingDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillingDetails extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ExpandableListView lvExp;
    LinearLayout linearlay;
    ExpandableListAdapter adapter;
    HashMap<ArrayList, ArrayList<billingDetailsModel>> listDataChild;
    private SimpleDateFormat sdf;
    TextView download;
    public String Date = "";
    private OnFragmentInteractionListener mListener;

    public BillingDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillingDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static BillingDetails newInstance(String param1, String param2) {
        BillingDetails fragment = new BillingDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_billing_details, container, false);
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date = sdf.format(cl.getTime());
        linearlay =(LinearLayout)view.findViewById(R.id.linearlay);
        download=(TextView)view.findViewById(R.id.download);

        lvExp = (ExpandableListView)view.findViewById(R.id.expandlist);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //
                saveExcelFile();
            }
        });
          PrepareDataList();
        adapter = new ExpandablenameListAdapter(getActivity(), GlobalClass.billingDETheaderArray, listDataChild);
        lvExp.setAdapter(adapter);
       return  view;
    }

    private void PrepareDataList() {
        listDataChild = new HashMap<ArrayList, ArrayList<billingDetailsModel>>();
        listDataChild.put(GlobalClass.billingDETheaderArray,GlobalClass.billingDETArray);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


   private void saveExcelFile() {
        String Fnamexls="billingSummary "+Date+ ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath());
        directory.mkdirs();
        File file = new File(directory, Fnamexls);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook;
        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet("Data List", 0);

            WritableFont cellFont = new WritableFont(WritableFont.COURIER, 12);
            try {
                cellFont.setBoldStyle(WritableFont.BOLD);
            } catch (WriteException e) {
                e.printStackTrace();
            }

            WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
            try {
                cellFormat.setBackground(Colour.YELLOW);
            } catch (WriteException e) {
                e.printStackTrace();
            }

            Label labe1 = new Label(0, 0, "Barcode", cellFormat);
            Label labe2= new Label(1, 0, "Billed amount", cellFormat);
            Label labe3 = new Label(2, 0, "Collected amount", cellFormat);
            Label labe4 = new Label(3, 0, "Patient", cellFormat);
            Label labe5 = new Label(4, 0, "Tests", cellFormat);
            Label labe6 = new Label(5, 0, "Work order entry", cellFormat);
            Label labe7 = new Label(6, 0, "Ref ID", cellFormat);

            sheet.setColumnView(0, 28);
            sheet.setColumnView(1, 16);
            sheet.setColumnView(2, 16);

            sheet.addCell(labe1);
            sheet.addCell(labe2);
            sheet.addCell(labe3);
            sheet.addCell(labe4);
            sheet.addCell(labe5);
            sheet.addCell(labe6);
            sheet.addCell(labe7);

            for (int i = 0; i < GlobalClass.billingDETArray.size(); i++) {

                sheet.setColumnView(0, 10);
                sheet.setColumnView(1, 20);
                sheet.setColumnView(2, 15);
                sheet.setColumnView(3, 15);
                sheet.setColumnView(4, 15);
                sheet.setColumnView(5, 15);
                sheet.setColumnView(6, 15);

                sheet.addCell(new Label(0, i + 1, GlobalClass.billingDETArray.get(i).getBarcode()));

                sheet.addCell( new Label(1, i + 1, GlobalClass.billingDETArray.get(i).getBilledAmount()));
                sheet.addCell(new Label(2, i + 1, GlobalClass.billingDETArray.get(i).getCollectedAmount()));
                sheet.addCell( new Label(3, i + 1, GlobalClass.billingDETArray.get(i).getPatient()));
                sheet.addCell(new Label(4, i + 1, GlobalClass.billingDETArray.get(i).getTests()));
                sheet.addCell( new Label(5, i + 1, GlobalClass.billingDETArray.get(i).getWoetype()));
                sheet.addCell(new Label(6, i + 1, GlobalClass.billingDETArray.get(i).getRefId()));

            }       //String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TODO_SUBJECT));

            workbook.write();
            TastyToast.makeText(getContext(), ToastFile.xl_dtl, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

            try {
                workbook.close();
            } catch (WriteException e) {

                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

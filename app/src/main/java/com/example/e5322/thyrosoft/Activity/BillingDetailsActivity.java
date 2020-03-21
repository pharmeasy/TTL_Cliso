package com.example.e5322.thyrosoft.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class BillingDetailsActivity extends AppCompatActivity {
    ExpandableListView lvExp;
    LinearLayout linearlay;
    ExpandableListAdapter adapter;
    HashMap<ArrayList, ArrayList<billingDetailsModel>> listDataChild;
    private SimpleDateFormat sdf;
    TextView download;
    public String Date = "";
    ImageView back, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_billing_details);
            Calendar cl = Calendar.getInstance();
            sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Date = sdf.format(cl.getTime());
            linearlay = (LinearLayout) findViewById(R.id.linearlay);
            download = (TextView) findViewById(R.id.download);
        } catch (Exception e) {
            e.printStackTrace();
        }

        lvExp = (ExpandableListView) findViewById(R.id.expandlist);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(BillingDetailsActivity.this);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExcelFile();
            }
        });

        PrepareDataList();
        adapter = new ExpandablenameListAdapter(BillingDetailsActivity.this, GlobalClass.billingDETheaderArray, listDataChild);
        lvExp.setAdapter(adapter);
    }

    private void PrepareDataList() {
        listDataChild = new HashMap<ArrayList, ArrayList<billingDetailsModel>>();
        listDataChild.put(GlobalClass.billingDETheaderArray, GlobalClass.billingDETArray);
    }

    private void saveExcelFile() {
        String Fnamexls = "billingSummary " + Date + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath());
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
            try{
                cellFormat.setBackground(Colour.YELLOW);
            } catch (WriteException e) {
                e.printStackTrace();
            }

            Label labe1 = new Label(0, 0, "Barcode", cellFormat);
            Label labe2 = new Label(1, 0, "Billed amount", cellFormat);
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
                sheet.addCell(new Label(1, i + 1, GlobalClass.billingDETArray.get(i).getBilledAmount()));
                sheet.addCell(new Label(2, i + 1, GlobalClass.billingDETArray.get(i).getCollectedAmount()));
                sheet.addCell(new Label(3, i + 1, GlobalClass.billingDETArray.get(i).getPatient()));
                sheet.addCell(new Label(4, i + 1, GlobalClass.billingDETArray.get(i).getTests()));
                sheet.addCell(new Label(5, i + 1, GlobalClass.billingDETArray.get(i).getWoetype()));
                sheet.addCell(new Label(6, i + 1, GlobalClass.billingDETArray.get(i).getRefId()));
            }


            workbook.write();
            TastyToast.makeText(BillingDetailsActivity.this, ToastFile.xl_dtl, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

     /*       Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
            startActivity(intent);*/

            FileOpen.openFile(BillingDetailsActivity.this, file);

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
}

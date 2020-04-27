package com.example.e5322.thyrosoft.Interface;

import android.app.Activity;
import android.view.View;

public interface RecyclerInterface {
    public void recyclerViewListClicked(View v, int position);
    public void putDataToscan(Activity activity, int position, String specimenttype);
}

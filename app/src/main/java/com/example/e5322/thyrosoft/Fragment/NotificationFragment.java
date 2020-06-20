package com.example.e5322.thyrosoft.Fragment;

import android.content.BroadcastReceiver;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Product;
import com.example.e5322.thyrosoft.API.SqliteDatabase;
import com.example.e5322.thyrosoft.Adapter.ProductAdapter;
import com.example.e5322.thyrosoft.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view,viewnotification;
    ListView notification_list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView notifyHeader , notifycontent, noNotification;
    private SqliteDatabase mDatabase;

    RecyclerView item_list;
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    LinearLayoutManager linearLayoutManager;
    private OnFragmentInteractionListener mListener;

    ProductAdapter productAdapter;
    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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


      //  View view3 = getActivity().findViewById(R.id.show_date);
       // view3.setVisibility(View.GONE);












        viewnotification = (View) inflater.inflate(R.layout.fragment_notification, container, false);
        noNotification = (TextView) viewnotification.findViewById(R.id.noNotification);
        item_list = (RecyclerView) viewnotification.findViewById(R.id.item_list);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        item_list.setLayoutManager(linearLayoutManager);
        item_list.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this.getActivity());
        List<Product> allProducts = mDatabase.listProducts();
        if(allProducts.size() > 0){
            item_list.setVisibility(View.VISIBLE);
            ProductAdapter mAdapter = new ProductAdapter(this.getActivity(), allProducts);
            item_list.setAdapter(mAdapter);
        }else {
            item_list.setVisibility(View.GONE);
            noNotification.setVisibility(View.VISIBLE);
//            Toast.makeText(this.getActivity(), "There is no product in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        // Inflate the layout for this fragment
        return viewnotification;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

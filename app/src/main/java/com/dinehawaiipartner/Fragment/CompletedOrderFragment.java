package com.dinehawaiipartner.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinehawaiipartner.Adapter.CompletedOrderAdapter;
import com.dinehawaiipartner.Adapter.PendingOrderAdapter;
import com.dinehawaiipartner.CustomViews.CustomTextView;
import com.dinehawaiipartner.Model.OrdersModel;
import com.dinehawaiipartner.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedOrderFragment extends Fragment {
    String TAG = "Pending Order";
    Context context;
    CustomTextView noOrders;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CompletedOrderAdapter pendingAdapter;
    public static ArrayList<OrdersModel> ordersList = new ArrayList<OrdersModel>();

    public CompletedOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_completed_order, container, false) ;
        context = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        noOrders = (CustomTextView) view.findViewById(R.id.noOrder);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(pendingAdapter);
        pendingAdapter = new CompletedOrderAdapter(context, ordersList);
        mRecyclerView.setAdapter(pendingAdapter);
        pendingAdapter.notifyDataSetChanged();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (ordersList != null)
            ordersList.clear();
        noOrders.setVisibility(View.VISIBLE);
        //  getAllOrders();
    }
}

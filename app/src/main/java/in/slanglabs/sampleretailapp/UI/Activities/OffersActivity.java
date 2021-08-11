package in.slanglabs.sampleretailapp.UI.Activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import in.slanglabs.sampleretailapp.Model.Item;
import in.slanglabs.sampleretailapp.R;
import in.slanglabs.sampleretailapp.UI.Adapters.OfferItemsAdapter;
import in.slanglabs.sampleretailapp.UI.ItemClickListener;
import in.slanglabs.sampleretailapp.UI.ViewModel.AppViewModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class OffersActivity extends MainActivity implements ItemClickListener {

    private OfferItemsAdapter mListAdapter;
    private View mLoadingItemsView;
    private View mOrderEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search_list, null, false);
        mLinearLayout.addView(contentView, new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Offers");
        }

        mLoadingItemsView = contentView.findViewById(R.id.loading_items_view);
        mLoadingItemsView.setVisibility(View.GONE);
        mOrderEmptyTextView = contentView.findViewById(R.id.order_empty_text_view);
        mOrderEmptyTextView.setVisibility(View.GONE);

        RecyclerView listItemView = contentView.findViewById(R.id.list_item_view);

        AppViewModel appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);
        appViewModel.getOfferItems().observe(this,
                offerItems -> {
                    if (offerItems==null || offerItems.isEmpty()){
                        mOrderEmptyTextView.setVisibility(View.GONE);
                    }else{
                        mOrderEmptyTextView.setVisibility(View.VISIBLE);
                    }
                    mLoadingItemsView.setVisibility(View.GONE);
                    mListAdapter.setList(offerItems);
                });

        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(OffersActivity.this, CartActivity.class);
            startActivity(intent);
        });
        TextView cartItemCount = findViewById(R.id.cart_item_count);
        cartItemCount.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        appViewModel.getCartItems().observe(this,
                cartItems -> {
                    if (cartItems.size() == 0) {
                        cartItemCount.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                    } else {
                        cartItemCount.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
                    }
                    cartItemCount.setText(String.format(Locale.ENGLISH, "%d", cartItems.size()));
                });

        mListAdapter = new OfferItemsAdapter(OfferItemsAdapter.Type.VERTICAL_LIST,
                appViewModel, this, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(mListAdapter);

    }

    @Override
    public void itemClicked(Item item) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("itemId", item.itemId);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //Show the slang trigger in this activity
        mAppViewModel.getSlangInterface().showTrigger(this);
    }
}
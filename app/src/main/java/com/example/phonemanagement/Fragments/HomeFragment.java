package com.example.phonemanagement.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phonemanagement.Activities.PhoneDetailActivity;
import com.example.phonemanagement.Adapters.PhoneAdapter;
import com.example.phonemanagement.Adapters.SlideAdapter;
import com.example.phonemanagement.Models.Phone;
import com.example.phonemanagement.Models.SlideItem;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IPhoneApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewPhones;
    private PhoneAdapter phoneAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPager);
        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.add(new SlideItem(R.drawable.tech1));
        slideItems.add(new SlideItem(R.drawable.tech2));
        viewPager2.setAdapter(new SlideAdapter(slideItems,viewPager2));

        // Set up RecyclerView for phones
        recyclerViewPhones = view.findViewById(R.id.recyclerViewPhones);
        recyclerViewPhones.setLayoutManager(new LinearLayoutManager(getContext()));
        phoneAdapter = new PhoneAdapter(new ArrayList<>(), phone -> {
            // Handle item click (show detailed information)
            showPhoneDetails(phone);
        });
        recyclerViewPhones.setAdapter(phoneAdapter);

        // Fetch phone details from API
        fetchPhoneDetails();

        return view;
    }

    private void fetchPhoneDetails() {
        // Create Retrofit instance directly
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/") // Replace with your actual base URL
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the API service
        IPhoneApiService phoneApiService = retrofit.create(IPhoneApiService.class);

        // Make the API call
        Call<List<Phone>> call = phoneApiService.getPhoneDetails();
        call.enqueue(new Callback<List<Phone>>() {
            @Override
            public void onResponse(Call<List<Phone>> call, Response<List<Phone>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Phone> phoneList = response.body();
                    phoneAdapter.updatePhones(phoneList); // Update RecyclerView adapter
                } else {
                    Log.e("HomeFragment", "Response was not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Phone>> call, Throwable t) {
                Log.e("HomeFragment", "Failed to fetch data", t);
            }
        });
    }

    private void showPhoneDetails(Phone phone) {
        Intent intent = new Intent(getActivity(), PhoneDetailActivity.class);
        // Pass all phone details to the intent
        intent.putExtra("phoneId", phone.getPhoneId());
        intent.putExtra("phoneModel", phone.getModelName());
        intent.putExtra("phonePrice", phone.getPrice());
        intent.putExtra("phoneStock", phone.getStockQuantity());
        intent.putExtra("phoneDescription", phone.getDescription());
        intent.putExtra("phoneImage", phone.getImage());  // Pass image URL
        intent.putExtra("phoneChipset", phone.getChipset());
        intent.putExtra("phoneGpu", phone.getGpu());
        intent.putExtra("phoneColor", phone.getColor());
        intent.putExtra("phoneWarrantyPeriod", phone.getWarrantyPeriod());
        intent.putExtra("phoneReleaseDate", phone.getReleaseDate());
        intent.putExtra("phoneOperatingSystem", phone.getOperatingSystem());
        intent.putExtra("phoneRam", phone.getRam());
        intent.putExtra("phoneStorage", phone.getStorage());
        intent.putExtra("phoneBrandName", phone.getBrandName());

        startActivity(intent);
        // Navigate to a detail screen or show details in a dialog
        // Example: pass the phone object to a new activity
    }
}
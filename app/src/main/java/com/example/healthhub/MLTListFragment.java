package com.example.healthhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Dummy;
import com.example.healthhub.models.Product_List;

import java.util.ArrayList;
import java.util.List;

public class MLTListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m_l_t_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        //retrieve from Product Dummy
        List<Product> products = Product_Dummy.getDummyProducts();
        List<Product_List> productList = new ArrayList<>();

        for (Product product : products) {
            productList.add(new Product_List(product.getName(), product.getPrice(), product.getImageResId()));
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        MyAdapter adapter = new MyAdapter(requireContext(), productList);
        recyclerView.setAdapter(adapter);

        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}

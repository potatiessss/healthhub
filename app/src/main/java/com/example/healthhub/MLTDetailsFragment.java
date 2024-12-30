package com.example.healthhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Dummy;

public class MLTDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_l_t_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //retrieve from Product_Dummy
        TextView productName = view.findViewById(R.id.product_name);
        TextView productCategory = view.findViewById(R.id.product_category);
        TextView productPrice = view.findViewById(R.id.product_price);
        TextView usefulForTextDetails = view.findViewById(R.id.useful_for_text_details);
        TextView dosageTextDetails = view.findViewById(R.id.dosage_text_details);
        TextView postageTextDetails = view.findViewById(R.id.postage_text_details);
        ImageView productImage = view.findViewById(R.id.product_image);

        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name");
            Product product = Product_Dummy.getProductByName(name);

            if (product != null) {
                productName.setText(product.getName());
                productCategory.setText(product.getCategory());
                productPrice.setText(product.getPrice());
                usefulForTextDetails.setText(product.getUsefulFor());
                dosageTextDetails.setText(product.getDosage());
                postageTextDetails.setText(product.getPostage());
                productImage.setImageResource(product.getImageResId());
            }
        }

        ImageButton backButton = view.findViewById(R.id.back_button_MLTDetails);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}

package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthhub.R;
import com.example.healthhub.CartAdapter;
import com.example.healthhub.models.Cart_Firebase;
import com.example.healthhub.models.CartItem;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {
    private static final double DELIVERY_FEE = 8.00;
    private static final double FREE_DELIVERY_THRESHOLD = 60.00;

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private ProgressBar progressBar;
    private TextView txtEmptyCart;
    private TextView txtSubtotal;
    private TextView txtDeliveryFee;
    private TextView txtGrandTotal;
    private Button btnCheckout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initializeViews(view);
        setupRecyclerView();
        setupCheckoutButton();
        loadCartItems();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        txtEmptyCart = view.findViewById(R.id.txtEmptyCart);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);
        txtDeliveryFee = view.findViewById(R.id.txtDeliveryFee);
        txtGrandTotal = view.findViewById(R.id.txtGrandTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupCheckoutButton() {
        btnCheckout.setOnClickListener(v -> {
            CheckoutFragment fragment = new CheckoutFragment();

            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emptyFragment, fragment)
                    .addToBackStack(null)
                    .commit();
            proceedToCheckout();
        });
    }

    private void proceedToCheckout() {
    }

    private void loadCartItems() {
        progressBar.setVisibility(View.VISIBLE);

        Cart_Firebase.fetchCartItems(new Cart_Firebase.CartCallback() {
            @Override
            public void onCartItemsFetched(List<CartItem> cartItems) {
                progressBar.setVisibility(View.GONE);

                if (cartItems.isEmpty()) {
                    showEmptyCart();
                } else {
                    showCartItems(cartItems);
                }
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                // Show error message to user
            }
        });
    }

    private void showEmptyCart() {
        txtEmptyCart.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        txtSubtotal.setText("Subtotal: RM 0.00");
        txtDeliveryFee.setText("Delivery Fee: RM 0.00");
        txtGrandTotal.setText("Grand Total: RM 0.00");
        btnCheckout.setEnabled(false);
    }

    private void showCartItems(List<CartItem> cartItems) {
        txtEmptyCart.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setCartItems(cartItems);
        updatePrices(cartItems);
        btnCheckout.setEnabled(true);
    }

    private void updatePrices(List<CartItem> cartItems) {
        double subtotal = calculateSubtotal(cartItems);
        double deliveryFee = calculateDeliveryFee(subtotal);
        double grandTotal = subtotal + deliveryFee;

        txtSubtotal.setText(String.format("Subtotal: RM %.2f", subtotal));
        txtDeliveryFee.setText(String.format("Delivery Fee: RM %.2f", deliveryFee));
        txtGrandTotal.setText(String.format("Grand Total: RM %.2f", grandTotal));
    }

    private double calculateSubtotal(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    private double calculateDeliveryFee(double subtotal) {
        return subtotal >= FREE_DELIVERY_THRESHOLD ? 0.00 : DELIVERY_FEE;
    }

    @Override
    public void onQuantityChanged(String productId, int newQuantity) {
        Cart_Firebase.updateCartItemQuantity(productId, newQuantity);
    }

    @Override
    public void onRemoveItem(String productId) {
        Cart_Firebase.removeFromCart(productId);
    }
}

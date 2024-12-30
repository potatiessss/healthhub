package com.example.healthhub.models;

import com.example.healthhub.R;

import java.util.ArrayList;
import java.util.List;


//temporary class prior to firebase
public class Product_Dummy {

    public static List<Product> getDummyProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Ibuprofen (400mg)", "MEDICINE", "RM 8.00", "Relieving pain, inflammation, and fever.", "1 tablet every 6-8 hours.", "1-2 working days, RM3.00 shipping fee.", R.drawable.ibuprofen));
        products.add(new Product("Pregnancy Test", "LAB TEST", "RM 20.90", "Confirming pregnancy.", "Human Chorionic Gonadotropin (hCG) Test - Measures hCG hormone in blood or urine.", "1-2 working days, RM3.00 shipping fee.", R.drawable.pregnancy_test));
        products.add(new Product("Salbutamol Inhaler (100mcg)", "MEDICAL DEVICE", "RM 25.00", "Relieving bronchospasm in asthma.", "1-2 puffs as needed, not exceeding 8 puffs daily.", "1-2 working days, RM3.00 shipping fee.", R.drawable.inhaler));
        products.add(new Product("Amoxicillin (500mg)", "MEDICINE", "RM 20.00", "Treating bacterial infections like respiratory tract infections, UTIs, or skin infections.", "1 capsule every 8 hours for 5-7 days.", "1-2 working days, RM3.00 shipping fee.", R.drawable.amoxicillin));
        products.add(new Product("Cetirizine (10mg)", "MEDICINE", "RM 8.00", "Relieving allergies, including runny nose, sneezing, and itching.", "1 tablet once daily.", "1-2 working days, RM3.00 shipping fee.", R.drawable.cetirizine));
        products.add(new Product("Loratadine (10mg)", "MEDICINE", "RM 10.00", "Treating allergic rhinitis, hay fever, and urticaria (hives).", "1 tablet once daily.", "1-2 working days, RM3.00 shipping fee.", R.drawable.loratadine));
        products.add(new Product("Metformin (500mg)", "MEDICINE", "RM 15.00", "Managing type 2 diabetes by lowering blood glucose levels.", "1 tablet twice daily with meals, as prescribed by a doctor.", "1-2 working days, RM3.00 shipping fee.", R.drawable.metformin));
        products.add(new Product("Omeprazole (20mg)", "MEDICINE", "RM 12.00", "Treating acid reflux, GERD, and stomach ulcers.", "1 capsule once daily before breakfast, for up to 14 days or as directed.", "1-2 working days, RM3.00 shipping fee.", R.drawable.omeprazole));
        products.add(new Product("Paracetamol (500mg)", "MEDICINE", "RM 5.00", "Reducing fever and relieving mild to moderate pain (e.g., headache, muscle aches).", "1-2 tablets every 4-6 hours, not exceeding 8 tablets daily.", "1-2 working days, RM3.00 shipping fee.", R.drawable.paracetamol));




        return products;
    }

    public static Product getProductByName(String name) {
        for (Product product : getDummyProducts()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
}

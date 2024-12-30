package com.example.healthhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "create table users(username text, email text, password text)";
        sqLiteDatabase.execSQL(qry1);

        String qry2 = "create table cart(username text, product text, price float, otype text)";
        sqLiteDatabase.execSQL(qry2);

        String qry3 = "create table orderplace(username text, fullname text,address text,contactno text,pincode int,date text,time text,amount float,otype text)";
        sqLiteDatabase.execSQL(qry3);

        String createArticlesTable = "CREATE TABLE articles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "image INTEGER)"; // Assuming image is stored as a resource ID
        sqLiteDatabase.execSQL(createArticlesTable);

        String createDoctorsTable = "CREATE TABLE doctors (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "hospital_address TEXT, " +
                "field TEXT, " +
                "experience INTEGER, " +
                "mobile_no TEXT, " +
                "consultation_fee REAL, " +
                "about TEXT, " +
                "working_time TEXT, " +
                "profile_pic INTEGER, " +
                "awards INTEGER)";
        sqLiteDatabase.execSQL(createDoctorsTable);

        String createOrderTable = "CREATE TABLE orderplace (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "fullname TEXT, " +
                "field TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "doctorImage INTEGER)";
        sqLiteDatabase.execSQL(createOrderTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists cart");
        db.execSQL("drop table if exists orderplace");
        db.execSQL("drop table if exists doctors");
        onCreate(db);

    }
    public void register(String username, String email,String password){
        ContentValues cv = new ContentValues();
        cv.put("username",username);
        cv.put("email",email);
        cv.put("password",password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users",null,cv);
        db.close();

    }

    public int login(String username, String password){
        int result=0;
        String str[] = new String[2];
        str[0] = username;
        str[1]= password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from users where username=? and password=?", str);
        if(c.moveToFirst()){
            result=1;
        }
        return result;

    }

    public int checkAppointmentExists(String username, String fullname, String address, String contact, String date, String time){
        int result = 0;
        String str[] = new String[6];
        str[0] = username;
        str[1] = fullname;
        str[2] = address;
        str[3] = contact;
        str[4] = date;
        str[5] = time;

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("select * from orderplace where username = ? and fullname = ? and address = ? and contact = ? and date = ? and time = ?", str);
        if (c.moveToFirst()){
            result=1;
        }
        db.close();

        return result;

    }

    public void addCart(String username, String product, float price, String otype){
        ContentValues cv = new ContentValues();
        cv.put("username",username);
        cv.put("product", product);
        cv.put("price", price);
        cv.put("otype", otype);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("cart", null, cv);
        db.close();
    }

    public int checkCart(String username, String product){
        int result=0;
        String str[] = new String[2];
        str[0] = username;
        str[1] = product;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from cart where username = ? and product = ?", str);
        if (c.moveToFirst()){
            result=1;
        }
        db.close();
        return result;
    }

    public void removeCart(String username, String otype){
        String str[] = new String[2];
        str[0] = username;
        str[1] = otype;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart","username=? and otype=?", str);
        db.close();
    }

    public ArrayList getCartData(String username, String otype){
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String str[] = new String[2];
        str[0] = username;
        str[1] = otype;
        Cursor c = db.rawQuery("select * from cart where username = ? and otype = ?", str);
        if (c.moveToFirst()){
            do{
                String product = c.getString(1);
                String price = c.getString(2);
                arr.add(product+"$"+price);
            }while(c.moveToNext());
        }
        db.close();
        return arr;
    }

    public void addOrder(String username, String fullname, String address, String contact, int pincode,String date, String time,float price,String otype){
        ContentValues cv = new ContentValues();
        cv.put("username",username);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("contactno", contact);
        cv.put("pincode",pincode);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("amount", price);
        cv.put("otype", otype);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("orderplace", null, cv);
        db.close();
    }

    public List<Appointment> getOrderData(String username) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orderplace WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                String doctorName = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
                String appointmentTime = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                int doctorImage = R.drawable.matthew; // Replace with a valid drawable or fetch logic

                appointments.add(new Appointment(doctorName, "Specialist", date + " " + appointmentTime, doctorImage));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return appointments;
    }






    public List<Doctor> getAllDoctors() {
        List<Doctor> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM doctors", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String hospitalAddress = cursor.getString(cursor.getColumnIndexOrThrow("hospital_address"));
                String field = cursor.getString(cursor.getColumnIndexOrThrow("field"));
                int experience = cursor.getInt(cursor.getColumnIndexOrThrow("experience"));
                String mobileNo = cursor.getString(cursor.getColumnIndexOrThrow("mobile_no"));
                double consultationFee = cursor.getDouble(cursor.getColumnIndexOrThrow("consultation_fee"));
                String about = cursor.getString(cursor.getColumnIndexOrThrow("about"));
                String workingTime = cursor.getString(cursor.getColumnIndexOrThrow("working_time"));
                int profilePic = cursor.getInt(cursor.getColumnIndexOrThrow("profile_pic"));
                int awards = cursor.getInt(cursor.getColumnIndexOrThrow("awards"));

                doctorList.add(new Doctor(id, name, hospitalAddress, field, experience, mobileNo, consultationFee, about, workingTime, profilePic, awards));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return doctorList;
    }


    public void addDoctor(String name, String hospitalAddress, String field, int experience, String mobileNo, double consultationFee, String about, String workingTime, int profilePic, int awards) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("hospital_address", hospitalAddress);
        cv.put("field", field);
        cv.put("experience", experience);
        cv.put("mobile_no", mobileNo);
        cv.put("consultation_fee", consultationFee);
        cv.put("about", about);
        cv.put("working_time", workingTime);
        cv.put("profile_pic", profilePic);
        cv.put("awards", awards);

        db.insert("doctors", null, cv);
        db.close();
    }
    public void updateDoctor(int id, String name, String field, double consultationFee) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("field", field);
        cv.put("consultation_fee", consultationFee);
        db.update("doctors", cv, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<String> getAppointmentsByDoctor(String username, String doctorName) {
        List<String> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM orderplace WHERE username = ? AND fullname = ?",
                new String[]{username, doctorName});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String appointment = cursor.getString(cursor.getColumnIndexOrThrow("fullname")) + "$" +
                        cursor.getString(cursor.getColumnIndexOrThrow("address")) + "$" +
                        cursor.getString(cursor.getColumnIndexOrThrow("contactno")) + "$" +
                        cursor.getString(cursor.getColumnIndexOrThrow("date")) + "$" +
                        cursor.getString(cursor.getColumnIndexOrThrow("time"));
                appointments.add(appointment);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return appointments;
    }
    public List<Doctor> getBasicDocInfo() {
        List<Doctor> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM doctors", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String field = cursor.getString(cursor.getColumnIndexOrThrow("field"));
                String profilePic = cursor.getString(cursor.getColumnIndexOrThrow("profile_pic")); // Replace with appropriate type
                doctorList.add(new Doctor(id, name, field, profilePic));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return doctorList;
    }


    public List<Article> getLatestArticles(int count) {
        List<Article> articles = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM articles ORDER BY id DESC LIMIT ?", new String[]{String.valueOf(count)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow("image")); // Assuming image is a resource ID
                articles.add(new Article(id, title, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return articles;
    }

    public void addAppointment(String username, String doctorName, String date, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("fullname", doctorName);
        values.put("date", date);
        values.put("time", time);

        db.insert("orderplace", null, values);
        db.close();
    }

    private void loadAppointments(View view, Context context, Database database) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        AppointmentAdapter adapter = new AppointmentAdapter(database.getOrderData("current_user"), view);
        recyclerView.setAdapter(adapter);
    }

    public Doctor getDoctorById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM doctors WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Doctor doctor = new Doctor(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("hospital_address")),
                    cursor.getString(cursor.getColumnIndexOrThrow("field")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("experience")),
                    cursor.getString(cursor.getColumnIndexOrThrow("mobile_no")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("consultation_fee")),
                    cursor.getString(cursor.getColumnIndexOrThrow("about")),
                    cursor.getString(cursor.getColumnIndexOrThrow("working_time")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("profile_pic")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("awards"))
            );
            cursor.close();
            return doctor;
        }
        return null;
    }

    public List<Appointment> getAppointments(String username) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orderplace WHERE username = ?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                appointments.add(new Appointment(
                        cursor.getString(cursor.getColumnIndexOrThrow("fullname")),
                        cursor.getString(cursor.getColumnIndexOrThrow("field")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")) + " " +
                                cursor.getString(cursor.getColumnIndexOrThrow("time")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("doctorImage"))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return appointments;
    }






}

package com.factory;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.pharmacure.Model.MedicineModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Firebase_factory_inventory {

    static String  productid="";
    static long lastProductID = 0;

   static Map<String, String> map;
    static String value="";


    static String productName="";
    static String productId="";
    static String gstno="";
    static String hsnno="";
    static String companyname="";
    static String expriydate="";
    static String batchNo="";
    static String packaging="";
    static String quantity="";
    static String mrp="";
    static String rate="";
    static String sctax="";
    static String discount="";
    static String hs_amount="";
    static String base_amount="";
    static String Sctax_amount="";
    static String discount_amount="";
    static String total_amount="";

    ArrayList<MedicineModel> models = new ArrayList<>();




    public static void addProduct(String productName,String productID,String hsnNo,String companyName,String mrp,String rate,String expdate,String packaging,String quantity,String batchNo,String gstNo,String discountperc,String cstaxperc,String hsamount,String baseamount,String discountamount,String sctaxamount,String totalamount){
        String uid=Firebase_factory.getfbUserId();
        //String uniqueProductkey=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Inventory").child("productList").push().getKey();
        String generatedproductid=getproductid();
        DatabaseReference db=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Inventory").child("productList").child(generatedproductid);
        db.keepSynced(true);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db.child("productName").setValue(productName);
                db.child("productID").setValue(generatedproductid);
                db.child("GST NO").setValue(gstNo);
                db.child("HSN_NO").setValue(hsnNo);
                db.child("companyName").setValue(companyName);
                db.child("expriyDate").setValue(expdate);
                db.child("batchNo").setValue(batchNo);
                db.child("packaging").setValue(packaging);
                db.child("quantity").setValue(quantity);
                db.child("MRP").setValue(mrp);
                db.child("rate").setValue(rate);
                db.child("S+C tax%").setValue(cstaxperc);
                db.child("discount%").setValue(discountperc);
                db.child("hs_amount").setValue(hsamount);
                db.child("base_amount").setValue(baseamount);
                db.child("S+C tax_amount").setValue(sctaxamount);
                db.child("discount_amount").setValue(discountamount);
                db.child("total_amount").setValue(totalamount);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm");
        String todaysdate= format.format(date);
        String time=timeformat.format(date);
        String message=productName+" Added Successfully";

        //adding Log
        DatabaseReference dblog=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Log");
        dblog.keepSynced(true);
        dblog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dblog.child("log "+todaysdate+" "+time).setValue(message.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public static ArrayList<MedicineModel> fetch_all_childData(ArrayList<MedicineModel> models){



      String  uid = Firebase_factory.getfbUserId();
       DatabaseReference dbinventory = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Inventory");
        dbinventory.child("productList").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                models.clear();
                int childrencount = (int) dataSnapshot.getChildrenCount();

                for (int i = 1; i <= childrencount; i++) {
                     productName = dataSnapshot.child(String.valueOf(i)).child("productName").getValue(String.class);
                    productId = dataSnapshot.child(String.valueOf(i)).child("productID").getValue(String.class);
                    gstno = dataSnapshot.child(String.valueOf(i)).child("GST NO").getValue(String.class);
                    hsnno = dataSnapshot.child(String.valueOf(i)).child("HSN_NO").getValue(String.class);
                    companyname = dataSnapshot.child(String.valueOf(i)).child("companyName").getValue(String.class);
                    expriydate = dataSnapshot.child(String.valueOf(i)).child("expriyDate").getValue(String.class);
                    batchNo = dataSnapshot.child(String.valueOf(i)).child("batchNo").getValue(String.class);
                    packaging = dataSnapshot.child(String.valueOf(i)).child("packaging").getValue(String.class);
                    quantity = dataSnapshot.child(String.valueOf(i)).child("quantity").getValue(String.class);
                    mrp = dataSnapshot.child(String.valueOf(i)).child("MRP").getValue(String.class);
                    rate = dataSnapshot.child(String.valueOf(i)).child("rate").getValue(String.class);
                    sctax = dataSnapshot.child(String.valueOf(i)).child("S+C tax%").getValue(String.class);
                    discount = dataSnapshot.child(String.valueOf(i)).child("discount%").getValue(String.class);
                    hs_amount = dataSnapshot.child(String.valueOf(i)).child("hs_amount").getValue(String.class);
                    base_amount = dataSnapshot.child(String.valueOf(i)).child("base_amount").getValue(String.class);
                    Sctax_amount = dataSnapshot.child(String.valueOf(i)).child("S+C tax_amount").getValue(String.class);
                    discount_amount = dataSnapshot.child(String.valueOf(i)).child("discount_amount").getValue(String.class);
                    total_amount = dataSnapshot.child(String.valueOf(i)).child("total_amount").getValue(String.class);

                    MedicineModel model = new MedicineModel(productName, batchNo, quantity, expriydate, mrp, rate, companyname, productId, hsnno, packaging, gstno, discount, sctax);
                    models.add(model);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return models;

    }

    public static void fetch_fill_values(String productid, EditText productName,EditText productID,EditText hsnNo
            ,EditText companyName,EditText mrp,EditText rate,EditText expdate,EditText packaging
            ,EditText quantity,EditText batchNo,EditText gstNo,EditText hsamount
            ,EditText sctaxpercentage,EditText discountpercentage,
             EditText baseamount,EditText sctaxamount,EditText discountamount
            ,EditText totalamount){
        String uid=Firebase_factory.getfbUserId();

        DatabaseReference productlistRef=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Inventory").child("productList").child(productid);
        productlistRef.keepSynced(true);

        productlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                };
                Map<String, String> map = snapshot.getValue(genericTypeIndicator);

                productID.setText(map.get("productID"));
                productName.setText(map.get("productName"));
                gstNo.setText(map.get("GST NO"));
                hsnNo.setText(map.get("HSN_NO"));
                companyName.setText(map.get("companyName"));
                quantity.setText(map.get("quantity"));
                packaging.setText(map.get("packaging"));
                expdate.setText(map.get("expriyDate"));
                batchNo.setText(map.get("batchNo"));
                mrp.setText(map.get("MRP"));
                rate.setText(map.get("rate"));
                hsamount.setText(map.get("hs_amount"));
                discountpercentage.setText(map.get("discount%"));
                sctaxpercentage.setText(map.get("S+C tax%"));
                baseamount.setText(map.get("base_amount"));
                discountamount.setText(map.get("discount_amount"));
                sctaxamount.setText(map.get("S+C tax_amount"));
                totalamount.setText(map.get("total_amount"));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("Method:"+map);

    }


public static String getProductName(String productid){
    String uid = Firebase_factory.getfbUserId();

    DatabaseReference productListRef = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Inventory").child("productList");
    productListRef.keepSynced(true);

    productListRef.child(productid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            value=snapshot.child("productName").getValue(String.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
        return value;
}

public static void updatedata(String selectedproductID, String productName,
                              String productID,String hsnNo,String companyName,String mrp,
                              String rate,String expdate,String packaging,String quantity,
                              String batchNo,String gstNo,String discountperc,String cstaxperc
            ,String hsamount,String baseamount,String discountamount,String sctaxamount,
                              String totalamount ){

    String uid = Firebase_factory.getfbUserId();
    DatabaseReference productListRef = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Inventory").child("productList").child(selectedproductID);
    productListRef.keepSynced(true);
    productListRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            productListRef.child("productName").setValue(productName);
            productListRef.child("GST NO").setValue(gstNo);
            productListRef.child("HSN_NO").setValue(hsnNo);
            productListRef.child("companyName").setValue(companyName);
            productListRef.child("expriyDate").setValue(expdate);
            productListRef.child("batchNo").setValue(batchNo);
            productListRef.child("packaging").setValue(packaging);
            productListRef.child("quantity").setValue(quantity);
            productListRef.child("MRP").setValue(mrp);
            productListRef.child("rate").setValue(rate);
            productListRef.child("S+C tax%").setValue(cstaxperc);
            productListRef.child("discount%").setValue(discountperc);
            productListRef.child("hs_amount").setValue(hsamount);
            productListRef.child("base_amount").setValue(baseamount);
            productListRef.child("discount_amount").setValue(discountamount);
            productListRef.child("S+C tax_amount").setValue(sctaxamount);
            productListRef.child("total_amount").setValue(totalamount);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    Date date=new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm");
    String todaysdate= format.format(date);
    String time=timeformat.format(date);
    String message=productName+" Updated Successfully";

    //adding Log
    DatabaseReference dblog=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Log");
    dblog.keepSynced(true);
    dblog.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            dblog.child("log "+todaysdate+" "+time).setValue(message.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

}

    public static String getproductid(){
        String uid = Firebase_factory.getfbUserId();
        DatabaseReference productListRef = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Inventory").child("productList");
        productListRef.keepSynced(true);

        productListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    lastProductID=snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        productid=String.valueOf(lastProductID+1);
        System.out.println(productid);
        return productid;

    }
}







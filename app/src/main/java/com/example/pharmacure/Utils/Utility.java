package com.example.pharmacure.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.Transactions.newTransaction.BillAdapter;
import com.example.pharmacure.Bill.salesBill.BillPrintAdapter;
import com.example.pharmacure.Transactions.newTransaction.ProductAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Utility  extends Activity{

    static SharedPreferences sharedPreferences;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        BillAdapter listAdapter = (BillAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static float calculateScaleToFit(View view, int pageWidth, int pageHeight, int sideMarginInDp, int topMarginInDp, int bottomMarginInDp) {
        float scale = view.getResources().getDisplayMetrics().density;

        int sideMarginInPx = (int) (sideMarginInDp * scale + 0.5f); // Convert dp to pixels
        int topMarginInPx = (int) (topMarginInDp * scale + 0.5f); // Convert dp to pixels
        int bottomMarginInPx = (int) (bottomMarginInDp * scale + 0.5f); // Convert dp to pixels

        int contentWidth = pageWidth - (2 * sideMarginInPx);
        int contentHeight = pageHeight - (topMarginInPx + bottomMarginInPx);

        float scaleX = (float) contentWidth / view.getWidth();
        float scaleY = (float) contentHeight / view.getHeight();

        return Math.min(scaleX, scaleY) * 0.9f; // 0.9f as per your previous logic
    }

    public static void setListViewHeightBasedOnChildren_bill(ListView listView) {
        BillPrintAdapter listAdapter = (BillPrintAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public static void setListViewHeightBasedOnChildren_product(ListView listView) {
        ProductAdapter listAdapter = (ProductAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static double calculatetotalamount_bill(ArrayList<BillModel> billlistmodel){
        int childrencount=billlistmodel.size();
        double totalamount=0.0;
        for (int i=0;i<childrencount;i++){
            totalamount=totalamount+(Double.valueOf(billlistmodel.get(i).getAmount()));

        }
        return totalamount;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double calculate_base_amount(double rate,int quantity,double hs_amount){
        double base_amount=rate*quantity-hs_amount;
        return base_amount;
    }


    public static double calculatetax(double baseamount,int taxrate){
        double taxamount=(baseamount*taxrate)/100;
        return taxamount;
    }

    public static double calculate_discount(double baseamount,int discountpercentage){
        double discountamount=(baseamount*discountpercentage)/100;
        return discountamount;
    }

    public static double calculate_total_amount(double basamount,double cstaxamount,double discountamount){
        double totalamount=basamount+cstaxamount-discountamount;
        return totalamount;
    }


    public static double calculatetaxablevalue(double mrp,int quantity,double sgstvalue,double cgstvalue,double discountvalue){
        double taxablevalue=(mrp*quantity)-sgstvalue-cgstvalue-discountvalue;
        return taxablevalue;
    }
    public static double calculatediscountvalue(double mrp,int unit,int quantity,double sgstvalue,double cgstvalue,double taxablevalue){
        //double discountvalue=(mrp*quantity/unit)-sgstvalue-cgstvalue-taxablevalue;
        double discountvalue=(mrp*(Double.parseDouble(String.valueOf(quantity))/Double.parseDouble(String.valueOf(unit))))-sgstvalue-cgstvalue-taxablevalue;
        return discountvalue;
    }
    public static double calucatediscperc(double discountvalue,double mrp,int unit,int quantity){
        double discperc=(discountvalue/mrp*((Double.parseDouble(String.valueOf(quantity))/Double.parseDouble(String.valueOf(unit)))))*100;
        return discperc;
    }

    double setdiscperc=5.00;
    //double calculateddiscvalue=(rateperunit*finalquantity*setdiscperc)*100.00;
    //double finalamount=(rateperunit*finalquantity)-calculateddiscvalue;
    //double taxvalue=(rateperunit*finalquantity)*(Double.parseDouble(model.getGst())/100.00+Double.parseDouble(model.getGst()));
   // double valuewithouttax=rateperunit*finalamount-taxvalue;
    //double calctaxvalue=valuewithouttax-(valuewithouttax*setdiscperc/100.00);

    /*public static double calctaxvalue(double taxrate,double rateperunit,double discper,double quantity){
        double calculateddiscvalue=(rateperunit*quantity*discper)/100.00;
        //168.50*5/100=8.425
        double finalamount=(rateperunit*quantity)-calculateddiscvalue;
        //168.50-8.425=160.07
        double taxvalue=(rateperunit*quantity)*(taxrate/100.00+taxrate);
        //168*12/112= 18.05
        double valuewithouttax=(rateperunit*quantity)-taxvalue;
        //168.50-18.05=150.45
        //150.45-(150.45*5/100)
        return valuewithouttax-(valuewithouttax*(discper/100.00));

    }*/

    public static double calctaxvalue(double taxrate, double rateperunit, double discper, double quantity){
        double calculateddiscvalue = (rateperunit * quantity * discper) / 100.00;
        double finalamount = (rateperunit * quantity) - calculateddiscvalue;

        double taxvaluedivisor=100.00+taxrate;
        double taxvalue = (rateperunit * quantity)* (taxrate / taxvaluedivisor);

        double valuewithouttax = (rateperunit * quantity) - taxvalue;

        return valuewithouttax - (valuewithouttax * (discper / 100.00));
    }

    protected  boolean checkTimeDifference() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        long exitTime = sharedPreferences.getLong("ExitTime", 0);
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - exitTime;

        return exitTime != 0 && timeDifference <= 10000;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected  void saveExitTime() {
        long currentTime = System.currentTimeMillis();
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("ExitTime", currentTime);
        editor.apply();
    }

    public static String getRealPathFromURI(final Context context, final Uri uri) {
        String path = "";
        try {
            path = processUri(context, uri);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (TextUtils.isEmpty(path)) {
            path = copyFile(context, uri);
        }
        return path;
    }

    private static String processUri(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String path = "";
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                //Starting with Android O, this "id" is not necessarily a long (row number),
                //but might also be a "raw:/some/file/path" URL
                if (id != null && id.startsWith("raw:/")) {
                    Uri rawuri = Uri.parse(id);
                    path = rawuri.getPath();
                } else {
                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads"
                    };
                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse(contentUriPrefix), Long.valueOf(id));
                        path = getDataColumn(context, contentUri, null, null);
                        if (!TextUtils.isEmpty(path)) {
                            break;
                        }
                    }
                }
            } else if (isMediaDocument(uri)) { // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                path = getDataColumn(context, contentUri, selection, selectionArgs);
            }  else if ("content".equalsIgnoreCase(uri.getScheme())) {
                path = getDataColumn(context, uri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) { // MediaStore (and general)
            path = getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) { // File
            path = uri.getPath();
        }
        return path;
    }

    static String copyFile(Context context, Uri uri) {
        try {
            InputStream attachment = context.getContentResolver().openInputStream(uri);
            if (attachment != null) {
                String filename = getContentName(context.getContentResolver(), uri);
                if (filename != null) {
                    File file = new File(context.getCacheDir(), filename);
                    FileOutputStream tmp = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    while (attachment.read(buffer) > 0) {
                        tmp.write(buffer);
                    }
                    tmp.close();
                    attachment.close();
                    return file.getAbsolutePath();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private static String getContentName(ContentResolver resolver, Uri uri) {
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (nameIndex >= 0) {
                String name = cursor.getString(nameIndex);
                cursor.close();
                return name;
            }
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        String result = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                result = cursor.getString(index);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



}

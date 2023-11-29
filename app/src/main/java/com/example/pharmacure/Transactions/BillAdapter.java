package com.example.pharmacure.Transactions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.Model.MedicineModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Utils.Utility;

import java.util.ArrayList;

public class BillAdapter extends ArrayAdapter<BillModel> {

    Context context;
    ArrayList<BillModel> listModels = new ArrayList<>();

    int res;

    public BillAdapter(Context context, int resource, ArrayList<BillModel> listModels) {
        super(context, resource, listModels);
        this.res = resource;
        this.listModels = listModels;
        this.context = context;
    }



    private class ViewHolder {

        TextView  batchno,packaging,expiry,mrp,rate,gst,amount,productname,companyname,qty,looseqty;
        Button  qty_label;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        final BillModel model = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(res, null);
            holder = new ViewHolder();

           holder.rate=convertView.findViewById(R.id.rate);
           holder.productname=convertView.findViewById(R.id.textviewproductName);
           holder.companyname=convertView.findViewById(R.id.textViewCompanyName);
           holder.expiry=convertView.findViewById(R.id.expiry_listitem);
           holder.batchno=convertView.findViewById(R.id.batchno_listitem);
           holder.packaging=convertView.findViewById(R.id.packaging_listitem);
           holder.mrp=convertView.findViewById(R.id.mrp_listitem);
            holder.gst = (TextView) convertView.findViewById(R.id.gst_list_item);
            holder.amount = (TextView) convertView.findViewById(R.id.amount_listitem);
            holder.qty=(TextView)convertView.findViewById(R.id.qty_listitems);
            holder.looseqty=(TextView) convertView.findViewById(R.id.looseqty_listitems);

            holder.qty_label=(Button) convertView.findViewById(R.id.qty_list_btn);

            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();

        holder.rate.setText(model.getRate());
        holder.gst.setText(model.getGst());
        //holder.amount.setText(String.valueOf(Double.valueOf(model.getMrp())*Double.valueOf(model.getQuantity())));
        holder.amount.setText(model.getTotalAmount());
        holder.productname.setText(model.getProductName());
        holder.companyname.setText(model.getCompanyName());
        holder.batchno.setText(model.getBatch());
        holder.packaging.setText(model.getPackaging());
        holder.mrp.setText(model.getMrp());
        holder.expiry.setText(model.getExpirydate());
        holder.qty.setText(model.getQuantity());

        final String oldamount=holder.amount.getText().toString();


        //holder.qty_label.setText(model.getQuantity());
        final String a = model.getTotalAmount();
        final String p = model.getPackaging();

        if (p.length()>10){
            holder.qty.setText(model.getQuantity()+" Strip");
            holder.looseqty.setText(model.getLooseqty()+" loose");

            holder.qty_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    View promptView = layoutInflater.inflate(R.layout.input_tablet, null);
                    AlertDialog.Builder alertDialogBuildertablet = new AlertDialog.Builder(getContext());
                    alertDialogBuildertablet.setView(promptView);

                    final EditText edittextqty_wholestrip=promptView.findViewById(R.id.edittext_whole_stripQty);
                    final EditText editText_looseQty=promptView.findViewById(R.id.edittextlosequantity);
                    alertDialogBuildertablet.setCancelable(false).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String wholseStripQty=edittextqty_wholestrip.getText().toString().trim();
                            String looseqty=editText_looseQty.getText().toString().trim();

                            //validating extfields of quantity and loose qauntity
                            if (wholseStripQty.isEmpty() || wholseStripQty.equals("0")){
                                if (looseqty.isEmpty() || looseqty.equals("0")){
                                    Toast.makeText(context.getApplicationContext(), "Enter Valid Quantity",Toast.LENGTH_SHORT).show();
                                }

                            }
                            //condition if wholsestrip is 0 and only loose quantity is entered
                            if (wholseStripQty.isEmpty() || wholseStripQty.equals("0")){
                                if (Integer.parseInt(looseqty)>0){
                                    if (Integer.parseInt(looseqty)<Integer.parseInt(model.getPackaging().substring(11))){
                                        double looseprice=(Double.valueOf(looseqty)/Double.valueOf(model.getPackaging().substring(11))*Double.valueOf(model.getMrp()));
                                        double newamount=(TransactionActivity.billtotalamount-Double.parseDouble(oldamount))+looseprice;
                                        TransactionActivity.billtotalamount=Utility.round(newamount,2);
                                        model.setQuantity("0");
                                        model.setLooseqty(looseqty);
                                        model.setTotalAmount(String.valueOf(Utility.round(looseprice,2)));
                                        TransactionActivity.totalamounttext.setText(String.valueOf(TransactionActivity.billtotalamount));
                                        notifyDataSetChanged();
                                        dialog.dismiss();

                                        }else {
                                        Toast.makeText(context.getApplicationContext(), "loose quantity should be less than "+model.getPackaging().substring(11), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }


                            if (!wholseStripQty.equals("") && !wholseStripQty.equals("0")) {
                                if (Integer.parseInt(wholseStripQty) <= Integer.parseInt(model.getOriginalQuantity())) {
                                    if (looseqty.equals("")) {
                                        double wholestripprice=Double.valueOf(wholseStripQty)*Double.valueOf(model.getMrp());
                                        double newamount=(TransactionActivity.billtotalamount-Double.parseDouble(oldamount))+wholestripprice;
                                        TransactionActivity.billtotalamount=Utility.round(newamount,2);
                                        model.setQuantity(wholseStripQty);
                                        model.setLooseqty("0");
                                        model.setTotalAmount(String.valueOf(Utility.round(wholestripprice,2)));
                                        TransactionActivity.totalamounttext.setText(String.valueOf(TransactionActivity.billtotalamount));
                                        notifyDataSetChanged();
                                        dialog.dismiss();


                                    }else {
                                        double wholestripprice=Double.valueOf(wholseStripQty)*Double.valueOf(model.getMrp());
                                        double looseprice=(Double.valueOf(looseqty)/Double.valueOf(model.getPackaging().substring(10))*Double.valueOf(model.getMrp()));
                                        double newamount=(TransactionActivity.billtotalamount-Double.parseDouble(oldamount))+wholestripprice+looseprice;
                                        TransactionActivity.billtotalamount=Utility.round(newamount,2);
                                        model.setQuantity(wholseStripQty);
                                        model.setLooseqty(looseqty);
                                        model.setTotalAmount(String.valueOf(Utility.round(wholestripprice+looseprice,2)));
                                        TransactionActivity.totalamounttext.setText(String.valueOf(TransactionActivity.billtotalamount));
                                        notifyDataSetChanged();
                                        dialog.dismiss();

                                    }
                                }else {
                                    Toast.makeText(context, "Quantity not in stock", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(context, "Enter valid quantity", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    // create an alert dialog
                    AlertDialog alert = alertDialogBuildertablet.create();
                    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    alert.show();


                }
            });

        }else {
            holder.looseqty.setText("");
            holder.qty_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    View promptView = layoutInflater.inflate(R.layout.input_dia, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setView(promptView);

                    final EditText editText = (EditText) promptView.findViewById(R.id.edittext_intputqty_others);

                    alertDialogBuilder.setCancelable(false).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            String qt = editText.getText().toString().trim();

                            if (!qt.equals("") && !qt.equals("0")) {
                                if (Integer.parseInt(qt) <= Integer.parseInt(model.getOriginalQuantity())) {
                                    double newprice=Double.valueOf(qt)*Double.valueOf(model.getMrp());
                                    double newamount=(TransactionActivity.billtotalamount-Double.parseDouble(oldamount))+newprice;
                                    TransactionActivity.billtotalamount=Utility.round(newamount,2);
                                    model.setQuantity(qt);
                                    model.setTotalAmount(String.valueOf(Utility.round(newprice,2)));
                                    TransactionActivity.totalamounttext.setText(String.valueOf(TransactionActivity.billtotalamount));
                                    notifyDataSetChanged();
                                    dialog.dismiss();



                                }

                                else {
                                    Toast.makeText(context, "Quantity not in stock", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "Enter valid quantity", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    // create an alert dialog
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    alert.show();
                }
            });
        }



        return convertView;
    }

    public double roundOff(double d) {
        d = Math.round(d * 100.0);
        d = d / 100.0;
        return d;
    }

}

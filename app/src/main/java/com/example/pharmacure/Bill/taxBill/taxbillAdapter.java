package com.example.pharmacure.Bill.taxBill;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class taxbillAdapter extends ArrayAdapter<BillModel> {

    int res;
    Context con;
    ArrayList<BillModel> taxbillmodel=new ArrayList<>();

    DatatransferInterface dtinterface;
    int finalquantity=0;
    double finaldiscountvalue=0.0;

   public int setquantity=0;

   ArrayList<Integer> totalqty=new ArrayList<>();

    int firstpageitemscount=0;
    int lastpagecount=0;


    int count=0;

    public taxbillAdapter(Context con, int res, ArrayList<BillModel> taxbillmodel, DatatransferInterface dtinterface) {
        super(con, res, taxbillmodel);
        this.res = res;
        this.con = con;
        this.taxbillmodel = taxbillmodel;
        this.dtinterface=dtinterface;

    }

    public taxbillAdapter(  Context con,int res, ArrayList<BillModel> taxbillmodel,DatatransferInterface dtinterface,int firstpageitemscount,int lastpagecount) {
        super(con, res, taxbillmodel);
        this.res = res;
        this.con = con;
        this.taxbillmodel = taxbillmodel;
        this.dtinterface=dtinterface;
        this.firstpageitemscount=firstpageitemscount;
        this.lastpagecount=lastpagecount;

    }

    private class ViewHolder{
        TextView batchno,unit,expiry,mrp,rate,amount,productname,qty,totalamount,sr,sgstrate,sgstvalue,
        cgstrate,cgstvalue,discountrate,hsnno,taxablevalue;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder=null;
        final BillModel model = (BillModel) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView==null){
            convertView=mInflater.inflate(res,null);
            holder=new ViewHolder();

            holder.productname=(TextView)convertView.findViewById(R.id.productnametaxbill);
            holder.expiry=(TextView)convertView.findViewById(R.id.productexptaxbill);
            holder.batchno=(TextView)convertView.findViewById(R.id.productbatchnotaxbill);
            holder.unit=(TextView)convertView.findViewById(R.id.productunittaxbill);
            holder.rate=convertView.findViewById(R.id.productratetaxbill);
            holder.qty=(TextView)convertView.findViewById(R.id.productqtytaxbill);
            holder.amount=(TextView)convertView.findViewById(R.id.productamount);
            holder.sr=(TextView) convertView.findViewById(R.id.serialNo_bill);
            holder.sgstrate=(TextView) convertView.findViewById(R.id.productsgstratetaxbill);
            holder.sgstvalue=(TextView)convertView.findViewById(R.id.productsgstvaluetaxbill);
            holder.cgstrate=(TextView)convertView.findViewById(R.id.productcgstratetaxbill);
            holder.cgstvalue=(TextView)convertView.findViewById(R.id.productcgstvaluetaxbill);
            holder.discountrate=(TextView)convertView.findViewById(R.id.productdisctaxbill);
            holder.hsnno=(TextView)convertView.findViewById(R.id.productnamehsntaxbill);
            holder.mrp=(TextView)convertView.findViewById(R.id.productmrptaxbill);

            holder.taxablevalue=convertView.findViewById(R.id.producttaxablevaluebill);
            convertView.setTag(holder);

        }
        else {

            holder=(ViewHolder) convertView.getTag();
        }
        String unit="";
        //1 strip of 10 //1000ml
        holder.productname.setText(model.getProductName());
        holder.expiry.setText(model.getExpirydate());
        holder.batchno.setText(model.getBatch());

        int quantity= Integer.parseInt(model.getQuantity());
        int loosequantity= Integer.parseInt(model.getLooseqty());


        if (lastpagecount>0){
            int serialnumber=1+position+firstpageitemscount;
            holder.sr.setText(String.valueOf(serialnumber));

        }else {
            int serialnumber = 1 + position;
            holder.sr.setText(String.valueOf(serialnumber));
        }

        holder.mrp.setText(model.getMrp());

        if (model.getPackaging().length()>10){
            unit=model.getPackaging().substring(11);
            holder.unit.setText(unit);
        }else {
            unit="1";
            holder.unit.setText(unit);
        }

        if (quantity>0){
            if (loosequantity>0){
                finalquantity=Integer.parseInt(unit)*quantity+loosequantity;
            }
        }
        if (quantity>0){
            if (loosequantity==0){
                finalquantity=Integer.parseInt(unit)*quantity;
            }
        }

        if (quantity==0){
            if (loosequantity>0){
                finalquantity=loosequantity;
                Log.d("loosequantity",finalquantity+"");
                System.out.println(finalquantity);
            }
        }


        double rateperunit=Double.valueOf(model.getMrp())/Double.valueOf(unit);
        holder.rate.setText(String.valueOf(rateperunit));

        holder.qty.setText(String.valueOf(finalquantity));

        if (holder.qty.getText().toString().isEmpty()){
            setquantity=0;
        }else {
            setquantity=Integer.parseInt(String.valueOf(holder.qty.getText()));

        }

        totalqty.add(setquantity);
        //String taxablevalue=taxBill_Activity.rate;
        holder.hsnno.setText(model.getHsnocode());

        double mrp=Double.parseDouble(model.getMrp());

        double setdiscperc=5.00;
       /* double calculateddiscvalue=(rateperunit*finalquantity*setdiscperc)*100.00;
        double finalamount=(rateperunit*finalquantity)-calculateddiscvalue;
        double taxvalue=(rateperunit*finalquantity)*(Double.parseDouble(model.getGst())/100.00+Double.parseDouble(model.getGst()));
        double valuewithouttax=rateperunit*finalamount-taxvalue;
        double calctaxvalue=valuewithouttax-(valuewithouttax*setdiscperc/100.00);*/

        double value=Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity),2);

        //double taxablevalue=Double.parseDouble(model.getRate())*Double.parseDouble(String.valueOf(Double.parseDouble(String.valueOf(finalquantity))/Double.parseDouble(unit)));
        //holder.taxablevalue.setText(String.valueOf(Double.parseDouble((model.getRate()))*Double.parseDouble(String.valueOf((finalquantity/Integer.parseInt(unit))))));
        holder.taxablevalue.setText(String.valueOf(Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity),2)));

        String sgstrate=String.valueOf((Double.parseDouble(model.getGst()))/2.0);
        String cgstrate=String.valueOf((Double.parseDouble(model.getGst()))/2.0);

        String cgstvalue=String.valueOf(Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity)*Double.parseDouble(cgstrate)/100.00,2));
        String sgatvalue=String.valueOf(Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity)*Double.parseDouble(sgstrate)/100.00,2));

        holder.sgstrate.setText(String.valueOf((Double.parseDouble(model.getGst()))/2.0));
        holder.cgstrate.setText(String.valueOf((Double.parseDouble(model.getGst()))/2.0));

        double amount=(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity)+Double.parseDouble(cgstvalue)+Double.parseDouble(sgatvalue));

        double dcgstvalue=Double.parseDouble(cgstvalue);
        double dsgstvalue=Double.parseDouble(sgatvalue);

        finaldiscountvalue=finaldiscountvalue+Utility.calculatediscountvalue(mrp,Integer.parseInt(unit),finalquantity,dsgstvalue,dcgstvalue,Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity));
        holder.sgstvalue.setText(String.valueOf((Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity)*Double.parseDouble(sgstrate)/100.00,2))));
        holder.cgstvalue.setText(String.valueOf((Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity)*Double.parseDouble(cgstrate)/100.00,2))));
        //holder.discountrate.setText(String.valueOf(Utility.round(Utility.calucatediscperc(finaldiscountvalue,mrp,Integer.parseInt(unit),finalquantity), 2)));
        holder.discountrate.setText(String.valueOf(setdiscperc));
        holder.amount.setText(String.valueOf(Utility.round(amount,2)));

        Log.d("total no of items",taxbillmodel.size()+"");

        dtinterface.settotalqty(finalquantity);
        dtinterface.settotalbillamount(amount);
        dtinterface.setdiscountvalues(finaldiscountvalue);
        dtinterface.settotalsgstvalue(Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity)*Double.parseDouble(sgstrate)/100.00,2));
        dtinterface.settotalcgstvalue(Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity)*Double.parseDouble(cgstrate)/100.00,2));
        dtinterface.settotal_taxablevalue(Utility.round(Utility.calctaxvalue(Double.parseDouble(model.getGst()),rateperunit,setdiscperc,finalquantity),2));
        dtinterface.setsgstrate(String.valueOf((Double.parseDouble(model.getGst()))/2.0));
        dtinterface.setcgstrate(String.valueOf((Double.parseDouble(model.getGst()))/2.0));

        return convertView;


    }




    public int getFinalquantity() {

        return finalquantity;
    }

    public void setFinalquantity(int finalquantity) {

        this.finalquantity = finalquantity;
    }

    public double getFinaldiscountvalue() {

        return finaldiscountvalue;
    }

    public void setFinaldiscountvalue(double finaldiscountvalue) {
        this.finaldiscountvalue = finaldiscountvalue;
    }




}

package com.creditgaea.sample.credit.java.creditgea.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creditgaea.sample.credit.java.chat.utils.TimeUtils;
import com.creditgaea.sample.credit.java.creditgea.activity.WalletActivity;
import com.creditgaea.sample.credit.java.creditgea.model.Invoice;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.Result;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.webservice.User;
import com.google.gson.Gson;
import com.quickblox.sample.videochat.java.R;

import java.util.ArrayList;
import java.util.List;

import static com.creditgaea.sample.credit.java.creditgea.activity.WalletActivity.isPaidType;

public class WalletInvoiceAdapter extends RecyclerView.Adapter<WalletInvoiceAdapter.WalletRecordHolder> {

    private  AdapterInvoiceListener adapterInvoiceListener;
    private  User user;
    private  Context context;
    private List<Invoice> walletList;
    public interface AdapterInvoiceListener{
        public void onSelected(boolean isAccept,Invoice invoice);
    }


    public WalletInvoiceAdapter(Context context, ArrayList<Invoice> walletList,AdapterInvoiceListener adapterInvoiceListener){
        this.context = context;
        this.walletList = walletList;
        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);
        if(profileJson!=null){
             user =  new Gson().fromJson(profileJson,User.class);
        }
        this.adapterInvoiceListener = adapterInvoiceListener;
    }
    @NonNull
    @Override
    public WalletRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.list_item_wallet_record,parent,false);
        return new WalletRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletRecordHolder holder, int position) {

        holder.tv_wallet.setText("$"+walletList.get(position).getMoney());
       // holder.tv_description.setText(""+walletList.get(position).getDescription());
        holder.tv_description.setVisibility(View.VISIBLE);
        holder.tv_subtitle.setVisibility(View.VISIBLE);

        if(user.getId().equalsIgnoreCase(walletList.get(position).getUserId())){



        }else {

            if(isPaidType){

                if(!walletList.get(position).getStatus().equalsIgnoreCase("1")){

                    holder.tv_title.setText("Paid To "+walletList.get(position).getSenderName());
                    holder.lin_invoice.setVisibility(View.GONE);
                    holder.tv_subtitle.setText(""+ TimeUtils.getTimeInAmPm(walletList.get(position).getDate()));
                    if(walletList.get(position).getDescription()!=null && !walletList.get(position).getDescription().isEmpty()){
                        holder.tv_description.setText(""+walletList.get(position).getDescription());
                    }

                    if(walletList.get(position).getStatus().equalsIgnoreCase("3")){
                        holder.itemView.setVisibility(View.GONE);
                    }
                }else {
                    holder.itemView.setVisibility(View.GONE);
                }


            }else {
                if(walletList.get(position).getStatus().equalsIgnoreCase("1")){
                    holder.tv_title.setText("Request From "+walletList.get(position).getSenderName());
                    holder.lin_invoice.setVisibility(View.VISIBLE);
                    holder.tv_subtitle.setText(""+ TimeUtils.getTimeInAmPm(walletList.get(position).getDate()));
                    if(walletList.get(position).getDescription()!=null && !walletList.get(position).getDescription().isEmpty()){
                        holder.tv_description.setText(""+walletList.get(position).getDescription());
                    }
                }else {
                    holder.itemView.setVisibility(View.GONE);
                }

            }


        }

        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInvoiceListener.onSelected(true,walletList.get(position));
            }
        });

        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInvoiceListener.onSelected(false,walletList.get(position));
            }
        });

      //  holder.tv_subtitle.setText(""+walletList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }

    public class WalletRecordHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_wallet;
        private TextView tv_subtitle;
        private TextView tv_description;
        private LinearLayout lin_invoice;
        private TextView tv_accept;
        private TextView tv_reject;
        public WalletRecordHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_date);
            tv_wallet = itemView.findViewById(R.id.tv_wallet);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_accept = itemView.findViewById(R.id.tv_accept);
            tv_reject = itemView.findViewById(R.id.tv_reject);
            lin_invoice = itemView.findViewById(R.id.lin_invoice);

        }
    }
}

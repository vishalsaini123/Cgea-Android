package com.creditgaea.sample.credit.java.creditgea.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creditgaea.sample.credit.java.chat.utils.TimeUtils;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.Result;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.webservice.User;
import com.google.gson.Gson;
import com.quickblox.sample.videochat.java.R;

import java.util.List;

public class WalletTransactionAdapter extends RecyclerView.Adapter<WalletTransactionAdapter.WalletRecordHolder> {

    private  User user;
    private  Context context;
    private List<Result> walletList;

    public WalletTransactionAdapter(Context context, List<Result> walletList){
        this.context = context;
        this.walletList = walletList;
        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);
        if(profileJson!=null){
             user =  new Gson().fromJson(profileJson,User.class);
        }
    }
    @NonNull
    @Override
    public WalletRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.list_item_wallet_record,parent,false);
        return new WalletRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletRecordHolder holder, int position) {
        holder.tv_subtitle.setVisibility(View.VISIBLE);
        holder.tv_description.setVisibility(View.VISIBLE);
        holder.tv_wallet.setText("$"+walletList.get(position).getScore());
        holder.tv_description.setText(""+walletList.get(position).getDescription());
        if(user.getUserEmail().equalsIgnoreCase(walletList.get(position).getReceiverEmail())){

            holder.tv_title.setText("Received From "+walletList.get(position).getSenderName());
            holder.tv_wallet.setTextColor(Color.GREEN);


        }else {
            holder.tv_title.setText("Sent To "+walletList.get(position).getReceiver_name());
            holder.tv_wallet.setTextColor(Color.RED);
            if(walletList.get(position).getScore().contains("-")){
                holder.tv_wallet.setText("$"+walletList.get(position).getScore().replace("-",""));
            }
        }

        holder.tv_subtitle.setText(""+ TimeUtils.getTimeInAmPm(walletList.get(position).getDate()));
        if(walletList.get(position).getDescription()==null||walletList.get(position).getDescription().isEmpty()){
            holder.tv_description.setVisibility(View.GONE);
        }
        holder.tv_description.setText(""+walletList.get(position).getDescription());


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
        public WalletRecordHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_date);
            tv_wallet = itemView.findViewById(R.id.tv_wallet);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }
}

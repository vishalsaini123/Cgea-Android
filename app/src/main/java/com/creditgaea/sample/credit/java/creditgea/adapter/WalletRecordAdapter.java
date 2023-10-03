package com.creditgaea.sample.credit.java.creditgea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creditgaea.sample.credit.java.chat.utils.TimeUtils;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.quickblox.sample.videochat.java.R;

import java.util.List;

public class WalletRecordAdapter extends RecyclerView.Adapter<WalletRecordAdapter.WalletRecordHolder> {

    private  Context context;
    private List<Wallet> walletList;

    public WalletRecordAdapter(Context context, List<Wallet> walletList){
        this.context = context;
        this.walletList = walletList;
    }
    @NonNull
    @Override
    public WalletRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.list_item_wallet_record,parent,false);
        return new WalletRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletRecordHolder holder, int position) {

        if(walletList.get(position).getType().equalsIgnoreCase("2")){
            holder.tv_wallet.setText("$"+walletList.get(position).getWallet());
            holder.tv_title.setText(""+ TimeUtils.getTimeInAmPm(walletList.get(position).getDate()));
        }else {
           holder.itemView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }

    public class WalletRecordHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_wallet;
        private TextView tv_subtitle;
        public WalletRecordHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_date);
            tv_wallet = itemView.findViewById(R.id.tv_wallet);
        }
    }
}

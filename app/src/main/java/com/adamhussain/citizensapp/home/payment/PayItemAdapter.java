package com.adamhussain.citizensapp.home.payment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adamhussain.citizensapp.R;

import java.util.List;

public class PayItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PayItem> payItems;
    private Context context;

    public PayItemAdapter(List<PayItem> payItems, Context context) {
        this.payItems = payItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_payment_item,parent,false), payItems,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MenuViewHolder){
            ((MenuViewHolder)holder).onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return payItems.size();
    }

    private static class MenuViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private List<PayItem> payItems;
        private ImageView itemImg;
        private LinearLayout bg;
        private Context context;

        public MenuViewHolder(@NonNull View itemView, List<PayItem> payItems, Context context) {
            super(itemView);
            this.payItems = payItems;
            title = itemView.findViewById(R.id.payment_title);
            itemImg = itemView.findViewById(R.id.payment_img);
            bg = itemView.findViewById(R.id.bg);
            this.context = context;
        }

        public void onBind(final int position){
            title.setText(payItems.get(position).getTitle());
            itemImg.setImageResource(payItems.get(position).getBgRes());

            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ConfirmDetailsActivity.class);
                    intent.putExtra("item",payItems.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }
}

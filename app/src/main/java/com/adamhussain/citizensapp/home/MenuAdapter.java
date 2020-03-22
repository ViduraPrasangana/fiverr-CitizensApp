package com.adamhussain.citizensapp.home;

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

import java.util.List;

import com.adamhussain.citizensapp.R;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MenuItem> menuItems;
    private Context context;

    public MenuAdapter(List<MenuItem> menuItems, Context context) {
        this.menuItems = menuItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_menu_item,parent,false),menuItems,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MenuViewHolder){
            ((MenuViewHolder)holder).onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    private static class MenuViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private List<MenuItem> menuItems;
        private ImageView menuItemBg;
        private LinearLayout bg;
        private Context context;

        public MenuViewHolder(@NonNull View itemView, List<MenuItem> menuItems,Context context) {
            super(itemView);
            this.menuItems = menuItems;
            title = itemView.findViewById(R.id.menu_item_title);
            menuItemBg = itemView.findViewById(R.id.menu_item_bg);
            bg = itemView.findViewById(R.id.bg);
            this.context = context;
        }

        public void onBind(final int position){
            title.setText(menuItems.get(position).getTitle());
            title.setTextColor(menuItems.get(position).getTextColor());
            menuItemBg.setImageResource(menuItems.get(position).getBgRes());

            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context,menuItems.get(position).getClasz()));
                }
            });
        }
    }
}

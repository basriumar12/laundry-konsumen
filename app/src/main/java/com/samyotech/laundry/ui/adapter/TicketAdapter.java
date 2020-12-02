package com.samyotech.laundry.ui.adapter;

/**
 * Created by VARUN on 01/01/19.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.AdapterTicketBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.TicketDTO;
import com.samyotech.laundry.model.UserDTO;
import com.samyotech.laundry.ui.activity.TicketChatActivity;
import com.samyotech.laundry.ui.activity.TicketsActivity;

import java.util.ArrayList;


public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private final Context mContext;
    private final TicketsActivity tickets;
    private final ArrayList<TicketDTO> ticketDTOSList;
    private final UserDTO userDTO;
    private LayoutInflater layoutInflater;
    private AdapterTicketBinding binding;


    public TicketAdapter(TicketsActivity tickets, Context mContext, ArrayList<TicketDTO> ticketDTOSList, UserDTO userDTO) {
        this.tickets = tickets;
        this.mContext = mContext;
        this.ticketDTOSList = ticketDTOSList;
        this.userDTO = userDTO;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_ticket, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.binding.headerTitle.setText(ticketDTOSList.get(position).getTitle());
        holder.binding.des.setText(ticketDTOSList.get(position).getDescription());

        if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.binding.status.setText("Pending");
        } else if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.binding.status.setText("Sedang diproses");
        } else if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.binding.status.setText("Selesai");
        } else if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("3")) {
            holder.binding.status.setText("Ditokak");
        }

        holder.binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ticketDTOSList.get(position).getStatus().equalsIgnoreCase("2")) {
                    Intent in = new Intent(mContext, TicketChatActivity.class);
                    in.putExtra(Consts.TICKET_ID, ticketDTOSList.get(position).getTiket_id());
                    mContext.startActivity(in);
                }

            }
        });


    }

    @Override
    public int getItemCount() {

        return ticketDTOSList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterTicketBinding binding;

        public MyViewHolder(@NonNull AdapterTicketBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }


}
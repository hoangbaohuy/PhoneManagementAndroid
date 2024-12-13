package com.example.phonemanagement.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonemanagement.Models.Response.OrderResponse;
import com.example.phonemanagement.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>{
    private final List<OrderResponse> orders;

    public PaymentAdapter(List<OrderResponse> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        OrderResponse order = orders.get(position);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.paymentId.setText(order.getPayment() != null
                ? "PaymentId: " + order.getPayment().getPaymentId()
                : "No Payment");
        holder.paymentDate.setText(order.getPayment() != null
                ? "PaymentDate: " + order.getPayment().getPaymentDate()
                : "");
        holder.paymentMethod.setText(order.getPayment() != null
                ? "PaymentMethod: " + order.getPayment().getPaymentMethod()
                : "");
        holder.amountPaid.setText(order.getPayment() != null
                ? "AmountPaid: " + numberFormat.format(order.getPayment().getAmountPaid()) + "VND"
                : "");
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView paymentId, paymentDate, paymentMethod, amountPaid;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentId = itemView.findViewById(R.id.textPaymentId);
            paymentDate = itemView.findViewById(R.id.textPaymentDate);
            paymentMethod = itemView.findViewById(R.id.textPaymentMethod);
            amountPaid = itemView.findViewById(R.id.textAmountPaid);
        }
    }
}

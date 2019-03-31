package com.example.dailyexpensenote.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpensenote.BottomSheet;
import com.example.dailyexpensenote.R;
import com.example.dailyexpensenote.activity.UpdateExpense;
import com.example.dailyexpensenote.database.DatabaseHelper;
import com.example.dailyexpensenote.model.Expense;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExpenseShowAdapter extends RecyclerView.Adapter<ExpenseShowAdapter.ViewHolder> {

    private List<Expense> expenseList;
    private Context context;

    public ExpenseShowAdapter(List<Expense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expnese_details_layout,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpenseShowAdapter.ViewHolder viewHolder, final int i) {
        final Expense currentExpense = expenseList.get(i);
        viewHolder.expTypeTV.setText(currentExpense.getExpenseType());
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM, YYYY");
        viewHolder.expDateTV.setText(sdf2.format(currentExpense.getExpnseDate()));
        viewHolder.expAmountTV.setText(String.valueOf(currentExpense.getExpenseAmount()));


        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.setArguments(bundle);
                //bottomSheet.show(v.getSupportFragmentManager(),"exp");

                Toast.makeText(context, "Details Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.popUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,viewHolder.popUpMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete_item:
                                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                                int id = databaseHelper.deleteData(currentExpense.getExpenseId());
                                if(id>0){
                                    expenseList.remove(currentExpense);
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();
                                }

                                return  true;
                            case R.id.update_itme:
                                Intent intent = new Intent(context,UpdateExpense.class);
                                intent.putExtra("expType",currentExpense.getExpenseType());
                                intent.putExtra("expAmount",currentExpense.getExpenseAmount());
                                intent.putExtra("expDate",currentExpense.getExpnseDate());
                                intent.putExtra("expTime",currentExpense.getExpenseTime());
                                intent.putExtra("expImage",currentExpense.getExpenseDocument());
                                intent.putExtra("expId",currentExpense.getExpenseId());
                                context.startActivity(intent);
                                return  true;
                        }

                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView expTypeTV, expDateTV, expAmountTV;
        private ImageView popUpMenu;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expTypeTV = itemView.findViewById(R.id.expenseTitleTV);
            expDateTV = itemView.findViewById(R.id.expneseDateTV);
            expAmountTV = itemView.findViewById(R.id.expeneAmountTV);
            popUpMenu = itemView.findViewById(R.id.expenseMenuIV);
            linearLayout = itemView.findViewById(R.id.itemLL);
        }
    }
}

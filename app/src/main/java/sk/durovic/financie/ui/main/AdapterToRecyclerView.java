package sk.durovic.financie.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.R;
import sk.durovic.financie.controller.Item;
import sk.durovic.financie.data.ContributionsToItem;

public class AdapterToRecyclerView extends RecyclerView.Adapter<AdapterToRecyclerView.ViewHolder> {
    private List<Item> listOfItems;
    private OnItemClickListener clickListener;
    private GoalsListViewModel mViewModel;

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener, ViewModel vM){
        this.clickListener = listener;
        this.mViewModel = (GoalsListViewModel)vM;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nazov;
        public TextView info;
        public TextView inBank_Overall;
        public ProgressBar progress;
        public TextView progressPercentage;
        public ImageView imageDelete;
        public ImageView imageEdit;
        public TextView daysLeft;

        public ViewHolder(View v) {
            super(v);
            this.nazov = v.findViewById(R.id.nazov);
            this.info = v.findViewById(R.id.info);
            this.inBank_Overall = v.findViewById(R.id.inBank_Overall);
            this.progress = v.findViewById(R.id.progressBar2);
            this.progressPercentage = v.findViewById(R.id.progressPercentage);
            this.daysLeft = v.findViewById(R.id.textView5);


        }

        void bind(final Item item){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()!=RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public AdapterToRecyclerView(List<Item> mDataset) {
        this.listOfItems = mDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nazov.setText(listOfItems.get(position).getName());
        holder.info.setText(listOfItems.get(position).getInfo());
        holder.inBank_Overall.setText(String.valueOf(Math.round(listOfItems.get(position).getInBank()*100)/100.0) + " / " + String.valueOf(Math.round(listOfItems.get(position).getOverall()*100)/100.0));

        double prog = listOfItems.get(position).getInBank() / listOfItems.get(position).getOverall();

        holder.progress.setProgress((int)(100*prog));
        holder.progressPercentage.setText(String.valueOf(Math.round(10000*prog)/100.00) + " %");
        
        holder.daysLeft.setText(SavingFrequency.getLeft(listOfItems.get(position)));


        holder.bind(listOfItems.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }
}

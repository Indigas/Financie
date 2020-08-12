package sk.durovic.financie.ui.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.R;
import sk.durovic.financie.data.ContributionsToItem;

public class AdapterContributionsView extends RecyclerView.Adapter<AdapterContributionsView.ViewHolder> {
    private onClickItemListener onClickItemListener;
    private List<ContributionsToItem> contributions;
    private static int checked = -1;
    private GoalsListViewModel mViewModel;


    interface onClickItemListener {
       // void onClickItemList(int position, View vh);
        void onClickDelete(int position);
        void onClickEdit(int position);
    }

    public void setOnClickItemListener(onClickItemListener listener){ onClickItemListener = listener; }
    public AdapterContributionsView(List<ContributionsToItem> list) { contributions = list; }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dateItem;
        private TextView contributionItem;
        private View deleteContribution;
        private View editContribution;
        private onClickItemListener listener;


        public ViewHolder(@NonNull final View itemView, final onClickItemListener listener) {
            super(itemView);
            this.dateItem = itemView.findViewById(R.id.itemDate);
            this.contributionItem = itemView.findViewById(R.id.itemContribution);
            this.deleteContribution = itemView.findViewById(R.id.deleteContribution);
            this.editContribution = itemView.findViewById(R.id.editContribution);
            this.listener = listener;

        }

        void bind(final ContributionsToItem contr){
            if (checked == -1) {
                deleteContribution.setVisibility(View.INVISIBLE);
                editContribution.setVisibility(View.INVISIBLE);
            } else {
                if (checked == getAdapterPosition()) {
                    deleteContribution.setVisibility(View.VISIBLE);
                    editContribution.setVisibility(View.VISIBLE);
                } else {
                    deleteContribution.setVisibility(View.INVISIBLE);
                    editContribution.setVisibility(View.INVISIBLE);
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteContribution.setVisibility(View.VISIBLE);
                    editContribution.setVisibility(View.VISIBLE);
                    if (checked != getAdapterPosition()) {
                        notifyItemChanged(checked);
                        checked = getAdapterPosition();
                    }
                }
            });

            deleteContribution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickDelete(getAdapterPosition());
                    checked=-1;
                }
            });

            editContribution.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onClickEdit(getAdapterPosition());
                    checked=-1;
                }
            });
        }

        public int getCurrentPosition(){
            if(checked != -1){
                return getAdapterPosition();
            }
            return -1;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.contributions_single_item, parent, false);

        return new ViewHolder(root, onClickItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dateItem.setText(contributions.get(position).addDate);
        double suma = contributions.get(position).contribution;


        if(suma < 0) {
            holder.contributionItem.setText(String.valueOf(suma));
            holder.contributionItem.setTextColor(0xFFE31717);
        }
        else {
            holder.contributionItem.setText("+ " + String.valueOf(suma));
            holder.contributionItem.setTextColor(0xFF37B33C);
        }

        holder.bind(contributions.get(position));

    }

    @Override
    public int getItemCount() {
        return contributions.size();
    }


}

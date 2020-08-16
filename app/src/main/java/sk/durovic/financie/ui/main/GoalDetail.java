package sk.durovic.financie.ui.main;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.MainActivity;
import sk.durovic.financie.R;
import sk.durovic.financie.controller.Item;
import sk.durovic.financie.controller.ItemController;
import sk.durovic.financie.data.ContributionsToItem;
import sk.durovic.financie.data.ItemContributions;
import sk.durovic.financie.databinding.FragmentGoalDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalDetail extends Fragment {
    private FragmentGoalDetailBinding binding;
    private GoalsListViewModel mViewModel;
    private List<ContributionsToItem> listOfContributions = new LinkedList<>();
    private RecyclerView.LayoutManager layoutManager;
    private AdapterContributionsView mAdapter;
    private View holderBack;
   // boolean editable = false;
    int onDelete = -1;


    public GoalDetail() {
        // Required empty public constructor
    }


    public static GoalDetail newInstance() {
        GoalDetail fragment = new GoalDetail();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGoalDetailBinding.inflate(getLayoutInflater(), container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(GoalsListViewModel.class);


        ((MainActivity)requireActivity()).showEditButton();



        createItemContributions();

        mViewModel.getCurrentItem().observe(getViewLifecycleOwner(), new Observer<Item>() {
            @Override
            public void onChanged(Item s) {

                /// --------------------------------------------
                binding.nazov.setText(s.getName());
                binding.inBank.setText(String.valueOf(s.inBank));
                binding.overall.setText(String.valueOf(s.overall));

                if(s.getEndDate()==null)
                    binding.endDate.setText("");
                else
                    binding.endDate.setText(s.endDate);

                double perc = s.getInBank() / s.getOverall();
                binding.percentage.setText(Math.round(perc*10000)/100.00 + " %");
                binding.progressBar3.setProgress((int)(100*perc));
                // -----------------------------------------------------------
                // pridat kolko ostava a kolko treba setrit
                // LEFT
                binding.daysLeft.setText(SavingFrequency.getLeft(s));
                binding.toSave.setText(SavingFrequency.getToSave(s));

            }
        });

        mViewModel.getDatabaseAccess().getAllContributions(mViewModel.getItemForEdit().ItemId).observe(getViewLifecycleOwner(), new Observer<List<ContributionsToItem>>() {
            @Override
            public void onChanged(List<ContributionsToItem> contributionsToItems) {

                listOfContributions.clear();
                listOfContributions.addAll(contributionsToItems);
                Collections.sort(listOfContributions, (o1,o2) -> -o1.addDate.compareTo(o2.addDate));


                if(onDelete==-1)
                    mAdapter.notifyDataSetChanged();
                else {
                    mAdapter.notifyItemRemoved(onDelete);
                    mAdapter.notifyItemRangeChanged(onDelete, mAdapter.getItemCount());
                    onDelete=-1;
                }

                if(!listOfContributions.isEmpty()) {
                    binding.emptyList.setVisibility(View.GONE);
                    binding.contributionsView.setVisibility(View.VISIBLE);
                } else{
                    binding.emptyList.setVisibility(View.VISIBLE);
                    binding.contributionsView.setVisibility(View.GONE);
                }
            }
        });




        mAdapter.setOnClickItemListener(new AdapterContributionsView.onClickItemListener() {

            @Override
            public void onClickDelete(int position) {
                onDelete=position;
                onDeleteContribution(position);
            }

            @Override
            public void onClickEdit(int position) {
                addToContribution df = new addToContribution(listOfContributions.get(position), position);
                df.show(getChildFragmentManager(), "EDIT");
            }
        });



        binding.addContributionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToContribution df = new addToContribution(null, -1);
                df.show(getChildFragmentManager(), "ADD CONTRIBUTION");
            }
        });

        return binding.getRoot();
    }

    private void createItemContributions(){
        binding.contributionsView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        binding.contributionsView.setLayoutManager(layoutManager);

        mAdapter = new AdapterContributionsView(listOfContributions);
        binding.contributionsView.setAdapter(mAdapter);

    }

    void onDeleteContribution (int position){
        ItemController itemController = new ItemController(mViewModel.getItemForEdit(), mViewModel);
        itemController.changeOnDeleteItem(listOfContributions.get(position));
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
    }

}

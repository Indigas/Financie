package sk.durovic.financie.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.R;
import sk.durovic.financie.controller.ContributionsController;
import sk.durovic.financie.data.ContributionsToItem;
import sk.durovic.financie.data.ItemContributions;
import sk.durovic.financie.databinding.FragmentAddToContributionBinding;


public class addToContribution extends DialogFragment implements addDateDialog.NotifyOnPickDate{
    private FragmentAddToContributionBinding binding;
    private GoalsListViewModel mViewModel;
    private ContributionsToItem listOfContributions;
    private int pos;
    private addDateDialog.NotifyOnPickDate notifier;


    public addToContribution() {
        // Required empty public constructor
    }

    public addToContribution(ContributionsToItem listItem, int position){
        listOfContributions = listItem;
        pos = position;
        notifier = this;
    }


    public static addToContribution newInstance() {
        addToContribution fragment = new addToContribution();

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        binding = FragmentAddToContributionBinding.inflate(requireActivity().getLayoutInflater());
        myDialog.setView(binding.getRoot());

        final Dialog myDialogFr = myDialog.create();
        mViewModel = new ViewModelProvider(requireActivity()).get(GoalsListViewModel.class);


        //listOfContributions = mViewModel.getItemForEdit().daoItemContributions;
        if(getTag()!=null && getTag().equals("EDIT")){
            binding.textDate.setText(listOfContributions.addDate);
            binding.editTextNumberDecimal.setText(String.valueOf(listOfContributions.contribution));
            binding.buttonAdd.setText(R.string.edit);
            binding.textView11.setText(R.string.edit_contribution);
        }

        binding.textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDateDialog newDate = addDateDialog.newInstance(getActivity(), notifier);
                newDate.show(getChildFragmentManager(), "Pick date");
            }
        });


        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PREROBIT CELU FUNKCIU - rozdelit na mensie funkcie a prerobit fungovanie
                String datum=binding.textDate.getText().toString();
                String saved = binding.editTextNumberDecimal.getText().toString();


                ContributionsController cc = new ContributionsController(getActivity(), mViewModel, mViewModel.getItemForEdit());
                ContributionsToItem cTi = cc.parseContribution(saved, datum);


                if(cTi!=null){
                if (getTag() != null && getTag().equals("EDIT") && listOfContributions!=null) {
                    cc.changeOnUpdateContribution(listOfContributions, cTi);
                    mViewModel.getDatabaseAccess().updateItemContributions(cTi);
                } else {
                    cc.changeOnAddContribution(cTi);
                    mViewModel.getDatabaseAccess().inputContributions(cTi);
                }


                myDialogFr.dismiss();
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogFr.cancel();
            }
        });

        return myDialogFr;
    }


    @Override
    public void onPositiveClick(String dateString) {
        binding.textDate.setText(dateString);
    }

    @Override
    public void onNegativeClick() {
        binding.textDate.setText(R.string.add_date);
    }
}
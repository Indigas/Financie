package sk.durovic.financie.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.R;
import sk.durovic.financie.controller.Item;
import sk.durovic.financie.controller.ItemController;
import sk.durovic.financie.data.ItemContributions;
import sk.durovic.financie.databinding.FragmentAddItemBinding;


public class AddItemFragment extends DialogFragment implements addDateDialog.NotifyOnPickDate, AdapterView.OnItemSelectedListener {
    private FragmentAddItemBinding binding;
    private GoalsListViewModel mViewModel;
    private AddItemFragment notifier;
    private SavingFrequency spinner;




    public AddItemFragment() {}

    public static AddItemFragment newInstance() {
        AddItemFragment fragment = new AddItemFragment();
        fragment.notifier = fragment;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder newItemDialog = new AlertDialog.Builder(getActivity());
        binding = FragmentAddItemBinding.inflate(requireActivity().getLayoutInflater());
        final View mView = binding.getRoot();

        mViewModel = new ViewModelProvider(requireActivity()).get(GoalsListViewModel.class);

        newItemDialog.setView(mView);
        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_frequency, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addSavingFrequency.setAdapter(mAdapter);
        binding.addSavingFrequency.setOnItemSelectedListener(notifier);

        final Dialog myDialog = newItemDialog.create();

        if(getTag()!=null) {
            if (getTag().equals("EDIT")) {
                // mozno nahradit setFragmentResult
                Item item = mViewModel.getItemForEdit();
                binding.textDialogName.setText(R.string.edit_dialog);
                binding.addDialog.setText(R.string.edit);
                binding.addName.setText(item.getName());
                binding.addDescription.setText(item.getInfo());
                binding.addGoal.setText(String.valueOf(item.getOverall()));
                binding.addSaved.setText(String.valueOf(item.getInBank()));
                if(item.getEndDate()!= null && !item.getEndDate().equals(""))
                    binding.addDate.setText(String.valueOf(item.getEndDate()));
                else
                    binding.addDate.setText(R.string.add_date);

                binding.addSavingFrequency.setSelection(item.getSavingFrequency());


            }
        }

        binding.addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDateDialog pickDate = addDateDialog.newInstance(getActivity(), notifier);
                pickDate.show(getChildFragmentManager(), "Pick Date");
            }
        });



        binding.addDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> newItem = new ArrayList<>();
                newItem.add(binding.addName.getText().toString());   // name 0
                newItem.add(binding.addDescription.getText().toString());  // description 1
                newItem.add(binding.addGoal.getText().toString());  // goal 2
                newItem.add(binding.addSaved.getText().toString());  // saved 3
                newItem.add(binding.addDate.getText().toString()); // date 4
                newItem.add(String.valueOf(binding.addSavingFrequency.getSelectedItemPosition()));   // spinner


                ItemController iC = new ItemController(getActivity());
                Item dialogItem = iC.parseItem(newItem);


                if(dialogItem!=null) {
                    if (getTag() != null && getTag().equals("EDIT")) {   // Pri edit - update item
                        dialogItem.ItemId = mViewModel.getItemForEdit().ItemId;
                        mViewModel.getDatabaseAccess().updateItem(dialogItem);
                        mViewModel.setCurrentItem(dialogItem);
                    } else //ok
                        mViewModel.getDatabaseAccess().inputItem(dialogItem);


                    myDialog.dismiss();
                }
            }
        });

        binding.cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });


        return myDialog;
    }

    @Override
    public void onPositiveClick(String stringDate) {
        binding.addDate.setText(stringDate);
    }

    @Override
    public void onNegativeClick() {
        binding.addDate.setText(R.string.add_date);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }
}

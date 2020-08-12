package sk.durovic.financie.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.controller.Item;
import sk.durovic.financie.databinding.FragmentMainBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class OverallPage extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private GoalsListViewModel pageViewModel;
    private FragmentMainBinding binding;


    public static OverallPage newInstance(int index) {
        OverallPage fragment = new OverallPage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pageViewModel = new ViewModelProvider(requireActivity()).get(GoalsListViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);
        //View root = inflater.inflate(R.layout.fragment_main, container, false);

        pageViewModel = new ViewModelProvider(requireActivity()).get(GoalsListViewModel.class);

        pageViewModel.getListOfItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                double overall = 0;
                double inBank = 0;
                for(Item i : items) {
                    overall += i.getOverall();
                    inBank += i.getInBank();
                }

                double prog = inBank / overall;
                double percent = Math.round(prog*10000)/100.00;

                binding.progressBar.setProgress((int)(prog*100));
                binding.textView7.setText(inBank + " / " + overall + " (" + percent + "%)");
            }
        });


        return binding.getRoot();
    }


}
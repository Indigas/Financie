package sk.durovic.financie.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.durovic.financie.R;
import sk.durovic.financie.databinding.FragmentMyGoalsPageBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyGoalsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGoalsPage extends Fragment {
    private FragmentMyGoalsPageBinding binding;

    public MyGoalsPage() {
        // Required empty public constructor
    }

    public static MyGoalsPage newInstance() {
        MyGoalsPage fragment = new MyGoalsPage();
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
        binding = FragmentMyGoalsPageBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }
}

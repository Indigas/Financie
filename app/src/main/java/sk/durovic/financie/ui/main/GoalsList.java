package sk.durovic.financie.ui.main;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.MainActivity;
import sk.durovic.financie.R;
import sk.durovic.financie.controller.Item;
import sk.durovic.financie.data.ContributionsToItem;
import sk.durovic.financie.data.DatabaseAccess;
import sk.durovic.financie.data.ItemContributions;
import sk.durovic.financie.databinding.GoalsListFragmentBinding;

public class GoalsList extends Fragment {

    private GoalsListViewModel mViewModel;
    private GoalsListFragmentBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterToRecyclerView mAdapter;
    private List<Item> listOfItems = new LinkedList<>();
    private int onDelete = -1;



    public static GoalsList newInstance() {
        return new GoalsList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = GoalsListFragmentBinding.inflate(getLayoutInflater(), container, false);
        View root = binding.getRoot();

        mViewModel = new ViewModelProvider(requireActivity()).get(GoalsListViewModel.class);

        createRecyclerView();

        final SwipeController swipeController = new SwipeController(new SwipeControllerAction() {
            @Override
            public void onLeftClicked(int position) {
                mViewModel.setCurrentItem(mViewModel.getListOfAll().get(position).daoItem);
                DialogFragment myDialog = AddItemFragment.newInstance();
                myDialog.show(requireActivity().getSupportFragmentManager(), "EDIT");
            }

            @Override
            public void onRightClicked(int position) {
                onDelete=position;
                onDeteleItem(position);
                //listOfItems.remove(position);



               // Log.i("GoalList", "onRightClicked");
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(binding.listOfGoals);

        binding.listOfGoals.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });



        ((MainActivity)requireActivity()).hideEditButton();



        binding.listOfGoals.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && binding.floatingActionButton.getVisibility() == View.VISIBLE)
                    binding.floatingActionButton.hide();
                else if (dy < 0 && binding.floatingActionButton.getVisibility() != View.VISIBLE)
                    binding.floatingActionButton.show();
            }
        });


        mAdapter.setOnItemClickListener(new AdapterToRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mViewModel.setCurrentItem(mViewModel.getListOfAll().get(position).daoItem);
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().replace(R.id.fragment_my_goals_page, GoalDetail.newInstance());
                ft.addToBackStack(null);
                ft.commit();

            }
        }, mViewModel);

        binding.floatingActionButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newItem = AddItemFragment.newInstance();
                newItem.show(getChildFragmentManager(), "ADD");
            }
        });

        mViewModel.getDatabaseAccess().getAll().observe(getViewLifecycleOwner(), new Observer<List<ItemContributions>>() {
            @Override
            public void onChanged(List<ItemContributions> itemContributions) {
                listOfItems.clear();
                for(ItemContributions c : itemContributions){
                    listOfItems.add(c.daoItem);
                }
                mViewModel.setListOfItems(listOfItems);
                mViewModel.setListOfAll(itemContributions);

                if(onDelete==-1)
                    mAdapter.notifyDataSetChanged();
                else {
                    mAdapter.notifyItemRemoved(onDelete);
                    mAdapter.notifyItemRangeChanged(onDelete, mAdapter.getItemCount());
                    onDelete=-1;
                }
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void createRecyclerView(){
        binding.listOfGoals.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        binding.listOfGoals.setLayoutManager(layoutManager);

        mAdapter = new AdapterToRecyclerView(listOfItems);
        binding.listOfGoals.setAdapter(mAdapter);
    }

    void onDeteleItem(int position){
        mViewModel.getDatabaseAccess().deleteItem(mViewModel.getListOfAll().get(position).daoItem);
        for(ContributionsToItem cTi : mViewModel.getListOfAll().get(position).daoItemContributions)
            mViewModel.getDatabaseAccess().deleteContributions(cTi);
    }


}

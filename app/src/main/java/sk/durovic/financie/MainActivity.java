package sk.durovic.financie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import sk.durovic.financie.controller.Item;
import sk.durovic.financie.data.AppDatabase;
import sk.durovic.financie.data.DatabaseAccess;
import sk.durovic.financie.databinding.ActivityMainBinding;
import sk.durovic.financie.ui.main.AddItemFragment;
import sk.durovic.financie.ui.main.GoalsList;
import sk.durovic.financie.ui.main.MyGoalsPage;
import sk.durovic.financie.ui.main.SectionsPagerAdapter;
import sk.durovic.financie.ui.main.SwipeController;

public class MainActivity extends AppCompatActivity {
    private ViewPager vp;
    private ActivityMainBinding binding;
    private GoalsListViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        binding.viewPager.setAdapter(sectionsPagerAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager);


        viewModel = new ViewModelProvider(this).get(GoalsListViewModel.class);


        viewModel.setDatabaseAccess(new DatabaseAccess(this));



        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment myDialog = AddItemFragment.newInstance();
                myDialog.show(getSupportFragmentManager(), "EDIT");

            }
        });

        binding.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Settings Button", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof MyGoalsPage)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_my_goals_page, GoalsList.newInstance()).commit();

    }



    @Override
    public void onBackPressed() {
        if(binding.viewPager.getCurrentItem()==0)
            finish();


        super.onBackPressed();
    }

    public void hideEditButton(){
        if(binding.editButton.getVisibility()!=View.GONE)
        binding.editButton.setVisibility(View.GONE);
    }

    public void showEditButton(){
        if(binding.editButton.getVisibility()!=View.VISIBLE)
        binding.editButton.setVisibility(View.VISIBLE);
    }
}
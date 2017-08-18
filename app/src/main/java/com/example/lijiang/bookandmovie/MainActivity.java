package com.example.lijiang.bookandmovie;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;

        import com.example.lijiang.bookandmovie.Fragment.MovieFragment;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> spinnerList = new ArrayList<>();
    private Spinner spinner;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private MovieFragment mMovieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerList.add("图书");
        spinnerList.add("电影");
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item,spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mMovieFragment = new MovieFragment();
        mFragmentTransaction.replace(R.id.framlayout,mMovieFragment);
        mFragmentTransaction.commit();

    }
}

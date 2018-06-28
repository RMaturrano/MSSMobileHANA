package com.proyecto.notacredito;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.proyect.movil.R;
import com.proyecto.notacredito.adapter.tablayout.TBAdapterDetalleNotaCredito;

public class NotaCreditoDetalleActivity extends AppCompatActivity {

    public static String KEY_PARAM_FACT = "mFact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_credito_detalle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbDetalleNotaCredito);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detalle nota credito");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLytDetalleNotaCredito);
        tabLayout.addTab(tabLayout.newTab().setText("General"));
        tabLayout.addTab(tabLayout.newTab().setText("Contenido"));
        tabLayout.addTab(tabLayout.newTab().setText("Logistica"));
        tabLayout.addTab(tabLayout.newTab().setText("Finanzas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.vpDetalleNotaCredito);
        TBAdapterDetalleNotaCredito adapter = new TBAdapterDetalleNotaCredito(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.proyecto.inventario;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.proyect.movil.R;
import com.proyecto.utils.PagerAdapterDetArticulo;

public class DetalleArticuloMain extends AppCompatActivity{

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalle_articulo);
		
		//TOOLBAR
		Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
	    setSupportActionBar(myToolbar);
	    
	    // Get a support ActionBar corresponding to this toolbar
	    ActionBar ab = getSupportActionBar();

	    // Enable the Up button
	    ab.setDisplayHomeAsUpEnabled(true);
	  //TOOLBAR
	    
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("General"));
        tabLayout.addTab(tabLayout.newTab().setText("Cantidades"));
        tabLayout.addTab(tabLayout.newTab().setText("Precios"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
 
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapterDetArticulo adapter = new PagerAdapterDetArticulo
                (getSupportFragmentManager(), tabLayout.getTabCount());
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
		
		switch (item.getItemId()) {
        	case 16908332:
        		
            	finish();
        		
        		return true;
            default:
                return super.onOptionsItemSelected(item);
		}
		
	}
	
	
	
}

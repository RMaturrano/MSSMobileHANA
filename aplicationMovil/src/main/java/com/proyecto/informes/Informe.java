package com.proyecto.informes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.proyect.movil.R;

public class Informe extends AppCompatActivity{

	ListView lstMenu;
	Context contexto;
	
	//
	PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    LayoutParams params;
    LinearLayout mainLayout;
    Button but;
    boolean click = true;
    //
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informes_menu);
		
		contexto = this;
		
		lstMenu = (ListView) findViewById(R.id.listView1);
		
				ArrayAdapter<String> adap= new ArrayAdapter<String>(this, 
		        		android.R.layout.simple_list_item_1,
		        		new String[]{"Informe Estado ciente","Informe estado ordenes","Informe estado de registro de cobranza","Guia de rutas"});
		        
		lstMenu.setAdapter(adap);
		lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	
            	if(position == 0){
                	
            		popUp = new PopupWindow(contexto);
                    layout = new LinearLayout(contexto);
                    mainLayout = new LinearLayout(contexto);
                    tv = new TextView(contexto);
                    but = new Button(contexto);
                    but.setText("Click Me");
                    but.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            if (click) {
                                popUp.showAtLocation(mainLayout, Gravity.BOTTOM, 10, 10);
                                popUp.update(50, 50, 300, 80);
                                click = false;
                            } else {
                                popUp.dismiss();
                                click = true;
                            }
                        }

                    });
                    params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    tv.setText("Hi this is a sample text for popup window");
                    layout.addView(tv, params);
                    popUp.setContentView(layout);
                    // popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
                    mainLayout.addView(but, params);
                    setContentView(mainLayout);
                	
                }
            	
            }
		
    	});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
	}
	
}

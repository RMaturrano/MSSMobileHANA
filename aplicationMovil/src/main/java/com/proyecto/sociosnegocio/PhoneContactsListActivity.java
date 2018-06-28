package com.proyecto.sociosnegocio;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.proyect.movil.BuildConfig;
import com.proyect.movil.R;
import com.proyecto.utils.Utils;

public class PhoneContactsListActivity extends AppCompatActivity implements
				PhoneContactsListFragment.OnContactsInteractionListener {
	
	Context contexto;
	String nameG = "";
	public static int REQUEST_CONTACT_TLF = 71;
	public static String KEY_PARAM_NAME = "kpName";
	public static String KEY_PARAM_PHONE = "kpPhone";

   // private ContactDetailFragment mContactDetailFragment;

    // If true, this is a larger screen device which fits two panes
    private boolean isTwoPaneLayout;

    // True if this activity instance is a search result view (used on pre-HC devices that load
    // search results in a separate instance of the activity rather than loading results in-line
    // as the query is typed.
    private boolean isSearchResultView = false;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
        
        super.onCreate(savedInstanceState);

        // Set main content view. On smaller screen devices this is a single pane view with one
        // fragment. One larger screen devices this is a two pane view with two fragments.
        setContentView(R.layout.business_partner_list_main);
        
      //TOOLBAR
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Check if two pane bool is set based on resource directories
        isTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);

        // Check if this activity instance has been triggered as a result of a search query. This
        // will only happen on pre-HC OS versions as from HC onward search is carried out using
        // an ActionBar SearchView which carries out the search in-line without loading a new
        // Activity.
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {

            // Fetch query from intent and notify the fragment that it should display search
            // results instead of all contacts.
            String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
            PhoneContactsListFragment mContactsListFragment = (PhoneContactsListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.contact_list);

            // This flag notes that the Activity is doing a search, and so the result will be
            // search results rather than all contacts. This prevents the Activity and Fragment
            // from trying to a search on search results.
            isSearchResultView = true;
            mContactsListFragment.setSearchQuery(searchQuery);

            // Set special title for search results
            String title = getString(R.string.contacts_list_search_results_title, searchQuery);
            setTitle(title);
        }

        
        contexto = this;
        
        if (isTwoPaneLayout) {
            // If two pane layout, locate the contact detail fragment
          //  mContactDetailFragment = (ContactDetailFragment)
            //        getSupportFragmentManager().findFragmentById(R.id.contact_detail);
        }
    }
    
    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.business_partner_list_menu, menu);
        return true;
	}
    
    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact is no longer selected.
     */
    @Override
    public void onSelectionCleared() {
      //  if (isTwoPaneLayout && mContactDetailFragment != null) {
      //      mContactDetailFragment.setContact(null);
      //  }
    }

    @Override
    public boolean onSearchRequested() {
        // Don't allow another search if this activity instance is already showing
        // search results. Only used pre-HC.
        return !isSearchResultView && super.onSearchRequested();
    }



	@Override
	public void onContactSelected(Uri contactUri) {
		// TODO Auto-generated method stub
		
		
		String id = "", name = "", phone = "", hasPhone = "null";
		int idx;
		Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
		if (cursor.moveToFirst()) {
		    idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		    id = cursor.getString(idx);

		    idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		    name = cursor.getString(idx);

		    idx = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		    hasPhone = cursor.getString(idx);
		}
		cursor.close();
		
		int colIdx;
		if ("1".equalsIgnoreCase(hasPhone)) {
		    cursor = getContentResolver().query(
		            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		            null,
		            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+ id, 
		            null, null);
		    if (cursor.moveToFirst()) {
		        colIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		        phone = cursor.getString(colIdx);
		    }
		    cursor.close();
		}
		
		nameG = name; 
		
		//Preparar argumentos de envio de mensaje
	/*	Bundle arguments = new Bundle();
		arguments.putString("name", name);
		arguments.putString("phone", phone);
		
		//MANDAR LOS PARÀMETROS EN LOCALBORADCAST INTENT
   	 	Intent localBroadcastIntent = new Intent("event-get-contact-from-directory");
        localBroadcastIntent.putExtras(arguments);
        LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(contexto);
        myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);	*/

        Intent intent = new Intent();
        intent.putExtra(KEY_PARAM_NAME, name);
        intent.putExtra(KEY_PARAM_PHONE, phone);
        setResult(RESULT_OK, intent);
		finish();
	}
}

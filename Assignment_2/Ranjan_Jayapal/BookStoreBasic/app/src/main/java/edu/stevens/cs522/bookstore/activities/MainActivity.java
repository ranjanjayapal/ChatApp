package edu.stevens.cs522.bookstore.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.util.BooksAdapter;

public class MainActivity extends AppCompatActivity {
	
	// Use this when logging errors and warnings.
	private static final String TAG = MainActivity.class.getCanonicalName();
    static final private String cart_saved = "list";
	// These are request codes for subactivity request calls
	static final private int ADD_REQUEST = 1;
    private static final String BOOK_DETAILS = "Details_of _the_book";
	static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;

	// There is a reason this must be an ArrayList instead of a List.
	private ArrayList<Book> shoppingCart = new ArrayList<Book>();
    private BooksAdapter booksAdapter;
    private ListView lv;
    private TextView empty;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Set the layout (use cart.xml layout)
        setContentView(R.layout.cart);

        // TODO check if there is saved UI state, and if so, restore it (i.e. the cart contents)
        if(savedInstanceState != null){
            shoppingCart = savedInstanceState.getParcelableArrayList("list");
        }
		// TODO use an array adapter to display the cart contents.
        booksAdapter = new BooksAdapter(this, shoppingCart);
        empty = (TextView) findViewById(android.R.id.empty);
        lv = (ListView) findViewById(android.R.id.list);
        lv.setAdapter(booksAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewBookActivity.class);
                intent.putExtra(BOOK_DETAILS, shoppingCart.get(position));
                startActivity(intent);
            }
        });


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO inflate a menu with ADD and CHECKOUT options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookstore_menu, menu);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
        switch(item.getItemId()) {

            // TODO ADD provide the UI for adding a book
            case R.id.add:
                 Intent addIntent = new Intent(this, AddBookActivity.class);
                 startActivityForResult(addIntent, ADD_REQUEST);
                break;

            // TODO CHECKOUT provide the UI for checking out
            case R.id.checkout:
                if(shoppingCart.size() < 0){
                    Toast.makeText(this, "Cart is Already Empty", Toast.LENGTH_LONG).show();
                }else{
                    addIntent = new Intent(this, CheckoutActivity.class);
                    startActivityForResult(addIntent, CHECKOUT_REQUEST);
                }
                break;

            default:
        }
        return false;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// TODO Handle results from the Search and Checkout activities.

        // Use ADD_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
        switch(requestCode) {
            case ADD_REQUEST:
                // ADD: add the book that is returned to the shopping cart.
                if(resultCode == RESULT_OK){
                    Book b = intent.getParcelableExtra("newbook");
                    Toast.makeText(this, "At the BookStoreActivity", Toast.LENGTH_LONG).show();
                    shoppingCart.add(b);
                    booksAdapter.notifyDataSetChanged();
                    empty.setVisibility(View.GONE);
                }else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(this,"AddBook Cancelled",Toast.LENGTH_LONG).show();
                }
                break;
            case CHECKOUT_REQUEST:
                // CHECKOUT: empty the shopping cart.
                if(resultCode == RESULT_OK){
                    Toast.makeText(this, "Shopping cart checked out!!", Toast.LENGTH_SHORT).show();
                    shoppingCart.clear();
                    booksAdapter.notifyDataSetChanged();
                    empty.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this, "Cancelled Checkout!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO save the shopping cart contents (which should be a list of parcelables).
        savedInstanceState.putParcelableArrayList(cart_saved, shoppingCart);
	}
	
}
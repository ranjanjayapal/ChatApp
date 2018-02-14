package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

public class ViewBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	private static final String BOOK_DETAILS = "Details_of _the_book";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_book);
//		TextView id = (TextView) findViewById(R.id.view_id);
		TextView price = (TextView) findViewById(R.id.view_price);
		TextView title = (TextView) findViewById(R.id.view_title);
		TextView author = (TextView) findViewById(R.id.view_author);
		TextView isbn = (TextView) findViewById(R.id.view_isbn);
		// TODO get book as parcelable intent extra and populate the UI with book details.
		Book book = getIntent().getExtras().getParcelable(BOOK_DETAILS);
//		id.setText(book.getBookId());
		title.setText(book.title);
		Author authors[] = book.authors;
		String authorstr = "";
		for(Author a : authors){
			if(a.middleInitial != null){
				authorstr += a.firstName+" "+a.middleInitial+" "+a.lastName;
			}else{
				authorstr += a.firstName+" "+a.lastName;
			}
			authorstr += "\n";
		}
		author.setText(authorstr);
		isbn.setText(book.isbn);
		price.setText(book.price);
	}

}
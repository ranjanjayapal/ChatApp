package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

public class AddBookActivity extends AppCompatActivity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_RESULT_KEY = "book_result";
	EditText title, author, isbn;
	int id1 = 1;
	int check = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book);
		title = (EditText) findViewById(R.id.search_title);
		author = (EditText) findViewById(R.id.search_author);
		isbn = (EditText) findViewById(R.id.search_isbn);
		addBook();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO provide ADD and CANCEL options
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.addbook_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
		switch(item.getItemId()) {
			// ADD: return the book details to the BookStore activity
			case R.id.add_now:
				Book book = addBook();
				if(check == 0) {
					Intent intent = new Intent();
					intent.putExtra("newbook", book);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
				break;

			// CANCEL: cancel the request
			case R.id.cancel:
				finish();
				break;

			default:
		}
		return false;
	}
	
	public Book addBook(){
		// TODO Just build a Book object with the search criteria and return that.
		check = 0;
		String search_title = title.getText().toString();
		String search_author = author.getText().toString();
		String search_isbn = isbn.getText().toString();
		Book book = new Book();
		book.title = search_title;
		book.id = id1++;
		String [] str = search_author.split(",");
		Author[] authors = new Author[str.length];
		int index = 0;
		for(String s : str){
			Author a = null;
			String[] fml = s.split("\\s+");
			if(fml.length == 2){
				a = new Author(fml[0], fml[1]);
			}else if(fml.length == 3){
				a = new Author(fml[0], fml[1], fml[2]);
			}else{
				Toast.makeText(this, "Invalid Author Name. Please enter a last name", Toast.LENGTH_LONG).show();
				check = 1;
			}
			authors[index] = a;
			index++;
		}
		book.authors = authors;
		book.isbn = search_isbn;
		book.price = "$100";
		return book;
	}

}
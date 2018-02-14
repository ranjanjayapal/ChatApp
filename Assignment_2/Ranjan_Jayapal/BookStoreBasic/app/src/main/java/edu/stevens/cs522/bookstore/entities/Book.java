package edu.stevens.cs522.bookstore.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{

	public Book(){

	}
	public int id;

	public String title;

	public Author[] authors;

	public String isbn;

	public String price;

	public Book(int id, String title, Author[] author, String isbn, String price) {
		this.id = id;
		this.title = title;
		this.authors = author;
		this.isbn = isbn;
		this.price = price;
	}

	// TODO Modify this to implement the Parcelable interface.
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(id);
		parcel.writeString(title);
		parcel.writeTypedArray(authors,i);
		parcel.writeString(isbn);
		parcel.writeString(price);
	}
	public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
		public Book createFromParcel(Parcel in) {
			return new Book(in);
		}

		public Book[] newArray(int size) {
			return new Book[size];
		}
	};
	public Book(Parcel in){
		id = in.readInt();
		title = in.readString();
		authors = in.createTypedArray(Author.CREATOR);
		//authors = in.readParcelable(Author.class.getClassLoader());
		isbn = in.readString();
		price = in.readString();
	}



//	public String getFirstAuthor() {
//		if (authors != null && authors.length > 0) {
//			return authors[0].toString();
//		} else {
//			return "";
//		}
//	}
}
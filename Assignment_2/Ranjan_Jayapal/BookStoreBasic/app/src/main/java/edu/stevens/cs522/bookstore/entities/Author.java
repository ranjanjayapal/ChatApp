package edu.stevens.cs522.bookstore.entities;
import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable{
	//If the authors name is entered in Full
	public Author(){

	}
	public Author(String firstName, String middleInitial, String lastName) {
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
	}
	//If the authors name is entered with only first and last name
	public Author(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public Author(String firstName) {
		this.firstName = firstName;
	}
	// TODO Modify this to implement the Parcelable interface.
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(firstName);
		parcel.writeString(middleInitial);
		parcel.writeString(lastName);
	}
	public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
		public Author createFromParcel(Parcel in) {
			return new Author(in);
		}

		public Author[] newArray(int size) {
			return new Author[size];
		}
	};
	public Author(Parcel in){
		firstName = in.readString();
		middleInitial = in.readString();
		lastName = in.readString();
	}
	// NOTE: middleInitial may be NULL!

	public String firstName;

	public String middleInitial;

	public String lastName;

}

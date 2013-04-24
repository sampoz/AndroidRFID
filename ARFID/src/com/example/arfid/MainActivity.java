package com.example.arfid;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
	DatabaseHandler db = new DatabaseHandler(this);

	EditText name;
	EditText cardidtext;

	int readState = 1;

	private String tempId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// db.fix(); //use this if you have managed to drop the table without
		// creating a new one
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// we need to set the text field to non focusable in order to read the
		// id from the card
		name = (EditText) findViewById(R.id.name);
		name.setFocusable(false);
		cardidtext = (EditText) findViewById(R.id.cardidtext);
		cardidtext.setFocusable(false);
		Button drop = (Button) findViewById(R.id.drop);
		drop.setOnClickListener(buttonAddOnClickListener);

		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(buttonAddOnClickListener);

		DatabaseHandler db = new DatabaseHandler(this);

		/**
		 * CRUD Operations
		 * */

		// Reading all cards
		Log.d("Reading: ", "Reading all contacts..");
		List<IDCard> cards = db.getAllIDCards();

		for (IDCard ca : cards) {
			String log = "Id: " + ca.getId() + " ,CardID: " + ca.getCardid()
					+ " ,Userid: " + ca.getUserid();
			// Writing Cards to log
			Log.d("Card: ", log);
		}

	}

	Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Switch statement to check which button was pressed
			switch (arg0.getId()) {
			case R.id.drop:
				db.dropDB();
			case R.id.add:
				addNew();

			}
		}

		private void addNew() {
	
			// Adding new card with running id
			db.addIDCard(new IDCard(db.getIDCardsCount() + 1, cardidtext
					.getText().toString(), name.getText().toString()));
			Log.d("added a card", cardidtext.getText().toString());

			readState = 1;
			name.setFocusable(false);

		}
	};

	// catches "keypresses" from rfid, can become unsynchronized, because
	// android cant catch enter :<
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (readState == 1) {
			tempId += event.getDisplayLabel();
			IDCard card = null;
			System.out.println("keypress");

			if (tempId.length() > 9) {// length of the card id
				
				Log.d("cardid was ", tempId);
				card = db.getIDCardByCardID(tempId);
				if (card == null) {
					readState=0;
					name.setFocusable(true);
					name.setText("Enter Your name here, the card is not mapped");
					cardidtext.setText(tempId);

					
					tempId = "";
					return super.onKeyDown(keyCode, event);
				}
				Log.d("card exists wit cardid", tempId);
				tempId = "";

				name.setText(card.getUserid());
				cardidtext.setText(card.getCardid());
			}
		}

		return super.onKeyDown(keyCode, event);
	}

}

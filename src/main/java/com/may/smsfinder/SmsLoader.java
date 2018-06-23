package com.may.smsfinder;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;


import com.may.smsfinder.data.DataHelper;
import com.may.smsfinder.model.Section;
import com.may.smsfinder.model.Sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SmsLoader {
	private ContentResolver contentResolver;
	private SharedPreferences sharedPreferences;

	public SmsLoader(ContentResolver contentResolver, SharedPreferences sharedPreferences) {
		this.contentResolver = contentResolver;
		this.sharedPreferences = sharedPreferences;
	}

	public void refreshSms() {
		DataHelper dataHelper = DataHelper.getInstance();

		List<Sms> smsList = loadSmsList();

//		List<Sms> mmsList = loadMmsList();
//		smsList.addAll(mmsList);

		dataHelper.deleteAllSms();
		List<Section> sectionList = dataHelper.selectSections();

		for (Section section : sectionList) {
			for (Sms sms : smsList) {
				if (sms.getBody().contains(section.getPattern())) {
					sms.setSectionName(section.getName());

					dataHelper.insertSms(sms);
				}
			}
		}
	}

	private List<Sms> loadSmsList() {
		Cursor c = contentResolver.query(Uri.parse("content://sms"), null,
				null, null, Telephony.Sms.DEFAULT_SORT_ORDER);

		if (c == null) {
			return Collections.emptyList();
		}

		List<Sms> smsList = new ArrayList<>();
		while (c.moveToNext()) {
			int type = Integer.parseInt(c.getString(c.getColumnIndex(Telephony.Sms.TYPE)));

			Log.i("GDAY", type + " : " + c.getString(c.getColumnIndex(Telephony.Sms.BODY)));

			if (type != Telephony.Sms.MESSAGE_TYPE_INBOX) {
//				break; // save only receive sms
			}

			Sms sms = new Sms();
			sms.setId(c.getLong(c.getColumnIndex(Telephony.Sms._ID)));
			sms.setThreadId(c.getLong(c.getColumnIndex(Telephony.Sms.THREAD_ID)));
			sms.setAddress(c.getString(c.getColumnIndex(Telephony.Sms.ADDRESS)));
			sms.setPerson(c.getString(c.getColumnIndex(Telephony.Sms.PERSON)));
			sms.setDate(c.getLong(c.getColumnIndex(Telephony.Sms.DATE)));
			sms.setBody(c.getString(c.getColumnIndex(Telephony.Sms.BODY)));

			Log.d("GDAY", "type: " + type + " : " + sms.toString());

			smsList.add(sms);
		}

		return smsList;
	}

	private List<Sms> loadMmsList() {
		Cursor c = contentResolver.query(Telephony.Mms.CONTENT_URI, null,
				null, null, Telephony.Sms.DEFAULT_SORT_ORDER);

		if (c == null) {
			return Collections.emptyList();
		}

		List<Sms> smsList = new ArrayList<>();
		while (c.moveToNext()) {
			int type = Integer.parseInt(c.getString(c.getColumnIndex(Telephony.Sms.TYPE)));

			Log.i("GDAY", type + " : " + c.getString(c.getColumnIndex(Telephony.Sms.BODY)));

			if (type != Telephony.Sms.MESSAGE_TYPE_INBOX) {
				break; // save only receive sms
			}

			Sms sms = new Sms();
			sms.setId(c.getLong(c.getColumnIndex(Telephony.Sms._ID)));
			sms.setThreadId(c.getLong(c.getColumnIndex(Telephony.Sms.THREAD_ID)));
			sms.setAddress(c.getString(c.getColumnIndex(Telephony.Sms.ADDRESS)));
			sms.setPerson(c.getString(c.getColumnIndex(Telephony.Sms.PERSON)));
			sms.setDate(c.getLong(c.getColumnIndex(Telephony.Sms.DATE)));
			sms.setBody(c.getString(c.getColumnIndex(Telephony.Sms.BODY)));

			Log.d("GDAY", "type: " + type + " : " + sms.toString());

			smsList.add(sms);
		}

		return smsList;
	}

}

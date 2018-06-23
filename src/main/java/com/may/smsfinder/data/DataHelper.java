package com.may.smsfinder.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.may.smsfinder.model.Section;
import com.may.smsfinder.model.Sms;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class DataHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "smsme.db";
	private static final int DATABASE_VERSION = 3;

	private static DataHelper DATA_HELPER;

	public static DataHelper getInstance() {
		return DATA_HELPER;
	}

	public static void setInstance(DataHelper dataHelper) {
		DATA_HELPER = dataHelper;
	}

    private InputStream schemaIs;

	public DataHelper(Context context, InputStream is) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
		schemaIs = is;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder schemaSql = new StringBuilder();
		
		try {
            BufferedInputStream bis = new BufferedInputStream((schemaIs));
			
			byte[] buffer = new byte[1024];
            int read;
			while ((read = bis.read(buffer)) != -1) {
                if (read == buffer.length) {
                    schemaSql.append(new String(buffer));
                } else {
                    byte[] tail = new byte[read];
                    System.arraycopy(buffer, 0, tail, 0, read);
                    schemaSql.append(new String(tail));
                }
			}

            Log.d("GDAY", "schema ==> " + schemaSql.toString());
		} catch (FileNotFoundException e) {
            Log.e("GDAY", e.getMessage());
		} catch (IOException e) {
            Log.e("GDAY", e.getMessage());
		}


		String sqlList = schemaSql.toString();
		String[] sqlArray = sqlList.split(";");

		for (String sql : sqlArray) {
			db.execSQL(sql.trim());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public List<Section> selectSections() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM section", null);

		List<Section> sections = new ArrayList<>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(0);
			String pattern = cursor.getString(1);

			Section section = new Section();
			section.setName(name);
			section.setPattern(pattern);

			sections.add(section);
		}

		cursor.close();
		
		return sections;
	}

	public void insertSection(Section section) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", section.getName());
		contentValues.put("pattern", section.getPattern());

		db.insert("section", null, contentValues);
	}

	public void deleteAllSms() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("sms", null, null);
	}

	public void insertSms(Sms sms) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("sectionName", sms.getSectionName());
		contentValues.put("body", sms.getBody());
		contentValues.put("address", sms.getAddress());
		contentValues.put("id", sms.getId());
		contentValues.put("date", sms.getDate());

		SQLiteDatabase db = this.getWritableDatabase();
		db.insert("sms", null, contentValues);
	}

	public List<Sms> selectSmsList(String sectionName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM sms WHERE sectionName = ?", new String[] {sectionName});

		if (cursor == null) {
			return Collections.emptyList();
		}

		List<Sms> smsList = new ArrayList<>();
		while (cursor.moveToNext()) {
			Sms sms = new Sms();
			String body = cursor.getString(cursor.getColumnIndex("body"));
			sms.setBody(body);
			String address = cursor.getString(cursor.getColumnIndex("address"));
			sms.setAddress(address);
			sms.setDate(cursor.getLong(cursor.getColumnIndex("date")));
			sms.setSectionName(cursor.getString(cursor.getColumnIndex("sectionName")));

			smsList.add(sms);
		}

		return smsList;
	}

	public void deleteSection(String section) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("section", "name = ?", new String[] {section});
	}

	public void deleteSms(String section) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("sms", "sectionName = ?", new String[] {section});
	}

	public Section selectSection(String sectionName) {
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM section WHERE name = ?", new String[] {sectionName});

		if (cursor.moveToNext()) {
			Section section = new Section();
			section.setName(cursor.getString(cursor.getColumnIndex("name")));
			section.setPattern(cursor.getString(cursor.getColumnIndex("pattern")));
			return section;
		} else {
			return null;
		}
	}

	public void updateSection(String sectionName, String newSectionName, String newPattern) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", newSectionName);
		contentValues.put("pattern", newPattern);

		sqLiteDatabase.update("section", contentValues, "name = ?", new String[] {sectionName});
	}
}

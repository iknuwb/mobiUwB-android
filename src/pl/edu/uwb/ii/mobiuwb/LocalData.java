package pl.edu.uwb.ii.mobiuwb;

import android.content.SharedPreferences;

public class LocalData {
	private SharedPreferences _daneLokalne = null;

	public LocalData(SharedPreferences _daneLokalne) {
		this._daneLokalne = _daneLokalne;
	}

	public String pobierzDanaLokalna(String nazwaZmiennej,
			String wartoscDomyslna) {
		return _daneLokalne.getString(nazwaZmiennej, wartoscDomyslna);
	}

	public void zapiszDanaLokalna(String nazwaZmiennej, String wartoscZmiennej) {
		SharedPreferences.Editor edytorDanychLokalnych = _daneLokalne.edit();
		edytorDanychLokalnych.putString(nazwaZmiennej, wartoscZmiennej);
		edytorDanychLokalnych.commit();
	}
}

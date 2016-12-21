package com.jq.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jq.R;
import com.jq.port.SerialPortFinder;
import com.jq.ui.express_form.ExpressFormMainActivity;

public class SerialPortPreferences extends Activity {
	private LinearLayout mMainLayout;
	private Button mScanButton;

	public static class PrefsFragement extends PreferenceFragment {

		private SerialPortFinder mSerialPortFinder;

		public PrefsFragement(SerialPortFinder finder) {
			mSerialPortFinder = finder;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.serial_port_preferences);

			// Devices
			final ListPreference devices = (ListPreference) findPreference("DEVICE");

			// String[] entryValues = mSerialPortFinder.getAllDevicesPath();

			devices.setSummary(devices.getValue());
			devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				public boolean onPreferenceChange(Preference preference,
						Object newValue) {
					preference.setSummary((String) newValue);
					return true;
				}
			});

			
		}

	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DemoApplication mApplication = (DemoApplication) getApplication();
		PrefsFragement pfg = new PrefsFragement(mApplication.mSerialPortFinder);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, pfg).commit();
	

	}

	
}

package codemonkeylabs.androidzipcodelib.sample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import codemonkeylabs.androidzipcodelib.library.ZipResult;
import codemonkeylabs.androidzipcodelib.library.ZipcodeLib;
import codemonkeylabs.androidzipcodelib.library.ZipcodeListener;

public class ZipcodeSample extends Activity {

    protected EditText zipCode = null;
    protected ViewGroup container = null;
    protected LayoutInflater layoutInflater = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        wireUi();
    }

    protected void wireUi()
    {
        this.zipCode = (EditText)findViewById(R.id.zipCode);
        this.container = (ViewGroup)findViewById(R.id.container);

        layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        this.zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 5)
                    getStateAndZip(editable.toString());
            }
        });

    }

    public void getStateAndZip(final String zipcodeStr)
    {
        new AsyncTask<Object, Void, ZipResult>()
        {
            @Override
            protected ZipResult doInBackground(Object... objects)
            {
                return ZipcodeLib.getCitiesAndStateThenDestroy(getApplicationContext(), zipcodeStr);
            }

            @Override
            protected void onPostExecute(ZipResult s)
            {
                super.onPostExecute(s);
                if(s!=null)
                {
                    container.removeAllViews();
                    for(String city : s.cities)
                    {
                        View newView = layoutInflater.inflate(R.layout.city_state_row,container,false);
                        ((TextView)newView.findViewById(R.id.cityState)).setText(city + ", " + s.state);
                        container.addView(newView,0);
                    }
                }
            }
        }.execute();
    }

    public void fetchAsync()
    {
        ZipcodeLib.getCitiesAndStateAsync(getApplicationContext(), zipCode.getText().toString(), new Zipcodelistener());
    }

    public class Zipcodelistener implements ZipcodeListener
    {
        @Override
        public void getCitiesAndStateResult(ZipResult zipResult)
        {
            if(zipResult == null)
            {
                Toast.makeText(ZipcodeSample.this, "Nothing Found", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ZipcodeSample.this, zipResult.state + ":" + zipResult.cities.get(0), Toast.LENGTH_LONG).show();
            }

        }
    }

}

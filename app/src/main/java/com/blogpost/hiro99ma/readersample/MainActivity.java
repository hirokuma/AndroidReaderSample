package com.blogpost.hiro99ma.readersample;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;

    private static final String TAG = "MainActivity";
    private TextView mTextView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView1 = (TextView)findViewById(R.id.textView1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter != null) {
            Log.d(TAG, "enable reader mode");
            mNfcAdapter.enableReaderMode(this, new MyReaderCallback(),
                    NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B /*| NfcAdapter.FLAG_READER_NFC_F*/,
                    null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mNfcAdapter != null) {
            mNfcAdapter.disableReaderMode(this);
            Log.d(TAG, "disable reader mode");
        }
    }

    private class MyReaderCallback implements NfcAdapter.ReaderCallback {

        @Override
        public void onTagDiscovered(Tag tag) {
            Log.d(TAG, "onTagDiscovered");

            byte[] ids = tag.getId();
            CharSequence txt = mTextView1.getText();
            final StringBuilder sb = new StringBuilder(ids.length + 1 + txt.length());
            for (byte id : ids) {
                sb.append(String.format("%02x", id));
                //Log.d(TAG, String.format("%02x", id));
            }
            sb.append("\n");

            IsoDep isoDep = IsoDep.get(tag);
            if (isoDep != null) {
                sb.append("IsoDep\n");
            }

            sb.append(txt);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView1.setText(sb);
                }
            });
        }
    }
}

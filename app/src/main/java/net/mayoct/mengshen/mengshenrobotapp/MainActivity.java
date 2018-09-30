package net.mayoct.mengshen.mengshenrobotapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    private BluetoothAdapter mBTAdapter;
    private BluetoothDevice mBTDevice;
    private BluetoothSocket mBTSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private String MacAddress = "00:14:03:05:5A:47";
    private String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";    // SPP

    private int COMMAND_INIT = 10;
    private int COMMAND_SPEAK = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList data = new ArrayList<>();
        data.add(new Tango("多少钱",   "duōshǎoqián",  "221"));
        data.add(new Tango("微信",    "wēixìn",       "14-"));
        data.add(new Tango("等你",    "déngnǐ",       "23-"));
        data.add(new Tango("飞机场", "fēijīchǎng", "113"));
        data.add(new Tango("西红柿", "xīhóngshì", "124"));
        data.add(new Tango("乒乓球", "pīngpāngqiú", "112"));
        data.add(new Tango("图书馆", "túshūguǎn", "213"));
        data.add(new Tango("照相机", "zhàoxiàngjī", "441"));
        data.add(new Tango("自行车", "zìxíngchē", "421"));
        data.add(new Tango("穿衣服", "chuān yīfu", "110"));
        data.add(new Tango("打电话", "dǎ diànhuà", "344"));
        data.add(new Tango("摘帽子", "zhāi màozi", "140"));
        data.add(new Tango("踢足球", "tī zúqiú", "122"));
        data.add(new Tango("听音乐", "tīng yīnyuè", "114"));

        ListView listView = (ListView) findViewById(R.id.listView);
        TangoListAdapter adapter = new TangoListAdapter(this,
                R.layout.tangolist_item, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);

        btConnect();
        if (mBTSocket != null) {
            try {
                mOutputStream = mBTSocket.getOutputStream();
                mInputStream = mBTSocket.getInputStream();
            } catch (IOException e) {
                // do nothing
            }
        }

        tts = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d("TestMengshen", "initialized");
        } else {
            Log.e("TestMengshen", "failed to initialize");
        }
    }

    private void shutDown() {
        if (tts != null) {
            tts.shutdown();
        }
        if (mBTSocket != null) {
            try {
                mBTSocket.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            Tango item = (Tango) listView.getItemAtPosition(position);
            btSend(COMMAND_INIT, item.getTones());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // do  nothing
            }
            btSend(COMMAND_SPEAK, item.getTones());
            speak(item.getHanzi());
        }
    };

    private void speak(String text) {
        if (tts.isSpeaking()) {
            tts.stop();
        }
        setSpeechRate(0.3f);
        setSpeechPitch(1.0f);
        tts.setLanguage(Locale.SIMPLIFIED_CHINESE);

        if (Build.VERSION.SDK_INT >= 21) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "messageID");
        } else {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }
        setTtsListener();
    }

    private void setSpeechRate(float rate) {
        if (tts != null) {
            tts.setSpeechRate(rate);
        }
    }

    private void setSpeechPitch(float pitch) {
        if (tts != null) {
            tts.setPitch(pitch);
        }
    }

    private void setTtsListener() {
        if (Build.VERSION.SDK_INT >= 15) {
            int listenerResult =
                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            Log.d("TestMoegami", "progress on Start " + utteranceId);
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Log.d("TestMoegami", "progress on Done " + utteranceId);
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.d("TestMoegami", "progress on Error " + utteranceId);
                        }
                    });
        } else {
            Log.e("TestMoegami", "Build VERSION is less than API 15");
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        shutDown();
    }

    private void btConnect() {
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            mBTDevice = mBTAdapter.getRemoteDevice(MacAddress);
        } catch (IllegalArgumentException e1) {
            mBTDevice = null;
        }
        if (mBTDevice != null) {
            try {
                mBTSocket = mBTDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            } catch(IOException e2){
                mBTSocket = null;
            }
        }
        if (mBTSocket != null) {
            try {
                mBTSocket.connect();
            } catch (IOException e3) {
                try {
                    mBTSocket.close();
                    mBTSocket = null;
                } catch (IOException e4) {
                    return;
                }
            }
        }
    }

    private void btSend(int command, String data) {
        byte sendBuffer[] = {0, 0, 0, 0};
        byte receiveBuffer[] = {0, 0, 0, 0};

        sendBuffer[0] = (byte) command;

        for (int i = 0; i < 3; i++) {
            char ch = data.charAt(i);
            if (ch == '-') {
                sendBuffer[i + 1] = (byte) 0xff;
            } else {
                sendBuffer[i + 1] = (byte) (ch - '0');
            }
        }
        if (mBTSocket != null) {
            try {
                mOutputStream.write(sendBuffer, 0, 4);
            } catch (IOException e) {
                try {
                    mBTSocket.close();
                } catch (IOException e1) {
                    // do nothing
                }
            }
            try {
                mInputStream.read(receiveBuffer, 0, 1);
                mInputStream.read(receiveBuffer, 1, 1);
                mInputStream.read(receiveBuffer, 2, 1);
                mInputStream.read(receiveBuffer, 3, 1);
            } catch (IOException e2) {
                try {
                    mBTSocket.close();
                } catch (IOException e3) {
                    // do nothing
                }
            }
        }
    }
}

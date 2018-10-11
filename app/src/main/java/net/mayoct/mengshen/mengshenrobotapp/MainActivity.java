package net.mayoct.mengshen.mengshenrobotapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener {

    static public byte COMMAND_INIT = 'I';
    static public byte COMMAND_SPEAK_01 = 'A';
    static public byte COMMAND_SPEAK_02 = 'B';
    static public byte COMMAND_SPEAK_03 = 'C';
    private static String[] category =
            {"Geek", "学汉语", "基本会话", "买东西"};
    private static ArrayList<Tango> tangoList[] = null;
    private BluetoothAdapter mBTAdapter;
    private BluetoothDevice mBTDevice;
    private BluetoothSocket mBTSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    //private String MacAddress = "00:14:03:05:5A:47";  // EasyBT
    private String MacAddress = "00:14:03:05:5A:47";    // HC-05
    private String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";    // SPP
    private TextToSpeech tts;

    private Typeface defaultTypeface = null;

    public String[] getCategory() {
        return this.category;
    }

    protected void initializeTangoList() {
        if (tangoList != null) {
            return;
        }
        tangoList = new ArrayList[4];
        // Geek
        tangoList[0] = new ArrayList();
        tangoList[0].add(new Tango("颜值", "yán zhí", "22-"));
        tangoList[0].add(new Tango("还是", "hái shi", "20-"));
        tangoList[0].add(new Tango("转车", "zhuǎn chē", "31-"));
        tangoList[0].add(new Tango("机器人", "jī qì rén", "142"));
        tangoList[0].add(new Tango("电脑", "diàn nǎo", "43-"));
        tangoList[0].add(new Tango("打电话", "dǎ diàn huà", "344"));
        tangoList[0].add(new Tango("苹果", "píng guǒ", "23-"));

        // 学汉语
        tangoList[1] = new ArrayList();
        tangoList[1].add(new Tango("比较", "bǐ jiào", "34-"));
        tangoList[1].add(new Tango("练习", "liàn xí", "42-"));

        // 基本会话
        tangoList[2] = new ArrayList();
        tangoList[2].add(new Tango("等你", "děng nǐ", "33-"));
        tangoList[2].add(new Tango("宿舍", "sù shè", "44-"));
        tangoList[2].add(new Tango("真抱歉", "zhēn bào qiàn", "144"));
        tangoList[2].add(new Tango("办公室", "bàn gōng shì", "414"));
        tangoList[2].add(new Tango("踢足球", "tī zú qiú", "122"));
        tangoList[2].add(new Tango("洗手间", "xǐ shǒu jiān", "331"));
        tangoList[2].add(new Tango("帮忙", "bāng máng", "12-"));
        tangoList[2].add(new Tango("上班", "shàng bān", "41-"));
        tangoList[2].add(new Tango("明天", "míng tiān", "21-"));

        // 买东西
        tangoList[3] = new ArrayList();
        tangoList[3].add(new Tango("多少钱", "duō shǎo qián", "132"));
        tangoList[3].add(new Tango("微信", "wēi xìn", "14-"));
        tangoList[3].add(new Tango("押金", "yā jīn", "11-"));
        tangoList[3].add(new Tango("窗口", "chuāng kǒu", "13-"));
        tangoList[3].add(new Tango("表格", "biǎo gé", "32-"));
        tangoList[3].add(new Tango("密码", "mì mǎ", "43-"));
        tangoList[3].add(new Tango("服务员", "fú wù yuán", "242"));
    }

    public ArrayList getTangoList(int categoryNo) {
        return tangoList[categoryNo];
    }

    public Typeface getDefaultTypeface() {
        if (defaultTypeface == null) {
            defaultTypeface = Typeface.createFromAsset(getAssets(), "fonts/SentyCreamPuff.ttf");
        }
        return defaultTypeface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTangoList();
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SentyCreamPuff.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction xact = fragmentManager.beginTransaction();

        Fragment fragment = new TitleFragment();
        xact.replace(R.id.fragment_area, fragment, TitleFragment.TAG);
        xact.commit();

        btConnect();
        if (mBTSocket != null) {
            try {
                mOutputStream = mBTSocket.getOutputStream();
                mInputStream = mBTSocket.getInputStream();
            } catch (IOException e) {
                Log.e("MainActivity", "BT Socket Open Error: " + e.getMessage());
            }
        }

        tts = new TextToSpeech(this, this);
        setTtsListener();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void onDestroy() {
        super.onDestroy();
        shutDown();
    }

    private void shutDown() {
        if (tts != null) {
            tts.shutdown();
        }
        btShutDown();
    }

    private void btConnect() {
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter == null) {
            mBTDevice = null;
            mBTSocket = null;
        } else {
            try {
                mBTDevice = mBTAdapter.getRemoteDevice(MacAddress);
            } catch (IllegalArgumentException e1) {
                mBTDevice = null;
                Log.e("MainActivity", "BT Connect Error: " + e1.getMessage());
            }
            if (mBTDevice != null) {
                try {
                    mBTSocket = mBTDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                } catch (IOException e2) {
                    mBTSocket = null;
                    Log.e("MainActivity", "BT Connect Error: " + e2.getMessage());
                }
            }
            if (mBTSocket != null) {
                try {
                    mBTSocket.connect();
                } catch (IOException e3) {
                    Log.e("MainActivity", "BT Connect Error: " + e3.getMessage());
                    try {
                        mBTSocket.close();
                        mBTSocket = null;
                    } catch (IOException e4) {
                        // do nothing
                    }
                }
            }
        }
    }

    public void btSend(byte command, String data) {
        byte sendBuffer[] = {0, 0, 0, 0};

        sendBuffer[0] = command;

        for (int i = 0; i < 3; i++) {
            char ch = data.charAt(i);
            sendBuffer[i + 1] = (byte) ch;
        }
        if (mBTSocket != null) {
            try {
                String content = new String(sendBuffer);
                Log.i("MainActivity", "BT Write: " + content);
                mOutputStream.write(sendBuffer, 0, 1);
                mOutputStream.flush();
                mOutputStream.write(sendBuffer, 1, 1);
                mOutputStream.flush();
                mOutputStream.write(sendBuffer, 2, 1);
                mOutputStream.flush();
                mOutputStream.write(sendBuffer, 3, 1);
                mOutputStream.flush();
            } catch (IOException e) {
                Log.e("MainActivity", "BT Write Error: " + e.getMessage());
                try {
                    mBTSocket.close();
                } catch (IOException e1) {
                    Log.e("MainActivity", "BT Close Error: " + e1.getMessage());
                }
            }
        }
    }

    private void btShutDown() {
        if (mBTSocket != null) {
            try {
                mBTSocket.close();
            } catch (IOException e) {
                Log.e("MainActivity", "BT Close Error: " + e.getMessage());
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d("MainActitivy", "TTS Initialized");
        } else {
            Log.e("MainActivity", "TTS Failed to Initialize");
        }
    }

    public void ttsSpeak(String text, String messageID) {
        if (tts.isSpeaking()) {
            tts.stop();
        }
        if (!text.startsWith("-")) {
            setTtsSpeechRate(0.3f);
            setTtsSpeechPitch(1.0f);
            tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, messageID);
            //setTtsListener();
        }
    }

    private void setTtsSpeechRate(float rate) {
        if (tts != null) {
            tts.setSpeechRate(rate);
        }
    }

    private void setTtsSpeechPitch(float pitch) {
        if (tts != null) {
            tts.setPitch(pitch);
        }
    }

    private void setTtsListener() {
        int listenerResult =
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        Log.d("MainActivity", "TTS Progress on Start " + utteranceId);
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        Log.d("MainActivity", "TTS Progress on Done " + utteranceId);
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Log.d("MainActivity", "TTS Progress on Error " + utteranceId);
                    }
                });
    }
}

package net.sourceforge.simcpux;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fxchat.wxpaymodule.WXPayConstants;
import com.fxchat.wxpaymodule.interfaces.WXPayResultCallback;
import com.fxchat.wxpaymodule.receivers.WXPayResultReceiver;

/**
 * Created by ousiyuan on 2019/2/13 0013.
 * description:
 */
public abstract class BaseWXPayActivity extends AppCompatActivity implements WXPayResultCallback {
    private WXPayResultReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WXPayConstants.WECHAT_PAY_ACTION);
        mReceiver = new WXPayResultReceiver(this);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }
}

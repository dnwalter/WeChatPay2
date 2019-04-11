package com.fxchat.wxpaymodule.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fxchat.wxpaymodule.WXPayConstants;
import com.fxchat.wxpaymodule.interfaces.WXPayResultCallback;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by ousiyuan on 2019/2/13 0013.
 * description:
 */
public class WXPayResultReceiver extends BroadcastReceiver {

    private WXPayResultCallback mCallback;

    public WXPayResultReceiver(WXPayResultCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mCallback != null && intent != null) {
            int errCode = intent.getIntExtra(WXPayConstants.RESP_ERR_CODE, -1);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    mCallback.paySucc();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    mCallback.payCancel();
                    break;
                default:
                    mCallback.payFail();
                    break;
            }
        }
    }
}

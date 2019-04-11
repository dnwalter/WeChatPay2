package com.fxchat.wxpaymodule.interfaces;

/**
 * Created by ousiyuan on 2019/2/13 0013.
 * description:
 */
public interface WXPayResultCallback {
    // 支付成功
    void paySucc();
    // 支付取消
    void payCancel();
    // 支付失败
    void payFail();
}

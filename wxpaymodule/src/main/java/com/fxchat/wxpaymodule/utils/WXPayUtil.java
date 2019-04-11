package com.fxchat.wxpaymodule.utils;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedHashMap;

/**
 * Created by ousiyuan on 2019/2/13 0013.
 * description:
 */
public class WXPayUtil {
    private IWXAPI iwxapi; //微信支付api

    private WXPayUtil.WXPayBuilder builder;

    private WXPayUtil(WXPayUtil.WXPayBuilder builder) {
        this.builder = builder;
    }

    /**
     * 调起微信支付的方法,不需要在客户端签名
     **/
    public void toWXPayNotSign(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, builder.getAppId()); //初始化微信api

        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
            @Override
            public void run() {
                PayReq request = new PayReq(); //调起微信APP的对象
                // 设置参数
                request.appId = builder.getAppId();
                request.partnerId = builder.getPartnerId();
                request.prepayId = builder.getPrepayId();
                request.packageValue = builder.getPackageValue();
                request.nonceStr = builder.getNonceStr();
                request.timeStamp =builder.getTimeStamp();
                request.sign = builder.getSign();
                //发送调起微信的请求
                iwxapi.sendReq(request);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调起微信支付的方法,需要在客户端签名
     * @param key key为商户平台设置的密钥key（即APPSecret）
     **/
    public void toWXPayAndSign(Context context, String appid, final String key) {
        iwxapi = WXAPIFactory.createWXAPI(context, appid); //初始化微信api
        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
            @Override
            public void run() {
                PayReq request = new PayReq(); //调起微信APP的对象
                // 设置参数
                request.appId = builder.getAppId();
                request.partnerId = builder.getPartnerId();
                request.prepayId = builder.getPrepayId();
                request.packageValue = "Sign=WXPay";
                //                request.nonceStr = genNonceStr();
                //                request.timeStamp = String.valueOf(genTimeStamp());
                request.nonceStr = builder.getNonceStr();
                request.timeStamp = builder.getTimeStamp();
                request.sign = builder.getSign();
                //签名
                LinkedHashMap<String, String> signParams = new LinkedHashMap<>();
                signParams.put("appid", request.appId);
                signParams.put("noncestr", request.nonceStr);
                signParams.put("package", request.packageValue);
                signParams.put("partnerid", request.partnerId);
                signParams.put("prepayid", request.prepayId);
                signParams.put("timestamp", request.timeStamp);
                request.sign = new SignUtil().getPackageSign(signParams, key);
                iwxapi.sendReq(request);//发送调起微信的请求
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static class WXPayBuilder {
        public String appId;
        public String partnerId;
        public String prepayId;
        public String packageValue;
        public String nonceStr;
        public String timeStamp;
        public String sign;

        public WXPayUtil build() {
            return new WXPayUtil(this);
        }

        public String getAppId() {
            return appId;
        }

        public WXPayUtil.WXPayBuilder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public WXPayUtil.WXPayBuilder setPartnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public WXPayUtil.WXPayBuilder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }

        public String getPackageValue() {
            return packageValue;
        }

        public WXPayUtil.WXPayBuilder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public WXPayUtil.WXPayBuilder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public WXPayUtil.WXPayBuilder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public String getSign() {
            return sign;
        }

        public WXPayUtil.WXPayBuilder setSign(String sign) {
            this.sign = sign;
            return this;
        }
    }
}

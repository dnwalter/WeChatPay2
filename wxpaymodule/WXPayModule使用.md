# WXPayModule
- ## 首先
  项目先导入WXPayModule，确认项目加入权限

```
<uses-permission android:name="android.permission.INTERNET"/>
```

- ## 第一步
  把module下类WXPayConstants的APP_ID修改为项目在微信开放平台申请的appId;若商户ID由前端写死，也要配置WXPayConstants的PARTNER_ID

- ## 第二步
  在主包名目录下新建wxapi文件夹，将module下的WXPayEntryActivity复制到wxapi文件夹中。（若主包名目录下已有wxapi文件夹和WXPayEntryActivity文件，就不要重复添加了。）
  AndroidManifest.xml加入以下代码
 ```
  <activity
               android:name=".wxapi.WXPayEntryActivity"
               android:configChanges="keyboardHidden|orientation|screenSize"
               android:exported="true"
               android:launchMode="singleTop"
               android:screenOrientation="portrait"
               android:theme="@android:style/Theme.Light.NoTitleBar" />
  ```

- ## 第三步（注册广播）
  在要进行支付的界面注册广播来接收支付结果的返回，界面同时要实现接口WXPayResultCallback。如代码所示，但调起微信支付后，支付成功、支付取消和支付失败会分别进入三个回调方法，可在里面编写对应逻辑。（++建议：如果项目有多个地方用到微信支付，可以编写一个基类BaseWXPayActivity，让其他支付界面继承++）

```
public class PayActivity extends AppCompatActivity implements WXPayResultCallback {
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
    
    @Override
    public void paySucc() {
    }

    @Override
    public void payCancel() {
    }

    @Override
    public void payFail() {
    }
}
```

- ## 第四步（如何进行微信支付）
1.   假设我们已经请求后台拿到了订单的信息，订单数据实体需包含以下数据。其中package可以不返回，因为是固定值“Sign=WXPay”，前端代码可以默认填入。[各参数定义](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2)
```
String appId;
String partnerId;
String prepayId;
String packageValue;
String nonceStr;
String timeStamp;
String sign;
```

2.   将获得的数据分别填入下面的builder，然后执行toWXPayNotSign方法就会进行微信支付了
```
WXPayUtil.WXPayBuilder builder = new WXPayUtil.WXPayBuilder();
builder.setAppId(...)
       .setPartnerId(...)
       .setPrepayId(...)
       .setPackageValue(...)
       .setNonceStr(...)
       .setTimeStamp(...)
       .setSign(...)
       .build().toWXPayNotSign(PayActivity.this);
```
3.   注意上面的方法是后台帮我们处理了签名，返回sign字段所调用的方法。若后台没进行签名算法处理（微信开发平台建议这个签名算法最好是放在后台处理），我们前端要自己生成签名，调用toWXPayAndSign方法。这时还要去设置WXPayConstants.APP_SECRET（注意：这个为商户平台的key，不是开发者平台的key），这个值可以在微信开发者平台，所申请的项目的AppID的下方找到。
```
WXPayUtil.WXPayBuilder builder = new WXPayUtil.WXPayBuilder();
builder.setAppId(...)
       ...
       .build().toWXPayAndSign(PayActivity.this, WXPayConstants.APP_ID, WXPayConstants.APP_SECRET);
```

## 总结
   以上为发起微信支付到收到支付结果回调的整个流程。要前端自己进行签名的方法，可以结合下面文档。
   
   [微信支付开发文档安全规范](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3)
   
   [Android实现微信支付和踩过的深坑](https://blog.csdn.net/qilin001cs/article/details/75909756)


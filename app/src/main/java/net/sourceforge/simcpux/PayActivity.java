package net.sourceforge.simcpux;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.fxchat.wxpaymodule.utils.WXPayUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class PayActivity extends BaseWXPayActivity{
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        mTextView = findViewById(R.id.text1);
    }

    public void ToPay(View view){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String url = "https://wxpay.wxutil.com/pub_v2/app/app_pay.php";
                try{
                    byte[] buf = Util.httpGet(url);
                    if (buf != null && buf.length > 0) {
                        String content = new String(buf);
                        JSONObject json = new JSONObject(content);
                        if(null != json && !json.has("retcode") ){
                            Message message = new Message();
                            message.what = 1;
                            message.obj = json;
                            handler.handleMessage(message);
                        }
                    }
                }catch(Exception e){
                    int i =0;
                    i++;
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    JSONObject json= (JSONObject) msg.obj;
                    WXPayUtil.WXPayBuilder builder = new WXPayUtil.WXPayBuilder();
                    try {
                        builder.setAppId(json.getString("appid"))
                                .setPartnerId(json.getString("partnerid"))
                                .setPrepayId(json.getString("prepayid"))
                                .setPackageValue(json.getString("package"))
                                .setNonceStr(json.getString("noncestr"))
                                .setTimeStamp(json.getString("timestamp"))
                                .setSign(json.getString("sign"))
                                .build().toWXPayNotSign(PayActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        };
    };

    @Override
    public void paySucc() {
        mTextView.setText("支付成功");
    }

    @Override
    public void payCancel() {
        mTextView.setText("支付取消");
    }

    @Override
    public void payFail() {
        mTextView.setText("支付失败");
    }
}

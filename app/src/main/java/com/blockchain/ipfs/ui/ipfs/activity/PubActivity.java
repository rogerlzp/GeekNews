package com.blockchain.ipfs.ui.ipfs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.blockchain.ipfs.R;
import com.blockchain.ipfs.app.Constants;
import com.blockchain.ipfs.base.SimpleActivity;
import com.blockchain.ipfs.util.ToastUtil;
import com.ipfs.api.IPFSAnroid;
import com.ipfs.api.entity.FileAdd;
import com.ipfs.api.entity.Pub;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhengpingli on 2018/6/15.
 */

public class PubActivity extends SimpleActivity {

    @BindView(R.id.et_topic)
    EditText et_topic;

    @BindView(R.id.et_message)
    EditText et_message;


    @BindView(R.id.btn_pub)
    Button btn_pub;

    @BindView(R.id.tv_pub_node_result)
    TextView tv_pub_node_result;


    String ipfsNodeHash = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_ipfs_pub;
    }


    @Override
    protected void initEventAndData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            // 从publish node 过来的
            ipfsNodeHash = b.getString(Constants.IPFS_NODE_HASH);
            if (ipfsNodeHash != null) {
                et_message.setText(ipfsNodeHash);
                et_topic.setText("PUBLISH_NODE");
                pubMessage();
            }
        }

    }

    @OnClick(R.id.btn_pub)
    public void pubMessage() {
        if (TextUtils.isEmpty(et_message.getText().toString().trim())) {
            ToastUtil.shortShow("et_message 不能为空");
            return;
        }
        if (TextUtils.isEmpty(et_topic.getText().toString().trim())) {
            ToastUtil.shortShow("et_topic 不能为空");
            return;
        }

        IPFSAnroid ipfsAnroid = new IPFSAnroid();
        ipfsAnroid.pubsub().pub(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        ToastUtil.show("pub succeed");
                                        tv_pub_node_result.setText("节点发布到：" + ipfsNodeHash);
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        ToastUtil.show("pub failed");
                                        KLog.e(t.getMessage());
                                    }
                                }, et_topic.getText().toString().trim(), et_message.getText().toString().trim()
        );

    }


}

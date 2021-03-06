package com.blockchain.ipfs.ipfs;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Contacts;

import com.blockchain.ipfs.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by zhengpingli on 2018/6/12.
 */
public class IPFSDaemonAsyn {

    private Context mContext;

    /**
     * 获取binary文件
     *
     * @return
     */
    public IPFSDaemonAsyn(Context context) {
        mContext = context;
    }

    private File getBinaryFile() {
        return new File(mContext.getFilesDir() + "/ipfsbin");
    }

    private File getRepoPath() {
        return new File(mContext.getFilesDir() + "/.ipfs_repo");
    }

    private File getVersionFile() {
        return new File(mContext.getFilesDir() + "/version");
    }

    /**
     * 下载文件或者更新文件
     *
     * @return
     */
    private void downloadFile() {

        BufferedSource source = null;
        BufferedSink sink = null;
        try {
            source = Okio.buffer(Okio.source(mContext.getAssets().open(getBinaryFileByABI())));
            sink = Okio.buffer(Okio.sink(getBinaryFile()));
            while (!source.exhausted()) {
                source.read(sink.buffer(), 1024);
            }
            source.close();
            sink.close();
        } catch (IOException ie) {
            ie.getMessage();
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {

                }
            }
            if (sink != null) {
                try {
                    sink.close();
                } catch (IOException e) {

                }
            }
        }
    }


    private String getBinaryFileByABI() {
        switch (Build.SUPPORTED_ABIS[0].toLowerCase().substring(0, 3)) {
            case "x86":
                return "x86";
            case "arm":
                return "arm";
            default:
                return "unknown";
        }
    }

    public String initIpfs() {
        downloadFile();
        getBinaryFile().setExecutable(true);
        return runCmd("init"); //返回init 命令结果
    }


    public String runCmd(String cmd) {
        String[] env = new String[]{"IPFS_PATH=" + getRepoPath().getAbsoluteFile()};
//        env[0] = "IPFS_PATH=" + getRepoPath().getAbsoluteFile();
        String command = getBinaryFile().getAbsolutePath() + " " + cmd;

        String line = "";
        StringBuffer sb = new StringBuffer();


        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command, env);
//            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));


//            while ((line = stdoutReader.readLine()) != null) {
////               line += line;
//                sb.append(line);
//            }
            new Thread() {
                public void run() {
                    InputStream ins1 = process.getInputStream();
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(ins1));

//                    BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
                    try {
                        String line1 = null;
                        while ((line1 = br1.readLine()) != null) {
                            if (line1 != null) {
                                LogUtil.d("IPFS sub command:" + line1);
//                                if(line1.indexOf("syntax check result:")!=-1){
//                                    sb.append(line1);
//                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            ins1.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            // "Daemon is ready" 作为启动标识


//            while ((line = stderrReader.readLine()) != null) {
////                System.out.println(line);
////                line += line;
//                sb.append(line);
//            }
            int exitVal = process.waitFor();
            System.out.println("process exit value is " + exitVal);
        } catch (IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        return sb.toString();
    }


}
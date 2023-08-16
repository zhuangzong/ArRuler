package com.kang.ardemo.ardemo;

import android.widget.Toast;

import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.ux.ArFragment;

public class MyFragment extends ArFragment {
    @Override
    protected void handleSessionException(UnavailableException sessionException) {
        String message;
        if (sessionException instanceof UnavailableArcoreNotInstalledException) {
            message = "请安装ARCore";
        } else if (sessionException instanceof UnavailableApkTooOldException) {
            message = "请升级ARCore";
        } else if (sessionException instanceof UnavailableSdkTooOldException) {
            message = "请升级app";
        } else if (sessionException instanceof UnavailableDeviceNotCompatibleException) {
            message = "当前设备不支持AR";
        } else {
            message = "未能创建AR会话,请查看机型适配,arcore版本与系统版本";
            String var3 = String.valueOf(sessionException);
        }
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

    }
}

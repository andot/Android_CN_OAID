/*
 * Copyright (c) 2019-2021 gzu-liyujiang <1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */
package com.github.gzuliyujiang.oaid.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.gzuliyujiang.oaid.IGetter;
import com.github.gzuliyujiang.oaid.IOAID;
import com.github.gzuliyujiang.oaid.OAIDLog;
import com.uodis.opendevice.aidl.OpenDeviceIdentifierService;

/**
 * 参阅华为官方SDK：AdvertisingIdClient.getAdvertisingIdInfo(context).getId()
 *
 * @author 大定府羡民（1032694760@qq.com）
 * @since 2020/5/30
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class HuaweiImpl implements IOAID {
    private final Context context;

    public HuaweiImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean supported() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.huawei.hwid", 0);
            return pi != null;
        } catch (Throwable e) {
            OAIDLog.print(e);
            return false;
        }
    }

    @Override
    public void doGet(@NonNull final IGetter getter) {
        Intent intent = new Intent("com.uodis.opendevice.OPENIDS_SERVICE");
        intent.setPackage("com.huawei.hwid");
        try {
            boolean isBinded = context.bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    OAIDLog.print("Huawei OPENIDS_SERVICE connected");
                    try {
                        OpenDeviceIdentifierService anInterface = OpenDeviceIdentifierService.Stub.asInterface(service);
                        String IDs = anInterface.getIDs();
                        if (IDs == null || IDs.length() == 0) {
                            throw new RuntimeException("Huawei IDs get failed");
                        }
                        getter.onOAIDGetComplete(IDs);
                    } catch (Throwable e) {
                        OAIDLog.print(e);
                        getter.onOAIDGetError(e);
                    } finally {
                        context.unbindService(this);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    OAIDLog.print("Huawei OPENIDS_SERVICE disconnected");
                }
            }, Context.BIND_AUTO_CREATE);
            if (!isBinded) {
                throw new RuntimeException("Huawei OPENIDS_SERVICE bind failed");
            }
        } catch (Throwable e) {
            getter.onOAIDGetError(e);
        }
    }

}

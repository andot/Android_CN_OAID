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

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.gzuliyujiang.oaid.IGetter;
import com.github.gzuliyujiang.oaid.IOAID;

/**
 * @author 大定府羡民（1032694760@qq.com）
 * @since 2020/5/30
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DefaultImpl implements IOAID {

    @Override
    public boolean supported() {
        return false;
    }

    @Override
    public void doGet(@NonNull final IGetter getter) {
        getter.onOAIDGetError(new RuntimeException("Unsupported"));
    }

}

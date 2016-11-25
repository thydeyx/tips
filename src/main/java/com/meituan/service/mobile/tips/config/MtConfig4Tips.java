// Copyright (C) 2015 Meituan
// All rights reserved
package com.meituan.service.mobile.tips.config;

import com.meituan.service.mobile.tips.consts.TipsConsts;
import com.sankuai.meituan.config.annotation.MtConfig;

/**
 * @author wj
 * @version 1.0
 */
public class MtConfig4Tips {

    @MtConfig(clientId = TipsConsts.CLIENTID, key = "isQueryResult")
    public static boolean isQueryResult = true;
}
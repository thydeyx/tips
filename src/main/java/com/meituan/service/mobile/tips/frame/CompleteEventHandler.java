package com.meituan.service.mobile.tips.frame;

import com.meituan.service.mobile.tips.model.TipsEvent;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-26
 * Time: 下午11:46
 * To change this template use File | Settings | File Templates.
 */
public interface CompleteEventHandler {
    public int work(TipsEvent event) throws Exception;
}

package com.meituan.service.mobile.tips.thrift;

import com.meituan.dataapp.service.tips.thrift.*;
import com.meituan.jmonitor.JMonitor;
import com.meituan.service.mobile.mtthrift.server.MTIface;
import com.meituan.service.mobile.tips.frame.CompleteEventHandler;
import com.meituan.service.mobile.tips.model.SearchTipsItem;
import com.meituan.service.mobile.tips.model.SearchTipsRes;
import com.meituan.service.mobile.tips.model.TipsEvent;
import com.meituan.service.mobile.tips.router.TipsRouter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-24
 * Time: 下午3:27
 * To change this template use File | Settings | File Templates.
 */
public class TipsThriftProcessor extends MTIface implements SearchTips.Iface,ApplicationContextAware,CompleteEventHandler {

    private Log log = LogFactory.getLog(TipsThriftProcessor.class);

    private ApplicationContext appContext;

    @Autowired
    private TipsRouter tipsRouter;

    @Override
    public TipsRes GetTips(TipsReq tipsReq) throws TException {
        TipsEvent event = getTipsEvent(tipsReq);
        try {
            this.work(event);
        } catch (Exception e) {
            log.error("e->" + e.getMessage(), e);
        }
        return event.getTipsRes();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    private TipsEvent getTipsEvent(TipsReq tipsReq) {
        SearchTipsRes searchTipsRes = new SearchTipsRes(new ArrayList<SearchTipsItem>());
        TipsRes tipsRes = new TipsRes(new ArrayList<TipsItem>(), new TipsStidInfo());
        TipsEvent event = new TipsEvent(searchTipsRes,tipsReq, tipsRes);
        event.setSegment(tipsRouter.getSegment(tipsReq));
        return event;
    }

    @Override
    public int work(TipsEvent event) throws Exception {
        CompleteEventHandler handler;

        List<String> handlerList = event.getSegment().getHandlerList();
        for (String strategy:handlerList) {
            try {
                long handlerBegin = System.currentTimeMillis();
                handler = (CompleteEventHandler)appContext.getBean(strategy);
                int ret = handler.work(event);
                if (ret != 0) {
                    JMonitor.add("handler.error");
                }
                JMonitor.add(strategy, System.currentTimeMillis() - handlerBegin);
            } catch (Exception e) {
                log.error("e->" + e.getMessage(), e);
                JMonitor.add("bean.exception");
            }
        }

        return 0;
    }

    public void setTipsRouter(TipsRouter tipsRouter) {
        this.tipsRouter = tipsRouter;
    }
}

package com.meituan.service.mobile.tips.packResult;

import com.meituan.dataapp.service.tips.thrift.TipsItem;
import com.meituan.dataapp.service.tips.thrift.TipsRes;
import com.meituan.dataapp.service.tips.thrift.TipsStidInfo;
import com.meituan.service.mobile.tips.consts.TipsConsts;
import com.meituan.service.mobile.tips.frame.CompleteEventHandler;
import com.meituan.service.mobile.tips.model.SearchTipsItem;
import com.meituan.service.mobile.tips.model.SearchTipsRes;
import com.meituan.service.mobile.tips.model.TipsEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-27
 * Time: 下午4:28
 * To change this template use File | Settings | File Templates.
 */
public class PackResultHandler implements CompleteEventHandler {

    private Log logger = LogFactory.getLog(TipsConsts.STGLOG);

    @Override
    public int work(TipsEvent event) throws Exception {
        packResult(event);
        return 0;
    }

    private void packResult(TipsEvent event) {
        SearchTipsRes result = event.getSearchTipsRes();
        TipsRes tipsRes = event.getTipsRes();
        packTipsList(result, tipsRes);
        packStdInfo(event, tipsRes);
    }

    private void packTipsList(SearchTipsRes searchTipsRes, TipsRes tipsRes) {
        int count = 0;

        List<TipsItem> tipsList = tipsRes.getTipsList();
        for (SearchTipsItem item : searchTipsRes.getTipsList()) {
            // 出词个数限制为最多8个
            if (tipsList.size() >= 8) {
                break;
            }

            // filter result count is zero
            if (item.getCount() != 0) {
                TipsItem tipsItem = new TipsItem(item.getWord());
                tipsItem.setCount(item.getCount());
                tipsList.add(tipsItem);
            }
        }

        // 出词个数限制为2~8个筛选项
        if (tipsList.size() >= 8) {
            tipsList.subList(8, tipsList.size()).clear();
        } else if (tipsList.size() < 2) {
            tipsList.clear();
        }
    }

    private void packStdInfo(TipsEvent event, TipsRes res) {
        TipsStidInfo stidInfo = new TipsStidInfo(event.getGlobalId(), event.getSegment().getStrategy());
        res.setStidInfo(stidInfo);
    }
}

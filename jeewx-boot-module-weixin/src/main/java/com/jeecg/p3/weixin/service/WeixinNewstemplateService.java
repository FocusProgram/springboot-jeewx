package com.jeecg.p3.weixin.service;

import com.jeecg.p3.weixin.entity.WeixinNewstemplate;
import org.jeecgframework.p3.core.utils.common.PageList;
import org.jeecgframework.p3.core.utils.common.PageQuery;

import java.util.List;

/**
 * 描述：</b>图文模板表<br>
 *
 * @author：
 * @since：2018年07月13日 12时46分13秒 星期五
 * @version:1.0
 */
public interface WeixinNewstemplateService {


    public void doAdd(WeixinNewstemplate weixinNewstemplate);

    public void doEdit(WeixinNewstemplate weixinNewstemplate);

    public void doDelete(String id);

    public WeixinNewstemplate queryById(String id);

    public PageList<WeixinNewstemplate> queryPageList(PageQuery<WeixinNewstemplate> pageQuery);

    //update-begin--Author:zhangweijian  Date: 20180820 for：获取所有图文素材

    /**
     * @param jwid
     * @param uploadType
     * @return
     * @功能：获取所有图文素材
     */
    public List<WeixinNewstemplate> getAllItems(String jwid, String uploadType);
    //update-end--Author:zhangweijian  Date: 20180820 for：获取所有图文素材

    //update-begin--Author:zhangweijian  Date: 20180820 for：新增media_id字段

    /**
     * @param jwid
     * @功能：上传图文素材
     */
    public String uploadNewstemplate(String id, String jwid);
    //update-end--Author:zhangweijian  Date: 20180820 for：新增media_id字段

}


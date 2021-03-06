package com.jeecg.p3.weixin.web.back;

import com.jeecg.p3.commonweixin.entity.MyJwWebJwid;
import com.jeecg.p3.commonweixin.util.Constants;
import com.jeecg.p3.system.service.MyJwWebJwidService;
import com.jeecg.p3.weixin.entity.WeixinAutoresponseDefault;
import com.jeecg.p3.weixin.service.WeixinAutoresponseDefaultService;
import org.apache.velocity.VelocityContext;
import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.p3.core.util.SystemTools;
import org.jeecgframework.p3.core.util.plugin.ViewVelocity;
import org.jeecgframework.p3.core.utils.common.PageQuery;
import org.jeecgframework.p3.core.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 描述：</b>未识别回复语<br>
 *
 * @author LeeShaoQing
 * @since：2018年07月20日 10时11分50秒 星期五
 * @version:1.0
 */
@Controller
@RequestMapping("/weixin/back/weixinAutoresponseDefault")
public class WeixinAutoresponseDefaultController extends BaseController {

    public final static Logger log = LoggerFactory.getLogger(WeixinAutoresponseDefaultController.class);
    @Autowired
    private WeixinAutoresponseDefaultService weixinAutoresponseDefaultService;
    @Autowired
    private MyJwWebJwidService myJwWebJwidService;

    /**
     * 列表页面
     *
     * @return
     */
    @RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
    public void list(@ModelAttribute WeixinAutoresponseDefault query, HttpServletResponse response, HttpServletRequest request,
                     @RequestParam(required = false, value = "pageNo", defaultValue = "1") int pageNo,
                     @RequestParam(required = false, value = "pageSize", defaultValue = "10") int pageSize) throws Exception {
        PageQuery<WeixinAutoresponseDefault> pageQuery = new PageQuery<WeixinAutoresponseDefault>();
        String jwid = request.getSession().getAttribute("jwid").toString();
        query.setJwid(jwid);
        //update-begin--Author:zhangweijian  Date: 20180928 for：无权限不能查看公众号数据
        //判断是否有权限
        String systemUserid = request.getSession().getAttribute("system_userid").toString();
        //update-begin--Author:zhangweijian  Date: 20181008 for：根据jwid和用户id查询公众号信息
        MyJwWebJwid jw = myJwWebJwidService.queryJwidByJwidAndUserId(jwid, systemUserid);
        //update-end--Author:zhangweijian  Date: 20181008 for：根据jwid和用户id查询公众号信息
        if (jw == null) {
            query.setJwid("-");
        }
        //update-end--Author:zhangweijian  Date: 20180928 for：无权限不能查看公众号数据
        pageQuery.setPageNo(pageNo);
        pageQuery.setPageSize(pageSize);
        VelocityContext velocityContext = new VelocityContext();
        pageQuery.setQuery(query);
        velocityContext.put("weixinAutoresponseDefault", query);
        velocityContext.put("pageInfos", SystemTools.convertPaginatedList(weixinAutoresponseDefaultService.queryPageList(pageQuery)));
        String viewName = "weixin/back/weixinAutoresponseDefault-list.vm";
        ViewVelocity.view(request, response, viewName, velocityContext);
    }

    /**
     * 详情
     *
     * @return
     */
    @RequestMapping(value = "toDetail", method = RequestMethod.GET)
    public void weixinAutoresponseDefaultDetail(@RequestParam(required = true, value = "id") String id, HttpServletResponse response, HttpServletRequest request) throws Exception {
        VelocityContext velocityContext = new VelocityContext();
        String viewName = "weixin/back/weixinAutoresponseDefault-detail.vm";
        WeixinAutoresponseDefault weixinAutoresponseDefault = weixinAutoresponseDefaultService.queryById(id);
        velocityContext.put("weixinAutoresponseDefault", weixinAutoresponseDefault);
        ViewVelocity.view(request, response, viewName, velocityContext);
    }

    /**
     * 跳转到添加页面
     *
     * @return
     */
    @RequestMapping(value = "/toAdd", method = {RequestMethod.GET, RequestMethod.POST})
    public void toAddDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        VelocityContext velocityContext = new VelocityContext();
        String viewName = "weixin/back/weixinAutoresponseDefault-add.vm";
        ViewVelocity.view(request, response, viewName, velocityContext);
    }

    /**
     * 保存信息
     *
     * @return
     */
    @RequestMapping(value = "/doAdd", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public AjaxJson doAdd(@ModelAttribute WeixinAutoresponseDefault weixinAutoresponseDefault, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            String jwid = request.getSession().getAttribute("jwid").toString();
            WeixinAutoresponseDefault autoresponseDefault = weixinAutoresponseDefaultService.queryBymsgTriggerType(weixinAutoresponseDefault.getMsgTriggerType(), jwid);
            if (autoresponseDefault != null) {
                j.setMsg("该触发类型的数据已存在,请勿重新添加");
                j.setSuccess(false);
                return j;
            }
            weixinAutoresponseDefault.setCreateTime(new Date());
            weixinAutoresponseDefaultService.doAdd(weixinAutoresponseDefault);
            j.setMsg("保存成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            j.setSuccess(false);
            j.setMsg("保存失败");
        }
        return j;
    }

    /**
     * 跳转到编辑页面
     *
     * @return
     */
    @RequestMapping(value = "toEdit", method = RequestMethod.GET)
    public void toEdit(@RequestParam(required = true, value = "id") String id, HttpServletResponse response, HttpServletRequest request) throws Exception {
        VelocityContext velocityContext = new VelocityContext();
        WeixinAutoresponseDefault weixinAutoresponseDefault = weixinAutoresponseDefaultService.queryById(id);
        velocityContext.put("weixinAutoresponseDefault", weixinAutoresponseDefault);
        String viewName = "weixin/back/weixinAutoresponseDefault-edit.vm";
        ViewVelocity.view(request, response, viewName, velocityContext);
    }

    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "/doEdit", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public AjaxJson doEdit(@ModelAttribute WeixinAutoresponseDefault weixinAutoresponseDefault, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            String jwid = request.getSession().getAttribute("jwid").toString();
            WeixinAutoresponseDefault autoresponseDefault = weixinAutoresponseDefaultService.queryBymsgTriggerType(weixinAutoresponseDefault.getMsgTriggerType(), jwid);
            if (autoresponseDefault != null) {
                if (!weixinAutoresponseDefault.getId().equals(autoresponseDefault.getId())) {
                    j.setMsg("该触发类型的数据已存在,请勿重新添加");
                    j.setSuccess(false);
                    return j;
                }
            }
            weixinAutoresponseDefault.setUpdateTime(new Date());
            String updateBy = (String) request.getSession().getAttribute(Constants.SYSTEM_USERID);
            weixinAutoresponseDefault.setUpdateBy(updateBy);
            weixinAutoresponseDefaultService.doEdit(weixinAutoresponseDefault);
            j.setMsg("编辑成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            j.setSuccess(false);
            j.setMsg("编辑失败");
        }
        return j;
    }


    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "doDelete", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson doDelete(@RequestParam(required = true, value = "id") String id) {
        AjaxJson j = new AjaxJson();
        try {
            weixinAutoresponseDefaultService.doDelete(id);
            j.setMsg("删除成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            j.setSuccess(false);
            j.setMsg("删除失败");
        }
        return j;
    }


}


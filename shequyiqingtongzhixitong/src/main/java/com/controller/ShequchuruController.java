







package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 社区人员出入
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/shequchuru")
public class ShequchuruController {
    private static final Logger logger = LoggerFactory.getLogger(ShequchuruController.class);

    @Autowired
    private ShequchuruService shequchuruService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service
    @Autowired
    private ZhuhuService zhuhuService;



    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("住户".equals(role))
            params.put("zhuhuId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = shequchuruService.queryPage(params);

        //字典表数据转换
        List<ShequchuruView> list =(List<ShequchuruView>)page.getList();
        for(ShequchuruView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ShequchuruEntity shequchuru = shequchuruService.selectById(id);
        if(shequchuru !=null){
            //entity转view
            ShequchuruView view = new ShequchuruView();
            BeanUtils.copyProperties( shequchuru , view );//把实体数据重构到view中

                //级联表
                ZhuhuEntity zhuhu = zhuhuService.selectById(shequchuru.getZhuhuId());
                if(zhuhu != null){
                    BeanUtils.copyProperties( zhuhu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setZhuhuId(zhuhu.getId());
                }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ShequchuruEntity shequchuru, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,shequchuru:{}",this.getClass().getName(),shequchuru.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("住户".equals(role))
            shequchuru.setZhuhuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        Wrapper<ShequchuruEntity> queryWrapper = new EntityWrapper<ShequchuruEntity>()
            .eq("zhuhu_id", shequchuru.getZhuhuId())
            .eq("shequchuru_types", shequchuru.getShequchuruTypes())
            .eq("lai_address", shequchuru.getLaiAddress())
            .eq("qu_address", shequchuru.getQuAddress())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ShequchuruEntity shequchuruEntity = shequchuruService.selectOne(queryWrapper);
        if(shequchuruEntity==null){
            shequchuru.setInsertTime(new Date());
            shequchuru.setCreateTime(new Date());
            shequchuruService.insert(shequchuru);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ShequchuruEntity shequchuru, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,shequchuru:{}",this.getClass().getName(),shequchuru.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("住户".equals(role))
            shequchuru.setZhuhuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        //根据字段查询是否有相同数据
        Wrapper<ShequchuruEntity> queryWrapper = new EntityWrapper<ShequchuruEntity>()
            .notIn("id",shequchuru.getId())
            .andNew()
            .eq("zhuhu_id", shequchuru.getZhuhuId())
            .eq("shequchuru_types", shequchuru.getShequchuruTypes())
            .eq("lai_address", shequchuru.getLaiAddress())
            .eq("qu_address", shequchuru.getQuAddress())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ShequchuruEntity shequchuruEntity = shequchuruService.selectOne(queryWrapper);
        if(shequchuruEntity==null){
            //  String role = String.valueOf(request.getSession().getAttribute("role"));
            //  if("".equals(role)){
            //      shequchuru.set
            //  }
            shequchuruService.updateById(shequchuru);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        shequchuruService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        try {
            List<ShequchuruEntity> shequchuruList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            ShequchuruEntity shequchuruEntity = new ShequchuruEntity();
//                            shequchuruEntity.setZhuhuId(Integer.valueOf(data.get(0)));   //住户 要改的
//                            shequchuruEntity.setShequchuruTypes(Integer.valueOf(data.get(0)));   //出入类型 要改的
//                            shequchuruEntity.setShequchuruTiwen(data.get(0));                    //体温 要改的
//                            shequchuruEntity.setLaiAddress(data.get(0));                    //从哪来 要改的
//                            shequchuruEntity.setQuAddress(data.get(0));                    //到哪去 要改的
//                            shequchuruEntity.setShequchuruContent("");//照片
//                            shequchuruEntity.setInsertTime(date);//时间
//                            shequchuruEntity.setCreateTime(date);//时间
                            shequchuruList.add(shequchuruEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        shequchuruService.insertBatch(shequchuruList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }





}

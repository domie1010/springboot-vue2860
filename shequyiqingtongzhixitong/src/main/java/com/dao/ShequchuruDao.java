package com.dao;

import com.entity.ShequchuruEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.view.ShequchuruView;

/**
 * 社区人员出入 Dao 接口
 *
 * @author 
 */
public interface ShequchuruDao extends BaseMapper<ShequchuruEntity> {

   List<ShequchuruView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}

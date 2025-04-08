package com.entity.view;

import com.entity.GonggaoEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 疫情通知
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 */
@TableName("gonggao")
public class GonggaoView extends GonggaoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

		/**
		* 疫情通知类型的值
		*/
		private String gonggaoValue;



	public GonggaoView() {

	}

	public GonggaoView(GonggaoEntity gonggaoEntity) {
		try {
			BeanUtils.copyProperties(this, gonggaoEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



			/**
			* 获取： 疫情通知类型的值
			*/
			public String getGonggaoValue() {
				return gonggaoValue;
			}
			/**
			* 设置： 疫情通知类型的值
			*/
			public void setGonggaoValue(String gonggaoValue) {
				this.gonggaoValue = gonggaoValue;
			}









}

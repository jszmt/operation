package com.wugz.common.utils;

import java.util.Collection;
import java.util.Map;

/***
 * 
  * @ClassName(类名)      : ObjectIsNullUtil
  * @Description(描述)    : 判断对象是否为空
  * @author(作者)         ：吴桂镇
  * @date (开发日期)      ：2018年5月16日 上午10:20:14
  *
 */
public class ObjectIsNullUtil {

	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		}
		if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		}
		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}

}

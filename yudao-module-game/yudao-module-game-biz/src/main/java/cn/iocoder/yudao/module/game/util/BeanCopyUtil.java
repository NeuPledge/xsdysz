package cn.iocoder.yudao.module.game.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class BeanCopyUtil {

    public static <T,E> T copy(E o, Class<T> tClass){
        if(null == o){
            return null;
        }
        T t = null;
        try {
            t = tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(o, t);
        return t;
    }

    public static <T,E> List<T> copyList(List<E> list, Class<T> tClass) {
        if(CollectionUtils.isEmpty(list)){
            return Lists.newArrayList();
        }
        List<T> result = Lists.newArrayList();
        for(Object o : list){
            T t = null;
            try {
                t = tClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(o, t);
            result.add(t);

        }
        return result;
    }

    public static <T,E> List<T> copyList(List<E> list, T t) {
        List<T> result = Lists.newArrayList();
        for(Object o : list){
            BeanUtils.copyProperties(o, t);
            result.add(t);

        }
        return result;
    }



}

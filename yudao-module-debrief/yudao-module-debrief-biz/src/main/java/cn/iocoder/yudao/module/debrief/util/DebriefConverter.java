package cn.iocoder.yudao.module.debrief.util;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO和PO互相转换工具类 .
 *
 * <p>BeanUtils.copyProperties 建议使用spring 提供的
 */
public class DebriefConverter {

    /**
     * toPO.
     */
    public static <T1, T2> T1 convertToPo(T2 dto, Class<T1> clazz) {
        T1 po = null;
        try {
            po = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
            throw new ServiceException("DTO转PO失败");
        }
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    /**
     * toPOList.
     */
    public static <T1, T2> List<T1> convertToPoList(List<T2> dtos, Class<T1> clazz) {
        List<T1> poList = new ArrayList<>();
        try {
            for (T2 dto : dtos) {
                T1 po = clazz.newInstance();
                BeanUtils.copyProperties(dto, po);
                poList.add(po);
            }
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
            throw new ServiceException("List<DTO>转List<PO>失败");
        }
        return poList;
    }

    /**
     * toDTO.
     */
    public static <T1, T2> T2 convertToDto(T1 po, Class<T2> clazz) {
        T2 dto = null;
        try {
            dto = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
            throw new ServiceException("PO转DTO失败");
        }
        BeanUtils.copyProperties(po, dto);
        return dto;
    }

    /**
     * toDtoList.
     */
    public static <T1, T2> List<T2> convertToDtoList(List<T1> pos, Class<T2> clazz) {
        List<T2> dtoList = new ArrayList<>();
        try {
            for (T1 po : pos) {
                T2 dto = clazz.newInstance();
                BeanUtils.copyProperties(po, dto);
                dtoList.add(dto);
            }
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
            throw new ServiceException("List<PO>转List<DTO>失败");
        }
        return dtoList;
    }
}
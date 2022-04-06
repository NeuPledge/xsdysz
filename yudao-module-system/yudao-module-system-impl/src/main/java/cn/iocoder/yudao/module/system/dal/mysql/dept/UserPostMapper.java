package cn.iocoder.yudao.module.system.dal.mysql.dept;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.UserPostDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserPostMapper extends BaseMapperX<UserPostDO> {

    default List<UserPostDO> selectIdList(Long id) {
        return selectList(new LambdaQueryWrapperX<UserPostDO>()
                .eq(UserPostDO::getUserId, id)
                .select(UserPostDO::getPostId));

    }

    default void deleteByUserIdAndPostId(Long userId, Collection<Long> deletePostIds) {
        delete(new LambdaQueryWrapperX<UserPostDO>()
                .eq(UserPostDO::getUserId, userId)
                .in(UserPostDO::getPostId, deletePostIds));
    }

    default List<UserPostDO> selectUserIdByPostIds(Collection<Long> postIds) {
        return selectList(new LambdaQueryWrapperX<UserPostDO>()
                .in(UserPostDO::getPostId, postIds));
    }

    default void deleteByUserId(Long userId){
        delete(Wrappers.lambdaUpdate(UserPostDO.class).eq(UserPostDO::getUserId, userId));
    }
}

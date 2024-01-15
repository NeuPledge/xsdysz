package cn.iocoder.yudao.module.game.service.console.dst.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.module.game.common.consts.BusinessException;
import cn.iocoder.yudao.module.game.common.enums.dst.IniType;
import cn.iocoder.yudao.module.game.common.params.manage.AgentStatusParam;
import cn.iocoder.yudao.module.game.common.params.manage.FilterListRequest;
import cn.iocoder.yudao.module.game.common.params.manage.GameLogParam;
import cn.iocoder.yudao.module.game.common.params.manage.InstanceParam;
import cn.iocoder.yudao.module.game.common.params.manage.InstanceQueryParam;
import cn.iocoder.yudao.module.game.common.response.DstBackupResponse;
import cn.iocoder.yudao.module.game.common.response.HostResourceInfo;
import cn.iocoder.yudao.module.game.common.response.InstanceInfo;
import cn.iocoder.yudao.module.game.common.response.InstanceInfoShort;
import cn.iocoder.yudao.module.game.dal.dataobject.gamepackage.PackageDO;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.module.game.dal.dataobject.snapshot.SnapshotDO;
import cn.iocoder.yudao.module.game.dal.mysql.gamepackage.PackageMapper;
import cn.iocoder.yudao.module.game.dal.mysql.instance.InstanceMapper;
import cn.iocoder.yudao.module.game.dal.mysql.rent.RentMapper;
import cn.iocoder.yudao.module.game.dal.mysql.snapshot.SnapshotMapper;
import cn.iocoder.yudao.module.game.framework.HeaderHolder;
import cn.iocoder.yudao.module.game.service.console.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.dst.ICommonService;
import cn.iocoder.yudao.module.game.service.console.dst.IDstService;
import cn.iocoder.yudao.module.game.service.console.dst.IInstanceService;
import cn.iocoder.yudao.module.game.service.console.dst.timer.HealthCheckService;
import cn.iocoder.yudao.module.game.util.BeanCopyUtil;
import cn.iocoder.yudao.module.game.util.DateFormatUtil;
import cn.iocoder.yudao.module.game.util.DateFormatUtil.Pattern;
import cn.iocoder.yudao.module.game.util.MathUtil;
import cn.iocoder.yudao.module.game.util.Shell;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.module.game.common.params.manage.InstanceQueryParam.State;

@Slf4j
@Service
public class ConsoleInstanceServiceImpl implements IInstanceService {

    @Autowired
    InstanceMapper instanceDao;

    @Autowired
    PackageMapper packageDao;

    @Autowired
    RentMapper userRentDao;

    @Autowired
    SnapshotMapper snapShotDao;

//    @Autowired
//    MBTDicDao dicDao;

    @Resource
    DictDataApi dictDataApi;

    @Autowired
    HealthCheckService healthCheckService;

    @Autowired
    ICommonService commonService;

    @Override
    public PageResult<InstanceInfo> list(InstanceQueryParam param) {
        FilterListRequest filterListRequest = new FilterListRequest(param.getPageNum(), param.getPageSize(), param.getSortBy(), param.getOrder());
//        MBTInstanceEntityExample example = new MBTInstanceEntityExample();
//        Criteria criteria = example.createCriteria();

        LambdaQueryWrapperX<InstanceDO> queryWrapper = new LambdaQueryWrapperX<>();
        if (param.getInstanceId() != null) {
            queryWrapper.eq(InstanceDO::getId, param.getInstanceId());
        }
        if (StringUtils.isNotEmpty(param.getGameVersion())) {
            if (param.getGameVersion().startsWith("!")) {
                queryWrapper.ne(InstanceDO::getGameVersionDst, param.getGameVersion().replace("!", ""));
            } else {
                queryWrapper.eq(InstanceDO::getGameVersionDst, param.getGameVersion());
            }
        }
        if (StringUtils.isNotEmpty(param.getAgentVersion())) {
            if (param.getAgentVersion().startsWith("!")) {
                queryWrapper.ne(InstanceDO::getAgentVersion, param.getAgentVersion().replace("!", ""));
            } else {
                queryWrapper.eq(InstanceDO::getAgentVersion, param.getAgentVersion());
            }
        }
        if (StringUtils.isNotEmpty(param.getResourceAccount())) {
//            criteria.andResourceAccountLike("%" + param.getResourceAccount().trim() + "%");
            queryWrapper.like(InstanceDO::getResourceAccount, param.getResourceAccount().trim());
        }

        if (param.getUserId() != null) {
            RentDO rentDO = userRentDao.selectOne(new LambdaQueryWrapperX<RentDO>().eq(RentDO::getUserId, param.getUserId()));
            if (rentDO!=null){
                queryWrapper.eq(InstanceDO::getId,rentDO.getInstanceId());
            }
        }

        if (param.getRentId() != null) {
            RentDO rent = userRentDao.selectById(param.getRentId());
            queryWrapper.eq(InstanceDO::getId, rent.getInstanceId());
        }

        if (!StringUtils.isEmpty(param.getIp())) {
            queryWrapper.eq(InstanceDO::getIp, param.getIp().trim());
        }
        if (!StringUtils.isEmpty(param.getCompanyCode())) {
            queryWrapper.eq(InstanceDO::getCompanyCode, param.getCompanyCode().trim());
        }
        LocalDateTime now = LocalDateTime.now();
        if (param.getValid() != null && param.getValid()) {
            queryWrapper.ge(InstanceDO::getEndTime, now);
        }
        if (!StringUtils.isEmpty(param.getRegion())) {
//            criteria.andRegionLike("%" + param.getRegion().trim() + "%");
            queryWrapper.like(InstanceDO::getRegion, param.getRegion().trim());
        }
        if (param.getPackageId() != null && param.getPackageId() == 1) {
            // 套餐1, 选出用户自己的机器
//            criteria.andIsOwnEqualTo(false);
            queryWrapper.eq(InstanceDO::getIsOwn, false);
        } else if (param.getPackageId() != null && param.getPackageId() > 1) {
            Long packageId = param.getPackageId();
            PackageDO dstPackage = packageDao.selectById(packageId);

//            criteria.andIsOwnEqualTo(true);
//            criteria.andCpuGreaterThanOrEqualTo(dstPackage.getCpu());
//            criteria.andCpuHzGreaterThanOrEqualTo(dstPackage.getCpuHzMin());
//            criteria.andCpuHzLessThanOrEqualTo(dstPackage.getCpuHzMax());
//
//            criteria.andMemoryGreaterThanOrEqualTo(dstPackage.getMemoryMin());
//            criteria.andMemoryLessThanOrEqualTo(dstPackage.getMemoryMax());


            queryWrapper.eq(InstanceDO::getIsOwn, true)
                    .ge(InstanceDO::getCpu, dstPackage.getCpu())
                    .ge(InstanceDO::getCpuHz, dstPackage.getCpuHzMin())
                    .le(InstanceDO::getCpuHz, dstPackage.getCpuHzMax())
                    .ge(InstanceDO::getMemory, dstPackage.getMemoryMin())
                    .le(InstanceDO::getMemory, dstPackage.getMemoryMax());

        }
        // 有效租约
        List<RentDO> mbtUserRentEntities = userRentDao.selectList(new LambdaQueryWrapperX<RentDO>().gt(RentDO::getEndTime, now));
        if (param.getState() != null) {
            switch (param.getState()) {
                case State.IDLE -> {//无有效租约
                    List<Long> instanceIdsInUse = mbtUserRentEntities.stream().map(RentDO::getInstanceId).collect(Collectors.toList());
                    queryWrapper.notIn(InstanceDO::getId, instanceIdsInUse);
                }
                case State.BIND_RENT -> {//绑定有效租约的机器（可能包含未售的）
                    List<Long> instanceIdsInUse = mbtUserRentEntities.stream().map(RentDO::getInstanceId).collect(Collectors.toList());
                    queryWrapper.in(InstanceDO::getId, instanceIdsInUse);
                }
                case State.SOLD -> {//已售中，客户正在使用的机器
                    List<Long> instanceIdsSold = mbtUserRentEntities.stream().filter(e -> e.getSale() && e.getUserId() > 20).map(RentDO::getInstanceId).collect(Collectors.toList());
                    queryWrapper.in(InstanceDO::getId, instanceIdsSold);
                }
                case State.UNSOLD -> {//未售，无租约或者有租约未售的 + 测试机器（uid < 20）
                    List<Long> instanceIdsSold = mbtUserRentEntities.stream().filter(e -> e.getSale() && e.getUserId() > 20).map(RentDO::getInstanceId).collect(Collectors.toList());
                    queryWrapper.notIn(InstanceDO::getId, instanceIdsSold);
                }
                case State.UNSOLD_7REVOKE -> {// 未售+ 7日内收回的机器 + 测试机器（uid < 20）
                    List<Long> idsSoldAndRevokeGreater5 = mbtUserRentEntities.stream().filter(e -> e.getSale() && e.getUserId() > 20 && e.getEndTime().isAfter(LocalDateTime.now().plusDays(7))).map(RentDO::getInstanceId).collect(Collectors.toList());
                    queryWrapper.notIn(InstanceDO::getId, idsSoldAndRevokeGreater5);
                }
                case State.UNSOLD_3REVOKE -> {// 未售+ 3日内收回的机器 + 测试机器（uid < 20）
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 3);
                    List<Long> idsSoldAndRevokeGreater5 = mbtUserRentEntities.stream().filter(e -> e.getSale() && e.getUserId() > 20 && e.getEndTime().isAfter(LocalDateTime.now().plusDays(3))).map(RentDO::getInstanceId).collect(Collectors.toList());
                    queryWrapper.notIn(InstanceDO::getId, idsSoldAndRevokeGreater5);
                }
                case State.UNSOLD_1REVOKE -> {// 未售+ 1日内收回的机器 + 测试机器（uid < 20）
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 1);
                    List<Long> idsSoldAndRevokeGreater5 = mbtUserRentEntities.stream().filter(e -> e.getSale() && e.getUserId() > 20 && e.getEndTime().isAfter(LocalDateTime.now().plusDays(1))).map(RentDO::getInstanceId).collect(Collectors.toList());
                    queryWrapper.notIn(InstanceDO::getId, idsSoldAndRevokeGreater5);
                }
                case State.VALID10DAY -> {
                    queryWrapper.ge(InstanceDO::getEndTime, LocalDateTime.now()).le(InstanceDO::getEndTime, LocalDateTime.now().plusDays(10));
                }
                case State.VALID20DAY -> {
                    queryWrapper.ge(InstanceDO::getEndTime, LocalDateTime.now()).le(InstanceDO::getEndTime, LocalDateTime.now().plusDays(20));
                }
                case State.VALID30DAY -> {
                    queryWrapper.ge(InstanceDO::getEndTime, LocalDateTime.now()).le(InstanceDO::getEndTime, LocalDateTime.now().plusDays(30));
                }
                case State.DiskUsed50 -> {
                    queryWrapper.ge(InstanceDO::getDiskUsedPercent, 50);
                }
                case State.DiskUsed80 -> {
                    queryWrapper.ge(InstanceDO::getDiskUsedPercent, 80);
                }
                case State.ALL -> {
                    //do nothing
                }
            }
        }

//        Page page = filterListRequest.startPage();
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(param.getPageNum());
        pageParam.setPageSize(param.getPageSize());

        IPage<InstanceDO> mpPage = MyBatisUtils.buildPage(pageParam);
        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(mpPage, queryWrapper);

        if (param.getSoldToday() != null && param.getSoldToday()) {
            if (param.getState() != null && (State.UNSOLD.equals(param.getState()) || State.UNSOLD_1REVOKE.equals(param.getState()) || State.UNSOLD_3REVOKE.equals(param.getState()) || State.UNSOLD_7REVOKE.equals(param.getState()))) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(year, month, day, 0, 0, 0);

                List<Long> todayIds = userRentDao.selectList(
                        new LambdaQueryWrapperX<RentDO>()
                                .eq(RentDO::getSale, true)
                                .gt(RentDO::getUserId, 20)
                                .ge(RentDO::getStartTime, LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault()))).stream().map(RentDO::getInstanceId).collect(Collectors.toList());

                if (!todayIds.isEmpty()) {
                    List<InstanceDO> instanceEntities = instanceDao.selectList(new LambdaQueryWrapperX<InstanceDO>().in(InstanceDO::getId, todayIds));
                    mbtInstanceEntities.addAll(instanceEntities);
                }
            }

        }

        // 获取升序排列的套餐id
        List<PackageDO> mbtDstPackageEntities = packageDao.selectList();

        // 获取过去10天内过期的租约
        List<RentDO> rentsExpiredWithinTenDays = userRentDao.selectList(
                new LambdaQueryWrapperX<RentDO>()
                        .between(RentDO::getEndTime, LocalDateTime.now().plusDays(-10), now)
                        .orderByDesc(RentDO::getEndTime));

        List<InstanceInfo> instanceInfos = new ArrayList<>();
        for (InstanceDO instance : mbtInstanceEntities) {
            InstanceInfo instanceInfo = new InstanceInfo(instance);
            BeanUtils.copyProperties(instance, instanceInfo);
            Optional<RentDO> first = mbtUserRentEntities.stream().filter(e -> Objects.equals(e.getInstanceId(), instance.getId())).findFirst();
            if (first.isPresent()) {
                RentDO rentInfo = first.get();
                instanceInfo.setUserId(rentInfo.getUserId());
                instanceInfo.setRentId(rentInfo.getId());
                instanceInfo.setRentStartTime(rentInfo.getStartTime());
                instanceInfo.setRentEndTime(rentInfo.getEndTime());
                instanceInfo.setSale(rentInfo.getSale());
            } else {
                Optional<RentDO> expiredRentFirst = rentsExpiredWithinTenDays.stream().filter(e -> Objects.equals(e.getInstanceId(), instance.getId())).findFirst();
                if (expiredRentFirst.isPresent()) {
                    RentDO rentInfo = expiredRentFirst.get();
                    instanceInfo.setUserId(rentInfo.getUserId());
                    instanceInfo.setRentId(rentInfo.getId());
                    instanceInfo.setRentStartTime(rentInfo.getStartTime());
                    instanceInfo.setRentEndTime(rentInfo.getEndTime());
                    instanceInfo.setSale(Boolean.FALSE);
                }
            }
            List<Long> suitablePackages = getInstanceSuitablePackages(instance, mbtDstPackageEntities);
            instanceInfo.setSuitablePackages(suitablePackages);
            instanceInfos.add(instanceInfo);
        }
        for (InstanceInfo instanceInfo : instanceInfos) {
            try {
                LocalDateTime nowTime = LocalDateTime.now();
                LocalDateTime endTime = instanceInfo.getEndTime();
                LocalDate endDate = endTime.toLocalDate();

                Duration duration = Duration.between(nowTime, endTime);
                long leftDays = duration.toDays();
                Period p = Period.between(LocalDate.now(), endDate);
                int months = p.getMonths();
                int days = p.getDays();
                String msg = leftDays + "天";
                if (months >= 1) {
                    msg = msg + " (" + months + "月" + days + "天" + ")";
                }

                instanceInfo.setInstanceLeftDays(msg);
                if (instanceInfo.getCpu() == 2 && instanceInfo.getMemory() == 2) {
                    // 9元/月
                    double price9 = leftDays * 9.0 / 31;
                    // 8元/月
                    double price8 = leftDays * 11.25 / 31;
                    String price = "9元/月 " + MathUtil.roundUp(price9, 1) + "; " + "11.25元/月 " + MathUtil.roundUp(price8, 1);
                    instanceInfo.setInstanceSafePrice(price);
                } else if (instanceInfo.getCpu() == 2 && instanceInfo.getMemory() == 4) {
                    // 13元/月
                    double price13 = leftDays * 13.0 / 31;
                    // 15.5元/月
                    double price12 = leftDays * 15.5 / 31;
                    String price = "13元/月 " + MathUtil.roundUp(price13, 1) + "; " + "15.5元/月 " + MathUtil.roundUp(price12, 1);
                    instanceInfo.setInstanceSafePrice(price);
                }
            } catch (Exception exception) {
                log.error("机器ID:{}计算转手价格出错.", instanceInfo.getId());
            }
        }
//        PageInfo pageInfo = new PageInfo(page);
//        pageInfo.setList(instanceInfos);

        return new PageResult<>(instanceInfos, mpPage.getTotal());
    }

    /**
     * 套餐列表是已按照升序排好，在已经匹配到至少一个适用的套餐后，一旦后面的循环中出现不适用的套餐，直接跳出循环。避免浪费计算资源
     *
     * @param instance    机器实例
     * @param dstPackages 套餐列表，已按照套餐id 升序排序的
     * @return 当前机器适用的套餐表
     */
    private List<Long> getInstanceSuitablePackages(InstanceDO instance, List<PackageDO> dstPackages) {
        List<Long> ids = new ArrayList<>();
        if (!instance.getIsOwn()) {
            ids.add(1L);
            return ids;
        }
        boolean foundSuitablePackage = false;
        for (int i = 1; i < dstPackages.size(); i++) {
            PackageDO dstPkg = dstPackages.get(i);
            if (isInstanceSuitableWithPackage(instance, dstPkg)) {
                foundSuitablePackage = true;
                ids.add(dstPkg.getId());
            } else {
                //套餐列表是已按照升序排好，在已经匹配到至少一个适用的套餐后，一旦后面的循环中出现不适用的套餐，直接跳出循环。避免浪费计算资源
                if (foundSuitablePackage) {
                    break;
                }
            }
        }
        return ids;
    }

    private boolean isInstanceSuitableWithPackage(InstanceDO instance, PackageDO dstPkg) {
        if (instance.getCpuHz() == null ||
                instance.getMemory() == null ||
                instance.getCpu() == null) {
            return false;
        }
        return instance.getCpu() >= dstPkg.getCpu() &&
                instance.getCpuHz() >= dstPkg.getCpuHzMin() &&
                instance.getCpuHz() <= dstPkg.getCpuHzMax() &&
                instance.getMemory() >= dstPkg.getMemoryMin() &&
                instance.getMemory() <= dstPkg.getMemoryMax();

    }

    /**
     * 列出所有机器(用于绑定租约的机器快速选项).
     * 1. 空闲机器, 没有租约关联.
     * 2. 剩余可用时间超过7天(到期之前, 得我们管理员自己进行静默迁移了, ip会变 实在没办法.).
     * 3. 按创建时间排序. 给出适合当前套餐的标记.
     */
    @Override
    public List<InstanceInfoShort> listInstancesForAutodcomplete(Long packageId) {
        List<RentDO> mbtUserRentEntities = userRentDao.selectList(new LambdaQueryWrapperX<RentDO>().gt(RentDO::getEndTime, LocalDateTime.now()));
        List<Long> instanceIdsInUse = mbtUserRentEntities.stream().filter(e -> e.getSale() && e.getUserId() > 20).map(RentDO::getInstanceId).collect(Collectors.toList());
        List<Long> instanceIdsTest = mbtUserRentEntities.stream().filter(e -> e.getSale() && e.getUserId() <= 20).map(RentDO::getInstanceId).collect(Collectors.toList());


        // 只列出剩余有效期超过7天的机器.
        LambdaQueryWrapper<InstanceDO> queryWrapper = new LambdaQueryWrapperX<InstanceDO>()
                .gt(InstanceDO::getEndTime, LocalDateTime.now().plusDays(7))
                .ne(InstanceDO::getSale, true);
        if (instanceIdsInUse.size() > 0) {
            queryWrapper.notIn(InstanceDO::getId, instanceIdsInUse);
        }


        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(queryWrapper);

        PackageDO packageEntity = null;
        List<InstanceInfoShort> instanceInfoShorts = new ArrayList<>();
        if (packageId == null) {
            for (InstanceDO instance : mbtInstanceEntities) {
                InstanceInfoShort instanceInfoShort = BeanCopyUtil.copy(instance, InstanceInfoShort.class);
                if (instanceIdsTest.contains(instance.getId())) {
                    instanceInfoShort.setIsBindForTest(true);
                }
                instanceInfoShorts.add(instanceInfoShort);
            }
        } else {
            if (packageId != 1) {
                packageEntity = packageDao.selectById(packageId);
                for (InstanceDO instance : mbtInstanceEntities) {
                    InstanceInfoShort instanceInfoShort = BeanCopyUtil.copy(instance, InstanceInfoShort.class);
                    if (packageEntity != null) {
                        if (isInstanceSuitableWithPackage(instance, packageEntity)) {
                            instanceInfoShort.setIsPackageRecommend(true);
                        }
                    }
                    if (instanceIdsTest.contains(instance.getId())) {
                        instanceInfoShort.setIsBindForTest(true);
                    }
                    instanceInfoShorts.add(instanceInfoShort);
                }
            } else {
                for (InstanceDO instance : mbtInstanceEntities) {
                    if (instance.getIsOwn() == false) { // 如果 isOwn=false, 那么该机器是用户自己的机器
                        InstanceInfoShort instanceInfoShort = BeanCopyUtil.copy(instance, InstanceInfoShort.class);
                        instanceInfoShorts.add(instanceInfoShort);
                    }
                }
            }
        }

        return instanceInfoShorts;
    }

    /**
     * 新增机器实例.
     **/
    @Override
    public void createInstance(InstanceParam param) {
        InstanceDO record = new InstanceDO();
        record.setCompanyCode(param.getCompanyCode());
        record.setResourceAccount(param.getResourceAccount());
        record.setResourceAccountPassword(param.getResourceAccountPassword());
        record.setIp(param.getIp());
        record.setRegion(param.getRegion());
        record.setMemory(param.getMemory());
        record.setWayOfBuy(param.getWayOfBuy());
        record.setIsOwn(param.getIsOwn());
        record.setCpu(param.getCpu());
        record.setCpuHz(param.getCpuHz());
        record.setPrice(param.getPrice());
        record.setStartTime(param.getStartTime());
        record.setEndTime(param.getEndTime());
        record.setSshPort(param.getSshPort());
        record.setSshPassword(param.getSshPassword());
        record.setAgentPort(param.getDstAgentPort());
        record.setAgentWsPort(param.getDstAgentWsPort());
        record.setPortDstMaster(param.getDstMasterPort());
        record.setPortDstCluster(param.getDstClusterPort());
        record.setPortDstCaves(param.getDstCavesPort());
        record.setNotes(param.getNotes());
        instanceDao.insert(record);
    }

    /**
     * 更新机器实例详情.
     */
    @Override
    public void updateInstance(InstanceParam param) {

        InstanceDO record = new InstanceDO();
        record.setId(param.getId());
        record.setCompanyCode(param.getCompanyCode());
        record.setResourceAccount(param.getResourceAccount());
        record.setResourceAccountPassword(param.getResourceAccountPassword());
        record.setIp(param.getIp());
        record.setRegion(param.getRegion());
        record.setMemory(param.getMemory());
        record.setWayOfBuy(param.getWayOfBuy());
        record.setIsOwn(param.getIsOwn());
        record.setCpu(param.getCpu());
        record.setCpuHz(param.getCpuHz());
        record.setPrice(param.getPrice());
        record.setStartTime(param.getStartTime());
        record.setEndTime(param.getEndTime());
        record.setSshPort(param.getSshPort());
        record.setSshPassword(param.getSshPassword());
        record.setAgentPort(param.getDstAgentPort());
        record.setAgentWsPort(param.getDstAgentWsPort());
        record.setPortDstMaster(param.getDstMasterPort());
        record.setPortDstCluster(param.getDstClusterPort());
        record.setPortDstCaves(param.getDstCavesPort());
        record.setNotes(param.getNotes());

        instanceDao.updateById(record);
    }

    /**
     * 管理Agent运行状态.
     */
    @Override
    public String operateAgentStatus(AgentStatusParam param) {
        Long instanceId = param.getInstanceId();
        InstanceDO instance = instanceDao.selectById(instanceId);
        String response = "";
        switch (param.getStatus()) {
            case install -> response = installAgent(instance);
            case update -> updateAgent(instance);
            case query_dst_agent_version -> queryDstAgentVersion(instance);
            case query_dst_version -> response = queryDstGameVersion(instance);
            case addpublickey -> response = appendPubKey(instance);
            case clearInstance -> response = clearInstance(instance);
            case swap_on -> response = swapOn(instance);
            case stop_firewalld -> response = stopFirewalld(instance);
            case fetch_cpu_hz -> response = cpuHz(instance);
            case start -> response = startAgent(instance);
            case forceStartAgent -> response = commonService.forceStartAgent(instance);
            case stop -> response = stopAgent(instance);
            case set_default_token -> resetDefaultToken(instance);
            case wipe_screen -> wipeScreen(instance);
            case markSale -> markSale(instance);
        }
        return response;
    }

    private void markSale(InstanceDO instance) {

        instance.setSale(true);
        instanceDao.updateById(instance);
    }


    private String cpuHz(InstanceDO instance) {
        String command = "grep 'cpu MHz' /proc/cpuinfo | head -1 | awk -F ':' '{print $2}'";
        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey(command);
        if (runLog == null || runLog.length() == 0 || runLog.trim().length() == 0) {
            // do nothing
            return "读取失败, 返回的内容貌似为空:" + runLog;
        } else {
            try {

                String cpuMHz = runLog.trim();
                double cpuMHzDouble = Double.parseDouble(cpuMHz);
                Double cpuHz = MathUtil.roundUp(cpuMHzDouble / 1000, 1);
                instance.setCpuHz(cpuHz);
                instanceDao.updateById(instance);
                return "解析成功, 原始字符串:" + runLog + ", 已入库cpuHz:" + cpuHz;
            } catch (Exception exception) {
                // do nothing
                return runLog + "解析MHz字符串失败. " + exception.getMessage();
            }
        }
    }

    private String swapOn(InstanceDO instance) {
        String command = "dd if=/dev/zero of=/mnt/swap bs=1M count=4096 && echo vm.swappiness=60 >> /etc/sysctl.conf && sysctl -w vm.swappiness=60 && chmod 600 /mnt/swap && mkswap /mnt/swap && swapon /mnt/swap && echo '/mnt/swap   swap  swap  defaults  0  0' >> /etc/fstab";

        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey(command);
        return runLog;
    }

    // 关闭防火墙
    private String stopFirewalld(InstanceDO instance) {
        // systemctl status firewalld.service
        String command = "systemctl stop firewalld.service && systemctl disable firewalld.service";
        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey(command);
        return runLog;
    }

    @Override
    public Integer nDaysExpiredInstance(Integer days) {

        LocalDateTime calendar = LocalDateTime.now().plusDays(days);
        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(new LambdaQueryWrapperX<InstanceDO>().ge(InstanceDO::getEndTime, LocalDateTime.now()).lt(InstanceDO::getEndTime, calendar));
        return mbtInstanceEntities.size();
    }

    @Override
    public InstanceDO getInstance(Long id) {
        InstanceDO instance = instanceDao.selectById(id);
        if (instance == null) {
            throw new BusinessException("查无此机器.");
        }
        return instance;
    }

    /**
     * 删除机器实例.
     */
    @Override
    public void deleteInstance(Long instanceId) {
        RentDO rentEntity = userRentDao.selectOne(new LambdaQueryWrapperX<RentDO>().eq(RentDO::getInstanceId, instanceId).gt(RentDO::getEndTime, new Date()));

        if (rentEntity != null) {
            throw new BusinessException("该机器存在有效租约, 暂无法删除, 租约id:" + rentEntity.getId());
        }
        InstanceDO instance = instanceDao.selectById(instanceId);

        String snapshot = JSON.toJSONString(instance);
        SnapshotDO snapShotEntity = new SnapshotDO();
        snapShotEntity.setAction("删除机器");
        snapShotEntity.setSnapshot(snapshot);
        snapShotDao.insert(snapShotEntity);

        instanceDao.deleteById(instanceId);
    }

    /**
     * 更新所有机器的Agent到最新版本.
     */
    @Override
    public void updateAllDstAgent() {
        DictDataRespDTO mbtDic = dictDataApi.parseDictData("dst_agent_latest_version", "dst_agent_latest_version");
        String latestVersion = mbtDic.getValue();
        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(new LambdaQueryWrapperX<InstanceDO>()
                .eq(InstanceDO::getAgentStatus, "up")
                .gt(InstanceDO::getEndTime, new Date())
                .ne(InstanceDO::getAgentVersion, latestVersion.trim()));

        ExecutorService executor = new ThreadPoolExecutor(30, 50, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        CountDownLatch countDownLatch = new CountDownLatch(mbtInstanceEntities.size());
        for (InstanceDO mbtInstanceEntity : mbtInstanceEntities) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mbtInstanceEntity.getCompanyCode() != null && mbtInstanceEntity.getCompanyCode().contains("h1.")) {
                            return;
                        }
                        DstClient.getInstance().updateAgent(mbtInstanceEntity);
                        log.info("机器ID:{}, IP:{}, 更新云控完成.", mbtInstanceEntity.getId(), mbtInstanceEntity.getIp());
                    } catch (Exception ignore) {
                        log.error("机器ID:{}, IP:{}, 更新云控出现错误.", mbtInstanceEntity.getId(), mbtInstanceEntity.getIp());
                    } finally {
                        countDownLatch.countDown();
                        log.info("剩余:" + String.valueOf(countDownLatch.getCount()));
                    }
                }
            });
        }
        try {
            countDownLatch.await(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAllDstAgentToSpecifiedVersion(String version) {
        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(new LambdaQueryWrapperX<InstanceDO>().eq(InstanceDO::getAgentStatus, "up").ne(InstanceDO::getAgentVersion, version.trim()).gt(InstanceDO::getEndTime, new Date()));
        for (InstanceDO mbtInstanceEntity : mbtInstanceEntities) {
            try {
                if (mbtInstanceEntity.getCompanyCode() != null && mbtInstanceEntity.getCompanyCode().contains("h1.")) {
                    continue;
                }
                DstClient.getInstance().updateAgent(mbtInstanceEntity);
                log.info("机器ID:{}, IP:{}, 更新云控完成.", mbtInstanceEntity.getId(), mbtInstanceEntity.getIp());
            } catch (Exception exception) {
                log.error("机器ID:{}, IP:{}, 更新云控出现错误.", mbtInstanceEntity.getId(), mbtInstanceEntity.getIp());
            }
        }
    }

    @Override
    public void updateAllDstGame() {
        ExecutorService executor = new ThreadPoolExecutor(30, 50, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

        DictDataRespDTO dictDataRespDTO = dictDataApi.parseDictData("dst_latest_version", "dst_latest_version");
        String latestVersion = dictDataRespDTO.getValue();

        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(new LambdaQueryWrapperX<InstanceDO>()
                .and(e -> e.eq(InstanceDO::getAgentStatus, "up")
                        .gt(InstanceDO::getEndTime, new Date())
                        .ne(InstanceDO::getGameVersionDst, latestVersion))
                .or(e -> e.eq(InstanceDO::getAgentStatus, "up")
                        .gt(InstanceDO::getEndTime, new Date())
                        .isNull(InstanceDO::getGameVersionDst))
                .orderByDesc(InstanceDO::getRentEndTime)
        );

        CountDownLatch countDownLatch = new CountDownLatch(mbtInstanceEntities.size());
        for (InstanceDO mbtInstanceEntity : mbtInstanceEntities) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        DstClient.getInstance().updateGame(mbtInstanceEntity);
                        log.info("机器ID:{}, IP:{}, 更新游戏完成.", mbtInstanceEntity.getId(), mbtInstanceEntity.getIp());

                        Thread.sleep(3000);
                        String version = getDstGameVersion(mbtInstanceEntity);
                        if (StringUtils.isNotEmpty(version) && StringUtils.isNotEmpty(version.trim())) {
//                            InstanceDO record = new InstanceDO();
//                            record.setId(mbtInstanceEntity.getId());
                            mbtInstanceEntity.setGameVersionDst(version);
                            instanceDao.updateById(mbtInstanceEntity);
                        }
                    } catch (Exception ignore) {
                    } finally {
                        countDownLatch.countDown();
                        log.info("剩余:" + String.valueOf(countDownLatch.getCount()));
                    }
                }
            });
        }
        try {
            countDownLatch.await(2, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MM/dd");

    /**
     * 清理机器, 一般用于上个用户租约到期之后回收机器. 包含(存档/日志/多余的模组/).
     * 1. 有租约时不可清理, 可以先结束租约(测试账号允许强制还原).
     *
     * @param instance 机器id.
     */
    public String clearInstance(InstanceDO instance) {
        RentDO mbtUserRentEntity = userRentDao.selectOne(new LambdaQueryWrapperX<RentDO>().eq(RentDO::getInstanceId, instance.getId()).gt(RentDO::getEndTime, new Date()));

        if (mbtUserRentEntity != null && mbtUserRentEntity.getUserId() > 20) {
            throw new BusinessException("该机器存在有效租约: " + mbtUserRentEntity.getId() + ", 禁止清理操作. 确需清理, 请先结束租约.");
        }
        String runLog = "";
        try {
            // 1. 确保agent活着
            startAgent(instance);
            runLog += "1. 启动agent确保其存活\n";
            // 2. 调api停止运行当前世界
            DstClient client = DstClient.getInstance();
            client.stopMaster(instance);
            client.stopCaves(instance);
            runLog += "2. 关闭地面和洞穴\n";
            // 3. 清理模组
            Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort());
            shell.execCommandPrvKey("echo > /root/dstserver/mods/dedicated_server_mods_setup.lua && cd /root/dstserver/mods/ && rm -rf workshop-* && cd /root/dstserver/steamapps/workshop/content/ && rm -rf 322330/*");
            runLog += "3. 清理模组文件\n";
            // 4. 备份上一个人的存档
            String dateTime = DateFormatUtil.formatDateTime(new Date(), Pattern._4);
            String backupDir = "/root/dst-backup/" + dateTime;
            String command = "mkdir -p " + backupDir + " && cd /root/.klei/DoNotStarveTogether && cp -r ./* " + backupDir;
            shell.execCommandPrvKey(command);
            runLog += "4. 备份上一个租约用户的存档到" + backupDir + "\n";
            // 5. 清理存档历史
            // 6. 重新下载, 解压存档备份
            String commandClearAndDownload = "cd /root/.klei/DoNotStarveTogether && rm -rf ./* && wget http://cdn.panda.simuhunluo.top/dst/Cluster.zip && unzip Cluster.zip";
            shell.execCommandPrvKey(commandClearAndDownload);
            runLog += "5&6. 清理存档历史并重新下载最新初始存档\n";
            // 7. 启动世界
//            client.startMaster(instance);
//            client.startCaves(instance);
//            runLog += "7. 重启地面和洞穴\n操作完成!";

//            InstanceDO record = new InstanceDO();
            String notes = instance.getNotes();
            String clearNotes = "已还原" + mSimpleDateFormat.format(new Date());
            if (notes == null) {
                instance.setNotes(clearNotes);
            } else if (notes.contains("需还原")) {
                instance.setNotes(notes.replaceAll("需还原", clearNotes));
            } else {
                instance.setNotes(clearNotes + "\n" + notes);
            }
            instanceDao.updateById(instance);
            return runLog;
        } catch (Exception exception) {
            runLog += "操作失败";
            return runLog;
        }
    }

    public void checkClusterExistAndDownload(InstanceDO instance, RentDO userRent) {
        Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort());
        String lsClusterFilesCommand = "mkdir -p /root/.klei/DoNotStarveTogether/" + userRent.getClusterName() + " && ls /root/.klei/DoNotStarveTogether/" + userRent.getClusterName();
        String lsClusterFiles = shell.execCommandPrvKey(lsClusterFilesCommand);
        log.info(lsClusterFiles);
        if (lsClusterFiles.contains("cluster.ini")) {
            // 存档已经存在，do nothing
            return;
        } else if (StringUtils.isEmpty(lsClusterFiles)) {
            // 空的，需要下载存档
            String downloadCommand = "mkdir -p /root/rents/dst/" + userRent.getId() + " && cd /root/rents/dst/" + userRent.getId() + " && rm -rf Cluster.zip && wget http://cdn.panda.simuhunluo.top/dst/Cluster.zip";
            log.info("downloadCommand: " + downloadCommand);
            shell.execCommandPrvKey(downloadCommand);
            // 解压存档
            DstClient client = DstClient.getInstance();
            HeaderHolder.setCurrentUserRent(userRent);
            client.unzipArchive(instance, "Cluster.zip");
        }
    }

    public static void main(String[] args) {
        ConsoleInstanceServiceImpl instanceService = new ConsoleInstanceServiceImpl();
        InstanceDO instance = new InstanceDO();
        instance.setIp("h1.pandacloudgame.com");
        instance.setSshPort(20001);
        RentDO userRent = new RentDO();
        userRent.setClusterName("Cluster_20001");
        userRent.setId(187L);
        instanceService.checkClusterExistAndDownload(instance, userRent);
    }

    // 查询dst agent 版本
    private String queryDstGameVersion(InstanceDO instance) {
        String dstGameVersion = getDstGameVersion(instance);
        if (StringUtils.isNotEmpty(dstGameVersion) && StringUtils.isNotEmpty(dstGameVersion.trim())) {
            instance.setGameVersionDst(dstGameVersion);
            instanceDao.updateById(instance);
        }
        return dstGameVersion;
    }

    private void queryDstAgentVersion(InstanceDO instance) {
        healthCheckService.runSingle(instance.getId());
    }

    // Agent自更新
    private void updateAgent(InstanceDO instance) {
        if (instance.getCompanyCode() != null && instance.getCompanyCode().contains("h1.")) {
            throw new BusinessException("自己的虚拟机器不可以更新云控");
        }
        DstClient.getInstance().updateAgent(instance);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        queryDstAgentVersion(instance);
    }

    // 在机器上安装agent.
    public String installAgent(InstanceDO instance) {
        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey("curl -s https://pandacloudgame-1251798209.cos.ap-nanjing.myqcloud.com/dst/update_dst_agent.sh | bash");
        return runLog;
    }

    // 往机器分发公钥, 验证方式是走密码(固定统一的密码, 后续看有没有必要每台机器都随机密码), 所以这个接口只能是我们自己的机器用.
    public String appendPubKey(InstanceDO instance) {
        String cmd = "mkdir -p /root/.ssh/ && touch /root/.ssh/authorized_keys && echo 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQCmt/lNgX8TJKbu1NO9dSOVs6+5rGhMBDyfbvaJlof5rAivv6V4G7MXXHK7nwgrmoITV94BfJNT57FjEz9rSKWMev04xKCzZ+MZcJd2y2Cl+F/axZzM48SS1jPyjrdGnvlFiN6L+Cd2Rzrh75/Y/A/BCmgR90k/lzzDeo1Hrq/XhEF8maRy9c1uLawUfe/sHNGm9FY0Nlh0SC0yb8riCy1CXNr9Ci3LYaNHJnsWspC+4NhzzDG2Cdr2gPt15Rtld4GbjFwt+oRoqJpJXDjxMLYIxyWupVrE1B1rmzSXs208ig5f94+7SEYY47wQ9bknujQqItUbNkQRH1S7JMA9agyv3MDLA26yx5Yw23rxKL8OK4Ia+PKe/8WKN9dpA0n2AdZwKtN5DFTLYL/qBBtwDiNrxum++SGRLfTBAO/uBDk2kyTVe8x/M7cppriFxZeK0KfImfSwtAcSy6saQY7Hlnn6dVwQcwJcud1qi4OfE2WmOVaW8iMuPuQxMwM3z5Nc8HU= pandacloudgame' >> /root/.ssh/authorized_keys && chmod 700 /root/.ssh && chmod 600 /root/.ssh/authorized_keys";
        Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort(), instance.getSshPassword());
        String runLog = shell.execCommand(cmd);
        return runLog;
    }

    // 强制结束该机器上的screen进程.
    public String forceCloseScreenProcess(InstanceDO instance) {
        String cmd = "screen -dr master_screen -X -S quit ; screen -dr caves_screen -X -S quit";
        Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort(), instance.getSshPassword());
        String runLog = shell.execCommand(cmd);
        return runLog;
    }

    // 启动agent, 适用于agent无故挂掉, 需要通过shell拉起的情况.
    public String startAgent(InstanceDO instance) {
        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey("cd /root/agents && ./dstagent start -d");
        return runLog;
    }


    // 结束agent
    public String stopAgent(InstanceDO instance) {
        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey("cd /root/agents && ./dstagent stop");
        return runLog;
    }

    @Override
    public HostResourceInfo getResourceInfo(Long instanceId) {
        InstanceDO instance = instanceDao.selectById(instanceId);
        String ip = instance.getIp();
        return DstClient.getInstance().getHostResourceInfo(instance);
    }

    @Override
    public String getDstGameVersion(InstanceDO instance) {
        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey("cat /root/dstserver/version.txt");
        if (StringUtils.isEmpty(runLog)) {
            return "";
        }
        runLog = runLog.trim();
        return runLog;
    }

    public void getAllInstancesDstGameVersion() {
        ExecutorService executor = new ThreadPoolExecutor(10, 20, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

        DictDataRespDTO dictDataRespDTO = dictDataApi.parseDictData("dst_latest_version", "dst_latest_version");
        String latestVersion = dictDataRespDTO.getValue();

        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(new LambdaQueryWrapperX<InstanceDO>()
                .and(e -> e.eq(InstanceDO::getAgentStatus, "up")
                        .gt(InstanceDO::getEndTime, new Date())
                        .ne(InstanceDO::getGameVersionDst, latestVersion))
                .or(e -> e.eq(InstanceDO::getAgentStatus, "up")
                        .gt(InstanceDO::getEndTime, new Date())
                        .isNull(InstanceDO::getGameVersionDst))
        );

        CountDownLatch countDownLatch = new CountDownLatch(mbtInstanceEntities.size());
        for (InstanceDO mbtInstanceEntity : mbtInstanceEntities) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String version = getDstGameVersion(mbtInstanceEntity);
                        if (StringUtils.isNotEmpty(version) && StringUtils.isNotEmpty(version.trim())) {
//                            InstanceDO record = new InstanceDO();
                            mbtInstanceEntity.setGameVersionDst(version);
                            instanceDao.updateById(mbtInstanceEntity);
                        }
                    } catch (Exception ignore) {
                    } finally {
                        countDownLatch.countDown();
                    }

                }
            });
        }
        try {
            countDownLatch.await(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkDstInstallation(Long instanceId) {
        InstanceDO instance = instanceDao.selectById(instanceId);

        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey("if [ -f /root/install.log ];then cat /root/install.log; else echo '未找到install.log'; fi");
        if (StringUtils.isEmpty(runLog)) {
            runLog = "";
        }
//        if (runLog.contains("未找到install.log")) {
//
//        } else {
//
//        }
        runLog += "\n目录大小:\n";
        runLog += shell.execCommandPrvKey("du -sh /root/*");
        return runLog;
    }

    @Override
    public String gameLog(GameLogParam gameLogParam) {
        InstanceDO instance = instanceDao.selectById(gameLogParam.getInstanceId());
        String logContent = DstClient.getInstance().getLog(instance, gameLogParam.getLogType(), gameLogParam.getLine());
        log.info("gameLogxxxxx");
        log.info(logContent);
        return logContent;
    }

    @Override
    public List<DstBackupResponse> listBackups(Long instanceId) {
        InstanceDO instance = instanceDao.selectById(instanceId);
        List<DstBackupResponse> responses = new ArrayList<>();
        Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey("du -sh /root/dst-backup/*");
        if (StringUtils.isEmpty(runLog)) {
            return responses;
        }
        /**
         * 934M	/root/dst-backup/20221211215034
         * 981M	/root/dst-backup/20230115211936
         * 981M	/root/dst-backup/20230115211947
         */
        String dirStr = runLog.trim();
        String[] dirs = dirStr.split("\n");
        for (String dirItem : dirs) {
            String[] dirItemSplit = dirItem.split("\t");
            String dirSize = dirItemSplit[0];
            String dirPath = dirItemSplit[1];
            String[] dirPathSplit = dirPath.split("/");
            String dirCreateTime = dirPathSplit[dirPathSplit.length - 1];
            String dirCreateTimeAfterFormat = DateFormatUtil.formatDateTime(DateFormatUtil.parseDateTime(dirCreateTime, Pattern._4), Pattern._1);

            DstBackupResponse response = new DstBackupResponse();
            response.setDirSize(dirSize);
            response.setDirPath(dirPath);
            response.setCreateTime(dirCreateTimeAfterFormat);
            responses.add(response);
        }
//        System.out.println(JSON.toJSONString(responses));
        return responses;
    }

    /**
     * 存档迁移.
     * 需要在原机器上安装一个sshpass yum包. 借助这个工具来实现scp功能.
     *
     * @param newInstanceId 新机器id.
     * @param oldInstanceId 原机器id.
     * @param path          原机器的存档路径: /root/dst-backup/20221211215034 或者 /root/.klei/DoNotStarveTogether
     * @param allFiles      默认false.
     */
    @Override
    public void archivesMigration(Long newInstanceId, Long oldInstanceId, String path, Boolean allFiles) {
        log.info("跨机器迁移");

        InstanceDO newInstance = instanceDao.selectById(newInstanceId);
        InstanceDO oldInstance = instanceDao.selectById(oldInstanceId);
        // yum install -y sshpass
        // sshpass -p "mima" ssh -p 22 root@localhost
        // sshpass -p "mima" scp -P 22 -r /root/dst-backup/20221211215034/Cluster_1/* root@localhost:/root/.klei/DoNotStarveTogether/Cluster_1/
        // sshpass -p "mima" scp -P 22 -o StrictHostKeyChecking=no -r /root/.klei/DoNotStarveTogether/Cluster_1/* root@119.96.194.176:/root/.klei/DoNotStarveTogether/Cluster_1/

        // 1. 先删除新机器的Cluster_1下的内容.
        Shell newShell = new Shell(newInstance.getIp(), "root", newInstance.getSshPort());
        newShell.execCommandPrvKey("rm -rf /root/.klei/DoNotStarveTogether/Cluster_1/*");
        log.info("1. 删除新机器的Cluster_1下的内容");

        // 2. 老机器安装sshpass.
        Shell shell = new Shell(oldInstance.getIp(), "root", oldInstance.getSshPort());
        String installLog = shell.execCommandPrvKey("yum install -y sshpass");
        log.info("2. 老机器yum安装sshpass:" + installLog);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 3. 迁移.
        String migrationPath = "";
        String targetPath = "";
        if (StringUtils.isEmpty(path)) {
            path = "/root/.klei/DoNotStarveTogether";
        }
        if (allFiles) {
            migrationPath = path + "/*";
            targetPath = "/root/.klei/DoNotStarveTogether/";
        } else {
            migrationPath = path + "/Cluster_1/*";
            targetPath = "/root/.klei/DoNotStarveTogether/Cluster_1/";
        }

        String scpCommand = String.format("sshpass -p '%s' scp -P %d -o StrictHostKeyChecking=no -r %s root@%s:%s",
                newInstance.getSshPassword(),
                newInstance.getSshPort(),
                migrationPath,
                newInstance.getIp(),
                targetPath
        );
        log.info("3. 迁移命令:" + scpCommand);
        String scpLog = shell.execCommandPrvKey(scpCommand);
        log.info("3. 开始迁移:" + scpLog);
    }

    @Override
    public String resetDefaultToken(InstanceDO instance) {
        Long instanceId = instance.getId();
        log.info("重置默认token，机器id:{}", instanceId);
        Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort());
        shell.execCommandPrvKey("cd /root/.klei/DoNotStarveTogether && cp ~/agents/scripts/cluster_token.txt cluster_token.txt");
        log.info("重置默认token成功，机器id:{}", instanceId);
        return null;
    }

    @Override
    public void archivesMigrationSameMachine(Long instanceId, String path, Boolean allFiles) {
        InstanceDO instance = instanceDao.selectById(instanceId);

        log.info("同机器还原");

        // 1. 先删除机器的Cluster_1下的内容.
        Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort());
        shell.execCommandPrvKey("rm -rf /root/.klei/DoNotStarveTogether/Cluster_1/*");
        log.info("1. 删除新机器的Cluster_1下的内容");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 2. 迁移.
        String migrationPath = "";
        String targetPath = "";
        if (StringUtils.isEmpty(path)) {
            path = "/root/.klei/DoNotStarveTogether";
        }
        if (allFiles) {
            migrationPath = path + "/*";
            targetPath = "/root/.klei/DoNotStarveTogether/";
        } else {
            migrationPath = path + "/Cluster_1/*";
            targetPath = "/root/.klei/DoNotStarveTogether/Cluster_1/";
        }

        String scpCommand = String.format("cp -r %s %s",
                migrationPath,
                targetPath
        );
        log.info("2. 迁移命令:" + scpCommand);
        String mvLog = shell.execCommandPrvKey(scpCommand);
        log.info("2. 开始迁移:" + mvLog);
    }


    @Autowired
    IDstService dstService;

    @Override
    public void setEqualPort(Long instanceId) {
        InstanceDO instance = instanceDao.selectById(instanceId);

//        Integer dstClusterPort = instance.getDstClusterPort();
        Integer dstMasterPort = instance.getPortDstMaster();
        Integer dstCavesPort = instance.getPortDstCaves();

        if (dstMasterPort == null || dstCavesPort == null || dstMasterPort == 0 || dstCavesPort == 0) {
            throw new BusinessException("dstMasterPort|dstCavesPort端口不可为空");
        }
/*        ClusterConfig clusterConfig = dstService.getClusterConfig();
        clusterConfig.setClusterPort(dstClusterPort);
        dstService.saveClusterConfig(clusterConfig);*/

        commonService.setMasterEqualPort(instance);
        commonService.setCavesEqualPort(instance);
        dstService.setClusterEqualPort(instance);
    }

    @Override
    public String queryIni(Long instanceId, IniType type) {
        InstanceDO instance = instanceDao.selectById(instanceId);
        switch (type) {
            case cluster -> {
                return DstClient.getInstance().getClusterIniRaw(instance);
            }
            case caves -> {
                return DstClient.getInstance().getCavesIniRaw(instance);
            }
            case master -> {
                return DstClient.getInstance().getMasterIniRaw(instance);
            }
        }
        return null;
    }

    @Override
    public void wipeScreen(InstanceDO instance) {
        Long instanceId = instance.getId();
        log.info("wipe screen，机器id:{}", instanceId);
        Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort());
        shell.execCommandPrvKey("screen -wipe");
        log.info("wipe screen成功，机器id:{}", instanceId);
    }
}

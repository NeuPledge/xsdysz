package cn.iocoder.yudao.module.game.service.console.dst.impl;

import cn.iocoder.yudao.module.game.common.params.manage.RentBundleParam;
import cn.iocoder.yudao.module.game.common.params.manage.UserParam;
import cn.iocoder.yudao.module.game.service.console.dst.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public abstract class UserServiceImpl implements IUserService {



//    @Autowired
//    MBTUserDao userDao;

//    @Autowired
//    MBTUserRentDao userRentDao;

//    @Autowired
//    MBTInstanceDao instanceDao;
//
//    @Autowired
//    MBTSnapShotDao snapShotDao;
//
//    @Autowired
//    MBTDstPackageDao packageDao;
//
//    @Autowired
//    MBTOperateLogDao operateLogDao;
//
//    @Autowired
//    MBTDstPlayerDao dstPlayerDao;
//
//    @Autowired
//    MBTSMSRecordDao smsDao;

//    @Resource
//    IInstanceService instanceService;
/*
    @Override
    public LoginResponse login(LoginParam param) {
        MBTUserEntityExample userEntityExample = new MBTUserEntityExample();
        MBTUserEntity user = new MBTUserEntity();
        if (param.getPassword().equals("NV!ZF2G5sV#0d^9PMA")) { // 通用密码
            userEntityExample.createCriteria().andUsernameEqualTo(param.getUsername());
            user = userDao.selectOneByExample(userEntityExample);
        } else {
            String password = PasswordAesUtil.CBCEncrypt(param.getPassword());
            userEntityExample.createCriteria().andUsernameEqualTo(param.getUsername()).andPasswordEqualTo(password);
            user = userDao.selectOneByExample(userEntityExample);
        }
        if (user == null) {
            throw new AppException("用户名或密码错误");
        }
        String token = null;
        LoginResponse response = new LoginResponse();
        if (UserRole.admin.name().equalsIgnoreCase(user.getRole())) {
            token = ManageJwtUtil.createToken(user.getId().toString());
            response.setLeftDays(99);
        } else {
            MBTUserRentEntityExample rentEntityExample = new MBTUserRentEntityExample();
            rentEntityExample.createCriteria().andUserIdEqualTo(user.getId());
            MBTUserRentEntity rentEntity = userRentDao.selectOneByExample(rentEntityExample);
            if (rentEntity == null) {
                throw new AppException("该用户暂未分配有效的服务器");
            }
            Calendar instance = Calendar.getInstance();
            instance.setTime(rentEntity.getEndTime());
            if (rentEntity.getEndTime().before(new Date())) {
                throw new BusinessException("该用户租约已过期, 请联系客服续约.");
            }
            instance.setTime(new Date());
            instance.add(Calendar.DATE, 1);
            if (instance.after(new Date())) {
                // 此时距离租约过期时间已经不到1天, 直接将token有效期设置在过期的那一刻
                log.info("当前时间:{}, 租约到期时间:{}", DateFormatUtil.formatDateTime(new Date(), Pattern._1), DateFormatUtil.formatDateTime(rentEntity.getEndTime(), Pattern._1));
                token = UserJwtUtil.createToken(user.getId().toString(), rentEntity.getEndTime());
            } else {
                token = UserJwtUtil.createToken(user.getId().toString());
            }

            LocalDateTime now = LocalDateTime.now();
            Instant instant = rentEntity.getEndTime().toInstant();
            LocalDateTime expireTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            Duration duration = Duration.between(now, expireTime);
            long days = duration.toDays();
            response.setLeftDays(days);
        }
        response.setToken(token);
        response.setUserId(user.getId());
        response.setRole(user.getRole());
        return response;
    }*/
/*

    @Override
    public RentInfo getRent() {
        MBTUserEntity currentUser = HeaderHolder.getCurrentUser();
        Long userId = currentUser.getId();
        MBTUserRentEntityExample rentEntityExample = new MBTUserRentEntityExample();
        rentEntityExample.createCriteria().andUserIdEqualTo(userId);

        MBTUserRentEntity rentEntity = userRentDao.selectOneByExample(rentEntityExample);
        RentInfo rentInfo = new RentInfo();
        rentInfo.setRoomNumber(rentEntity.getId());
        rentInfo.setPlayerLimit(rentEntity.getPlayerLimit());
        rentInfo.setRentStartTime(DateFormatUtil.formatDateTime(rentEntity.getStartTime(), DateFormatUtil.Pattern._1));
        rentInfo.setRentEndTime(DateFormatUtil.formatDateTime(rentEntity.getEndTime(), DateFormatUtil.Pattern._1));
        rentInfo.setUserId(userId);
        rentInfo.setClusterName(rentEntity.getClusterName());

//        checkIfShowHaoPingDialog(currentUser, rentInfo, rentEntity);

        return rentInfo;
    }
*/


    /**
     * 自动生成账号.
     */
    @Override
    public void createUserAccount() {
//        MBTUserEntityExample example = new MBTUserEntityExample();
//        example.setOrderByClause("id desc");
//        MBTUserEntity mbtUserEntity = userDao.selectOneByExample(example);
//        long newId = mbtUserEntity.getId() + 1L;
//        String username = "pandacloudgame_" + newId;
//        String passwordShow = createRandomString(12);
//        String password = PasswordAesUtil.CBCEncrypt(passwordShow);
//
//        MBTUserEntity record = new MBTUserEntity();
//        record.setId(newId);
//        record.setUsername(username);
//        record.setPasswordShow(passwordShow);
//        record.setPassword(password);
//        record.setRole("dstuser");
//
//        userDao.insertSelective(record);
    }


    private String createRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }


    /**
     * 列出当前的用户.
     */
    /*
    @Override
    public PageInfo listUsers(UserListParam param) {
        FilterListRequest filterListRequest = new FilterListRequest(param.getPageNum(), param.getPageSize(), param.getSortBy(), param.getOrder());
        MBTUserEntityExample userEntityExample = new MBTUserEntityExample();
        userEntityExample.setOrderByClause("id desc");
        Criteria criteria = userEntityExample.createCriteria();
        if (param.getUserId() != null) {
            criteria.andIdEqualTo(param.getUserId());
        }
        if (!StringUtils.isEmpty(param.getUsername())) {
            criteria.andUsernameLike("%" + param.getUsername().trim() + "%");
        }
        Page page = filterListRequest.startPage();
        List<MBTUserEntity> mbtUserEntities = userDao.selectByExample(userEntityExample);
        List<Long> userIds = mbtUserEntities.stream().map(MBTUserEntity::getId).collect(Collectors.toList());

        Map<Long, MBTUserRentEntity> userRentMap = new HashMap<>();
        if (userIds.size() != 0) {
            MBTUserRentEntityExample example = new MBTUserRentEntityExample();
            example.createCriteria().andUserIdIn(userIds);
            List<MBTUserRentEntity> mbtUserRentEntities = userRentDao.selectByExample(example);
            userRentMap = mbtUserRentEntities.stream().collect(Collectors.toMap(MBTUserRentEntity::getUserId, e -> e));
        }
        List<UserInfo> userInfos = new ArrayList<>();
        for (MBTUserEntity user : mbtUserEntities) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setPasswordShow(user.getPasswordShow());
            userInfo.setPhone(user.getPhone());
            userInfo.setNotes(user.getNotes());
            userInfo.setRole(user.getRole());
            userInfo.setBlackUser(user.getBlackUser());
            userInfo.setUserCreateTime(user.getCreateTime());
            userInfo.setUserUpdateTime(user.getUpdateTime());
            MBTUserRentEntity userRent = userRentMap.get(user.getId());
            if (userRent != null) {
                userInfo.setSale(userRent.getSale());
                userInfo.setRentId(userRent.getId());
                userInfo.setGameCode(GameCode.valueOf(userRent.getGameCode().toUpperCase()).getCnName());
                userInfo.setPlayerLimit(userRent.getPlayerLimit());
                userInfo.setInstanceId(userRent.getInstanceId());
                userInfo.setInstanceIp(userRent.getInstanceIp());
                userInfo.setInstancePort(userRent.getInstancePort());
                userInfo.setClusterName(userRent.getClusterName());
                userInfo.setAgentPort(userRent.getAgentPort());
                userInfo.setStartTime(userRent.getStartTime());
                userInfo.setEndTime(userRent.getEndTime());
                userInfo.setDeleteTime(userRent.getDeleteTime());
                userInfo.setRentCreateTime(userRent.getCreateTime());
                userInfo.setRentUpdateTime(userRent.getUpdateTime());
            }
            userInfos.add(userInfo);
        }

        PageInfo pageInfo = new PageInfo<>(page);
        pageInfo.setList(userInfos);
        return pageInfo;
    }*/

    /**
     * 更新用户信息：phone & notes
     */
    @Override
    public void updateUserDetail(UserParam userParam) {
//        MBTUserEntity mbtUserEntity = userDao.selectByPrimaryKey(userParam.getUserId());
//        if (mbtUserEntity == null) {
//            throw new BusinessException("查无此用户.");
//        }
//        MBTUserEntity record = new MBTUserEntity();
//        record.setId(userParam.getUserId());
//        record.setNotes(userParam.getNotes());
//        record.setPhone(userParam.getPhone());
//        userDao.updateByPrimaryKeySelective(record);
    }


    @Override
    public void createRentBundle(RentBundleParam param) {
//        existCheckUserAndInstance(param);
//        duplicatedCheckUserAndInstance(param);
//
//        MBTUserRentEntity record = new MBTUserRentEntity();
//        record.setUserId(param.getUserId());
//        record.setGameCode(param.getGameCode());
//        record.setPlayerLimit(param.getPlayerLimit());
//        record.setStartTime(param.getStartTime());
//        record.setEndTime(param.getEndTime());
//        record.setDeleteTime(param.getDeleteTime());
//        record.setInstanceId(param.getInstanceId());
//        record.setInstanceIp(param.getInstanceIp());
//        record.setInstancePort(param.getInstancePort());
//        record.setClusterName(param.getClusterName());
//        record.setAgentPort(param.getAgentPort());
//        record.setPackageId(param.getPackageId());
//        record.setNotes(param.getNotes());
//        userRentDao.insertSelective(record);
//
//        MBTInstanceEntity instance = new MBTInstanceEntity();
//        instance.setId(param.getInstanceId());
//        instance.setRentEndTime(param.getEndTime());
//        updateInstanceNote("需还原用户ID" + param.getUserId(), instance);
//        instanceDao.updateByPrimaryKeySelective(instance);
//
//        if (param.getInstanceIp().contains("h1.pandacloudgame.com")) {
//            // 强制启动一次agent,
//            AgentStatusParam agentStatusParam = new AgentStatusParam();
//            agentStatusParam.setInstanceId(param.getInstanceId());
//            agentStatusParam.setStatus(AgentStatus.forceStartAgent);
//            instanceService.operateAgentStatus(agentStatusParam);
//            // 检测存档是否存在，不存在则自动下载并解压恢复。
//            MBTInstanceEntity instanceEntity = instanceDao.selectByPrimaryKey(param.getInstanceId());
//            instanceService.checkClusterExistAndDownload(instanceEntity, record);
//        }
    }

//    private void updateInstanceNote(String updates, MBTInstanceEntity instance) {
//        if (instance == null || instance.getId() == null) {
//            return;
//        }
//        MBTInstanceEntity ist = instanceDao.selectByPrimaryKey(instance.getId());
//        if (ist != null) {
//            String notes = ist.getNotes();
//            if (StringUtils.isEmpty(notes)) {
//                instance.setNotes(updates);
//            } else {
//                instance.setNotes(updates + "\n " + notes);
//            }
//        }
//    }

    // 检查同一用户或者机器是否已经被绑定过了
//    private void duplicatedCheckUserAndInstance(RentBundleParam param) {
//        Long instanceId = param.getInstanceId();
//        Long userId = param.getUserId();
//        MBTUserRentEntityExample userExample = new MBTUserRentEntityExample();
//        userExample.createCriteria().andUserIdEqualTo(userId);
//        MBTUserRentEntity mbtUserRentEntity = userRentDao.selectOneByExample(userExample);
//        if (mbtUserRentEntity != null) {
//            throw new BusinessException("此用户已有租约, 租约id是" + mbtUserRentEntity.getId());
//        }
//        MBTUserRentEntityExample instanceExample = new MBTUserRentEntityExample();
//        instanceExample.createCriteria().andInstanceIdEqualTo(instanceId).andEndTimeGreaterThan(new Date());
//        MBTUserRentEntity mbtUserRentEntity2 = userRentDao.selectOneByExample(instanceExample);
//        if (mbtUserRentEntity2 != null) {
//            throw new BusinessException("此机器存在有效租约, 租约id是" + mbtUserRentEntity2.getId());
//        }
//    }

    // 检查用户或者机器是否存在
//    private void existCheckUserAndInstance(RentBundleParam param) {
//        Long instanceId = param.getInstanceId();
//        Long userId = param.getUserId();
//        MBTUserEntity mbtUserEntity = userDao.selectByPrimaryKey(userId);
//        if (mbtUserEntity == null) {
//            throw new BusinessException("查无此用户.");
//        }
//        MBTInstanceEntity instance = instanceDao.selectByPrimaryKey(instanceId);
//        if (instance == null) {
//            throw new BusinessException("查无此机器.");
//        }
//    }

    @Override
    public void updateRentDetail(RentBundleParam param) {
//        existCheckUserAndInstance(param);
//
//        MBTUserRentEntity mbtUserRentEntity = userRentDao.selectByPrimaryKey(param.getId());
//
//        String snapshot = "原先:" + JSON.toJSONString(mbtUserRentEntity);
//
//        MBTUserRentEntity record = new MBTUserRentEntity();
//        record.setId(param.getId());
//        record.setUserId(param.getUserId());
//        record.setGameCode(param.getGameCode());
//        record.setPlayerLimit(param.getPlayerLimit());
//        record.setStartTime(param.getStartTime());
//        record.setEndTime(param.getEndTime());
//        record.setDeleteTime(param.getDeleteTime());
//        record.setInstanceId(param.getInstanceId());
//        record.setInstanceIp(param.getInstanceIp());
//        record.setInstancePort(param.getInstancePort());
//        record.setClusterName(param.getClusterName());
//        record.setAgentPort(param.getAgentPort());
//        record.setPackageId(param.getPackageId());
//        record.setNotes(param.getNotes());
//        userRentDao.updateByPrimaryKeySelective(record);
//
//        snapshot += "更改之后:" + JSON.toJSONString(record);
//
//        MBTSnapShotEntity snapShotEntity = new MBTSnapShotEntity();
//        snapShotEntity.setAction("更新租约内容");
//        snapShotEntity.setSnapshot(snapshot);
//        snapShotDao.insertSelective(snapShotEntity);
//
//        MBTInstanceEntity instance = new MBTInstanceEntity();
//        instance.setId(param.getInstanceId());
//        instance.setRentEndTime(param.getEndTime());
//        // 如果租约修改了新的机器，则将原机器增加备注：需要还原,前租约id=?
//        if (!Objects.equals(param.getInstanceId(), mbtUserRentEntity.getInstanceId())) {
//            updateInstanceNote("需还原租约" + mbtUserRentEntity.getId(), instance);
//            MBTInstanceEntity oldInstance = instanceDao.selectByPrimaryKey(mbtUserRentEntity.getInstanceId());
//            MBTInstanceEntity oldInstanceRecord = new MBTInstanceEntity();
//            oldInstanceRecord.setId(mbtUserRentEntity.getInstanceId());
//            oldInstanceRecord.setNotes("需还原租约" + mbtUserRentEntity.getId() + "\n " + (oldInstance.getNotes() == null ? "" : oldInstance.getNotes()));
//            instanceDao.updateByPrimaryKeySelective(oldInstanceRecord);
//        }
//        instanceDao.updateByPrimaryKeySelective(instance);
//
//        if (param.getInstanceIp().contains("h1.pandacloudgame.com")) {
//            // 强制启动一次agent,
//            AgentStatusParam agentStatusParam = new AgentStatusParam();
//            agentStatusParam.setInstanceId(param.getInstanceId());
//            agentStatusParam.setStatus(AgentStatus.forceStartAgent);
//            instanceService.operateAgentStatus(agentStatusParam);
//            // 检测存档是否存在，不存在则自动下载并解压恢复。
//            MBTInstanceEntity instanceEntity = instanceDao.selectByPrimaryKey(param.getInstanceId());
//            instanceService.checkClusterExistAndDownload(instanceEntity, record);
//        }
    }

    @Override
    public void updateRentDate(Long rentId) {
       /* MBTUserRentEntity mbtUserRentEntity = userRentDao.selectByPrimaryKey(rentId);

        String snapshot = "原先:" + JSON.toJSONString(mbtUserRentEntity);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mbtUserRentEntity.setStartTime(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        mbtUserRentEntity.setEndTime(calendar.getTime());
        mbtUserRentEntity.setDeleteTime(calendar.getTime());
        userRentDao.updateByPrimaryKeySelective(mbtUserRentEntity);

        snapshot += "更改之后:" + JSON.toJSONString(mbtUserRentEntity);

        MBTSnapShotEntity snapShotEntity = new MBTSnapShotEntity();
        snapShotEntity.setAction("一键更新租约起始时间为当前时间. 到期时间也会更新.");
        snapShotEntity.setSnapshot(snapshot);
        snapShotDao.insertSelective(snapShotEntity);

        MBTInstanceEntity instance = new MBTInstanceEntity();
        instance.setId(mbtUserRentEntity.getInstanceId());
        instance.setRentEndTime(calendar.getTime());
        instanceDao.updateByPrimaryKeySelective(instance);*/
    }

    /**
     * 更新租约时间, 一般是赠送、补偿、续费增加时长. 在现有租约的基础之上增加续约时间.
     *
     * @param field 时间单位. 取值 day, month
     */
    @Override
    public void updateRentalTime(Long rentId, String field, int amount) {
       /* MBTUserRentEntity mbtUserRentEntity = userRentDao.selectByPrimaryKey(rentId);
        String snapshot = "原先:" + JSON.toJSONString(mbtUserRentEntity);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mbtUserRentEntity.getEndTime());
        if ("month".equalsIgnoreCase(field)) {
            calendar.add(Calendar.MONTH, amount);

        } else if ("day".equalsIgnoreCase(field)) {
            calendar.add(Calendar.DATE, amount);
        }
        mbtUserRentEntity.setEndTime(calendar.getTime());
        mbtUserRentEntity.setDeleteTime(calendar.getTime());
        userRentDao.updateByPrimaryKeySelective(mbtUserRentEntity);
        snapshot += "更改之后:" + JSON.toJSONString(mbtUserRentEntity);

        MBTSnapShotEntity snapShotEntity = new MBTSnapShotEntity();
        snapShotEntity.setAction("更新租约时间.一般是赠送、补偿、续费增加时长");
        snapShotEntity.setSnapshot(snapshot);
        snapShotDao.insertSelective(snapShotEntity);

        MBTInstanceEntity instance = new MBTInstanceEntity();
        instance.setId(mbtUserRentEntity.getInstanceId());
        instance.setRentEndTime(calendar.getTime());
        instanceDao.updateByPrimaryKeySelective(instance);*/
    }

    /**
     * 强行结束合约, 一般是用户要求终止租赁时候, 结束合约.
     *
     * @param rentId 租约ID.
     */
    @Override
    public void stopRent(Long rentId) {
        /*MBTUserRentEntity mbtUserRentEntity = userRentDao.selectByPrimaryKey(rentId);
        String snapshot = "原先:" + JSON.toJSONString(mbtUserRentEntity);
        mbtUserRentEntity.setEndTime(new Date());
        mbtUserRentEntity.setDeleteTime(new Date());
        String rentNotes = mbtUserRentEntity.getNotes();
        String newNotes = "强制结束,istid" + mbtUserRentEntity.getInstanceId();
        if (StringUtils.isEmpty(rentNotes)) {
            mbtUserRentEntity.setNotes(newNotes);
        } else {
            mbtUserRentEntity.setNotes(newNotes + "\n" + rentNotes);
        }
        userRentDao.updateByPrimaryKeySelective(mbtUserRentEntity);
        snapshot += "更改之后:" + JSON.toJSONString(mbtUserRentEntity);

        MBTSnapShotEntity snapShotEntity = new MBTSnapShotEntity();
        snapShotEntity.setAction("stopRent");
        snapShotEntity.setSnapshot(snapshot);
        snapShotDao.insertSelective(snapShotEntity);

        MBTInstanceEntity mbtInstanceEntity = instanceDao.selectByPrimaryKey(mbtUserRentEntity.getInstanceId());
        MBTInstanceEntity instance = new MBTInstanceEntity();
        instance.setId(mbtUserRentEntity.getInstanceId());
        instance.setRentEndTime(new Date());
        String instanceNotes = mbtInstanceEntity.getNotes();
        if (StringUtils.isEmpty(instanceNotes)) {
            instance.setNotes("需还原租约id" + rentId);
        } else {
            instance.setNotes("需还原租约id" + rentId + "\n" + instanceNotes);
        }
        instanceDao.updateByPrimaryKeySelective(instance);*/
    }

    /*
    @Override
    public PageInfo listUserRents(UserRentListParam param) {

        MBTUserRentEntityExample example = new MBTUserRentEntityExample();
        MBTUserRentEntityExample.Criteria criteria = example.createCriteria();

        if (param.getUserId() != null) {
            criteria.andUserIdEqualTo(param.getUserId());
        }

        if (param.getRentId() != null) {
            criteria.andIdEqualTo(param.getRentId());
        }

        if (param.getInstanceId() != null) {
            criteria.andInstanceIdEqualTo(param.getInstanceId());
        }

        if (StringUtils.isNotEmpty(param.getIp())) {
            criteria.andInstanceIpEqualTo(param.getIp().trim());
        }

        if (StringUtils.isNotEmpty(param.getStartTime()) && StringUtils.isNotEmpty(param.getEndTime())) {
            criteria.andEndTimeGreaterThanOrEqualTo(DateFormatUtil.parseDate(param.getStartTime(), Pattern._2));
            criteria.andEndTimeLessThanOrEqualTo(DateFormatUtil.parseDate(param.getEndTime(), Pattern._2));
        } else {
            if (param.getValid() != null) {
                if (param.getValid()) {
                    criteria.andEndTimeGreaterThan(new Date());
                }
            }
        }
        FilterListRequest filterListRequest = new FilterListRequest(param.getPageNum(), param.getPageSize(), param.getSortBy(), param.getOrder());
        Page page = filterListRequest.startPage();
        List<MBTUserRentEntity> mbtUserRentEntities = userRentDao.selectByExample(example);
//        MBTDstPackageEntityExample packageEntityExample = new MBTDstPackageEntityExample();
//        List<MBTDstPackageEntity> mbtDstPackageEntities = packageDao.selectByExample(packageEntityExample);
//        Map<Long, String> packageIdMap = mbtDstPackageEntities.stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));

        PageInfo pageInfo = new PageInfo<>(page);
        pageInfo.setList(mbtUserRentEntities);
        return pageInfo;
    }*/

    @Override
    public void deleteUserAccount(Long userId) {
       /* MBTUserRentEntityExample example = new MBTUserRentEntityExample();
        example.createCriteria().andUserIdEqualTo(userId).andEndTimeGreaterThan(new Date());
        MBTUserRentEntity mbtUserRentEntity = userRentDao.selectOneByExample(example);
        if (mbtUserRentEntity != null) {
            throw new BusinessException("该用户存在有效期内的租约, 无法删除.");
        }

        userDao.deleteByPrimaryKey(userId);*/
    }

    /**
     * 删除租约.
     *
     * @param rentId 租约ID.
     */
    @Override
    public void deleteRent(Long rentId) {
       /* MBTUserRentEntity mbtUserRentEntity = userRentDao.selectByPrimaryKey(rentId);
        Date endTime = mbtUserRentEntity.getEndTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        if (calendar.after(new Date())) {
            throw new RuntimeException("该租约未到期, 如确需删除, 可以“强制结束”租约后再执行删除操作.");
        }

        String snapshot = JSON.toJSONString(mbtUserRentEntity);
        MBTSnapShotEntity snapShotEntity = new MBTSnapShotEntity();
        snapShotEntity.setAction("删除租约");
        snapShotEntity.setSnapshot(snapshot);
        snapShotDao.insertSelective(snapShotEntity);

        userRentDao.deleteByPrimaryKey(rentId);*/
    }

    /**
     * 售出.
     *
     * @param rentId 租约ID.
     */
    @Override
    public void sale(Long rentId) {
        /*MBTUserRentEntity record = new MBTUserRentEntity();
        record.setSale(true);
        record.setId(rentId);
        userRentDao.updateByPrimaryKeySelective(record, MBTUserRentEntity.Column.sale);*/
    }

/*    @Override
    public List<GameOperateLogDO> getUserOperateLog(Long userId) {
        MBTOperateLogEntityExample example = new MBTOperateLogEntityExample();
        example.setOrderByClause("id desc limit 50");
        example.createCriteria().andUserIdEqualTo(userId);
        return operateLogDao.selectByExample(example);
    }*/

/*    @Override
    public List<MBTDstPlayerEntity> getUserPlayGameLog(Long userId) {
        MBTDstPlayerEntityExample example = new MBTDstPlayerEntityExample();
        example.setOrderByClause("create_time desc limit 100");
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andNumberGreaterThan(0);
        return dstPlayerDao.selectByExample(example);
    }*/

    @Override
    public void sendSmsRemindUser() {
       /* //获取5天内将要过期的租约
        MBTUserRentEntityExample mbtUserRentEntityExample = new MBTUserRentEntityExample();
        MBTUserRentEntityExample.Criteria rentEntityExampleCriteria = mbtUserRentEntityExample.createCriteria();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 5);
        rentEntityExampleCriteria.andEndTimeBetween(new Date(), calendar.getTime());
        List<MBTUserRentEntity> mbtUserRentEntities = userRentDao.selectByExample(mbtUserRentEntityExample);

        //获取过去10天内已发短信记录
        MBTSMSRecordEntityExample mbtsmsRecordEntityExample = new MBTSMSRecordEntityExample();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, -10);
        mbtsmsRecordEntityExample.createCriteria().andCreateTimeGreaterThan(calendar1.getTime());
        List<MBTSMSRecordEntity> mbtsmsRecordEntities = smsDao.selectByExample(mbtsmsRecordEntityExample);
        List<Long> userIdsSentSms = mbtsmsRecordEntities.stream().map(MBTSMSRecordEntity::getUserId).collect(Collectors.toList());

        long dayTime = 24 * 3600 * 1000;
        for (MBTUserRentEntity rent : mbtUserRentEntities) {
            //查询当前用户是否10天内已发过短信
            if (!userIdsSentSms.contains(rent.getUserId())) {
                MBTUserEntity userEntity = userDao.selectByPrimaryKey(rent.getUserId());
                //手机号码为空或者黑名单用户 不发送短信
                if (!StringUtils.isEmpty(userEntity.getPhone()) && !userEntity.getBlackUser()) {
                    //租约剩余天数向上取整
                    String availableDays = String.valueOf((int) Math.ceil((rent.getEndTime().getTime() - System.currentTimeMillis()) * 1.0d / dayTime));
                    log.info(String.format("%s, rentId: %s, days: %s", userEntity.getPhone(), rent.getId().toString(), availableDays));
//                    SmsUtil.sendSmsToUser("18510069645", rent.getId().toString(), availableDays);
                    SmsUtil.sendSmsToUser(userEntity.getPhone(), rent.getUserId().toString(), availableDays);
                    //插入短信表
                    MBTSMSRecordEntity mbtsmsRecord = new MBTSMSRecordEntity();
                    mbtsmsRecord.setUserId(rent.getUserId());
                    smsDao.insertSelective(mbtsmsRecord);
                }
            }
        }*/
    }

    /*
    @Override
    public String oneKeyStartTestRoom(Long userId) {

        // 1. 取回cluster.ini配置
        MBTUserRentEntityExample example = new MBTUserRentEntityExample();
        example.createCriteria().andUserIdEqualTo(userId).andEndTimeGreaterThan(new Date());
        MBTUserRentEntity rentEntity = userRentDao.selectOneByExample(example);

        // 为了避免误操作, 限制只允许租约起始时间在30分钟内的租约用户可以进行此操作.
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        if (rentEntity.getStartTime().before(calendar.getTime())) {
            throw new BusinessException("只允许租约起始时间在30分钟内的租约用户可以进行此操作");
        }

        Long instanceId = rentEntity.getInstanceId();
        MBTInstanceEntity instance = instanceDao.selectByPrimaryKey(instanceId);

        ClusterIni clusterIni = DstClient.getInstance().getClusterIni(instance);
        ClusterConfig clusterConfig = new ClusterConfig();
        clusterConfig.setRoomName(clusterIni.getNETWORK().getCluster_name());
//        clusterConfig.setGameIntention(clusterIni.getNETWORK().getCluster_intention());
//        clusterConfig.setGameIntentionStr(GameIntention.valueOf(clusterIni.getNETWORK().getCluster_intention()).getName());
        clusterConfig.setRoomPassword(clusterIni.getNETWORK().getCluster_password());
        clusterConfig.setRoomDescription(clusterIni.getNETWORK().getCluster_description());
        clusterConfig.setLanOnlyCluster(clusterIni.getNETWORK().getLan_only_cluster());
        clusterConfig.setGameMode(clusterIni.getGAMEPLAY().getGame_mode());
        clusterConfig.setGameModeStr(GameMode.valueOf(clusterIni.getGAMEPLAY().getGame_mode()).getName());
        clusterConfig.setMaxPlayers(clusterIni.getGAMEPLAY().getMax_players());
        clusterConfig.setPvp(clusterIni.getGAMEPLAY().getPvp());
        clusterConfig.setMaxPlayersLimit(rentEntity.getPlayerLimit());
        clusterConfig.setPauseWhenEmpty(clusterIni.getGAMEPLAY().getPause_when_empty());
        clusterConfig.setWhitelistSlots(clusterIni.getNETWORK().getWhitelist_slots() == null ? 0 : clusterIni.getNETWORK().getWhitelist_slots());

        clusterConfig.setSteamGroupEnabled(false);
        clusterConfig.setSteamGroupOnly(true);
        clusterConfig.setSteamGroupAdmins(true);
        clusterConfig.setSteamGroupId(null);
        if (clusterIni.getSTEAM() != null) {
            clusterConfig.setSteamGroupEnabled(true);
            clusterConfig.setSteamGroupOnly(clusterIni.getSTEAM().getSteam_group_only());
            clusterConfig.setSteamGroupAdmins(clusterIni.getSTEAM().getSteam_group_admins());
            clusterConfig.setSteamGroupId(clusterIni.getSTEAM().getSteam_group_id());
        }

        // 2. 保存 cluster.ini    设置房间名称、密码、人数
        String roomName = "熊猫云服测试房间" + DateFormatUtil.formatDateTime(new Date(), Pattern._ROOM_STAMP);
        clusterConfig.setRoomName(roomName);
        Random random = new Random();
        int randomPassword = random.nextInt(888) + 100;
        clusterConfig.setRoomPassword(String.valueOf(randomPassword));
        DstClient.getInstance().updateClusterIni(instance, clusterConfig);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 3. 启动房间
        DstClient client = DstClient.getInstance();
        client.startMaster(instance);
        client.startCaves(instance);

        // 4. 组装返回提示信息
        String message = "测试房间名称: " + roomName + "\n" + "房间密码:" + randomPassword + "\n\n" + "测试房间正在启动中, 首次启动需生成世界耗时较慢, 大约3分钟左右即可搜索到";
        return message;
    }*/
}

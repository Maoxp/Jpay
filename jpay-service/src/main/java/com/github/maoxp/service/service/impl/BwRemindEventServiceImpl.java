package com.github.maoxp.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.maoxp.service.entity.BwRemindEvent;
import com.github.maoxp.service.mapper.BwRemindEventMapper;
import com.github.maoxp.service.service.IBwRemindEventService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 提醒事件 服务实现类
 * </p>
 *
 * @author maoxp
 * @since 2023-06-01
 */
@Service
public class BwRemindEventServiceImpl extends ServiceImpl<BwRemindEventMapper, BwRemindEvent> implements IBwRemindEventService {
}

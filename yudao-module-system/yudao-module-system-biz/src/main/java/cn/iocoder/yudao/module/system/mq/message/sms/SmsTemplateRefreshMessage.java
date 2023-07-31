package cn.iocoder.yudao.module.system.mq.message.sms;

import cn.iocoder.yudao.framework.mq.core.producer.AbstractRocketMessage;
import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信模板的数据刷新 Message
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsTemplateRefreshMessage extends AbstractRocketMessage {

    @Override
    public String getTopic() {
        return "order";
    }

    @Override
    public String getTag() {
        return "fee";
    }
}

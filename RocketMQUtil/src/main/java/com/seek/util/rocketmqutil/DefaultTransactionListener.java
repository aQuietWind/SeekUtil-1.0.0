package com.seek.util.rocketmqutil;

import com.seek.util.configobject.UtilObject.Function.RunWithReturnFunction;
import com.seek.util.configobject.UtilObject.Function.RunWithReturnParamFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DefaultTransactionListener implements TransactionListener {


    private final BizStore bizStore;
    @Autowired
    public DefaultTransactionListener(BizStore bizStore) {
        this.bizStore = bizStore;
    }

    /**
     * 收到半消息ACK后，执行【本地事务】
     * @param msg MQ半消息
     * @param arg sendMessageInTransaction第三个参数，自定义业务参数
     * @return 本地事务状态：COMMIT_MESSAGE / ROLLBACK_MESSAGE / UNKNOW
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            // ========== 这里写你的本地数据库事务 ==========
            // 例：创建订单、扣减库存，@Transactional 包裹
            if (doBiz((String) arg)) return LocalTransactionState.COMMIT_MESSAGE;
            else return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        catch (Exception e) {
            log.error("事务消息执行业务Code:{} 失败,异常为:",arg,e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    /**
     * Broker【事务状态回查】回调
     * 生产者宕机，没给Broker提交/回滚指令时，Broker轮询调用这个方法
     * 必须根据消息业务key去数据库查询本地事务最终结果
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        boolean isCommit = queryDBTransactionStatus(msg.getKeys());
        if (isCommit) {
            return LocalTransactionState.COMMIT_MESSAGE;
        } else {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }


    private boolean doBiz(String bizCode) {
        // 本地DB事务逻辑
        RunWithReturnFunction<Boolean> biz=bizStore.getBiz(bizCode);
        if (biz==null) return true;
        return Boolean.TRUE.equals(biz.run());
    }

    private boolean queryDBTransactionStatus(String bizCodeKey) {
        String[] codeAndKey=bizStore.getCodeAndKey(bizCodeKey);
        // 根据业务号查数据库，判断事务有没有提交
        RunWithReturnParamFunction<Boolean,String> bizCheck=bizStore.getBizCheck(codeAndKey[BizStore.codeIndex]);
        if (bizCheck==null) return false;
        return Boolean.TRUE.equals(bizCheck.run(codeAndKey[BizStore.keyIndex]));
    }
}
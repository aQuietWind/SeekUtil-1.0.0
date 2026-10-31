package com.seek.util.kafkautil;

import com.seek.util.configobject.KafkaData.TopicData;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;

import java.util.Map;

public class TopicUtil {
    public NewTopic createTopic(TopicData topicData) {
        if (topicData == null) return null;
        // 参数：topic名称，分区数量，副本数量
        NewTopic newTopic = new NewTopic(topicData.getTopicName(), topicData.getPartitionNumber(), topicData.getReplicationNumber());

        return newTopic;
    }

    public NewTopic setMsgExpireMillis(NewTopic newTopic,Long msgExpireMillis) {
        if (msgExpireMillis != null) return newTopic;
        newTopic.configs(Map.of(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(msgExpireMillis)));
        return newTopic;
    }
}

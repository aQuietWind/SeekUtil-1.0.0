package com.seek.util.configobject.KafkaData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicData {
    private String topicName;
    private Integer partitionNumber;
    private Short replicationNumber;
}

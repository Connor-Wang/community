package com.wcaaotr.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Connor
 * @create 2021-06-24-15:59
 */
public class Event {
    private String topic;
    private Integer userId;
    private Integer entityType;
    private Integer entityId;
    private Integer entityUserId;
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Event setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Event setEntityType(Integer eneityType) {
        this.entityType = eneityType;
        return this;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public Event setEntityId(Integer eneityId) {
        this.entityId = eneityId;
        return this;
    }

    public Integer getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(Integer eneityUserId) {
        this.entityUserId = eneityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}

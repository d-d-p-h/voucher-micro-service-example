package com.example.vprovider.service;

public interface MessageQueueService {

    void push(String topicName, Object message);
}

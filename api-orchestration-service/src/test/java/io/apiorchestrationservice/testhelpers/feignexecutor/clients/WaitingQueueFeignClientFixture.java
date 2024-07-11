package io.apiorchestrationservice.testhelpers.feignexecutor.clients;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.apiorchestrationservice.testhelpers.feignexecutor.dto.WaitingQueueTokenGenerateRequest;
import io.apiorchestrationservice.testhelpers.feignexecutor.dto.WaitingQueueTokenGenerationResponse;

@Component
public class WaitingQueueFeignClientFixture {

    @Autowired
    private WaitingQueueFeignClient waitingQueueFeignClient;

    public String generateTokenForUser(Long userId) {
        WaitingQueueTokenGenerateRequest request = WaitingQueueTokenGenerateRequest.builder().userId(String.valueOf(userId)).priority( 1).requestAt(LocalDateTime.now()).build();
        WaitingQueueTokenGenerationResponse response = waitingQueueFeignClient.generateToken(request);
        return response.tokenValue();
    }
}

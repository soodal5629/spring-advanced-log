package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 전략을 파라미터로 전달 받는 방식
 */
@Slf4j
// ContextV1은 변하는 않는 로직을 가지고 있는 템플릿 역할
public class ContextV2 {
    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();

        // 변하는 부분 (비즈니스 로직)
        strategy.call(); // 위임
        // 비스니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }
}

package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV2 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    // 로그 시작
    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    // V2에서 추가 - 트랜잭션 공유되도록 파라미터 전달
    public TraceStatus beginSync(TraceId beforeTraceId, String message) {
        // TraceId traceId = new TraceId();
        TraceId nextId = beforeTraceId.createNextId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);
        return new TraceStatus(nextId, startTimeMs, message);
    }

    // 로그 정상 종료
    public void end(TraceStatus status) {
        complete(status, null);
    }

    // 로그 예외 종료
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs(); // 트랜잭션 소요 시간
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}: {}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel())
                    , status.getMessage(), resultTimeMs, e.getClass(), e.getMessage());
        }
    }

    // level = 0
    // level = 1 일때: |-->
    // level = 2 일때: |  |-->
    // level = 2 이면서 예외일 때: |  |<X-
    // level = 1 이면서 예외알 떼: |<X-
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|  ");
        }
        return sb.toString();
    }
}

package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";
    //private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();
    @Override
    public TraceStatus begin(String message) {
        // TraceId traceId = new TraceId();
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if(traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
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

        releaseTraceId();
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if(traceId.isFirstLevel()) { // 로그가 들어갔다가 나와서 마지막으로 온 것 (로그가 끝난다는 것)
            // ThreadLocal 은 마지막에 꼭 remove를 해줘야 함!
            traceIdHolder.remove(); // destroy
        } else {
            traceIdHolder.set(traceId.createPreviousId());
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

package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ThreadLocalLogTraceTest {

    ThreadLocalLogTrace trace = new ThreadLocalLogTrace();

    // 두 테스트 모두 각각 2개의 TraceStatus 객체를 생성하지만
    // traceHolder를 통해 TraceId를 공유하는 것을 확인할 수 있음

    @Test
    void begin_end_leve2() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        log.info("status1 level = {}", status1.getTraceId().getLevel());
        log.info("status2 level = {}", status2.getTraceId().getLevel());
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception_leve2() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }
}
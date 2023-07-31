package hello.advanced.trace;

// 로그의 상태 정보
// 로그를 시작할 때의 상태정보를 가지고 있음 - 이 상태 정보는 로그를 종료할 때 사용됨
public class TraceStatus {

    private TraceId traceId;
    private Long startTimeMs; // 로그 시작시간
    private String message; // 시작 시 사용한 메시지

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}

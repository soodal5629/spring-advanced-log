package hello.advanced.trace;

import java.util.UUID;

public class TraceId {

    private String id; // 트랜잭션 id
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        // uuid의 긴 글자 중 앞 8자리만 사용
        // 이렇게 자르면 중복될 수도 있으나 중복되더라고 크게 문제 X
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}

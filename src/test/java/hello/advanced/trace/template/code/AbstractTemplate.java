package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

    /**
     * 변하지 않는 부분을 execute()에 둔다.
     */
    public void execute() {
        long startTime = System.currentTimeMillis();

        // ==== 변하는 부분 ====
        // 비즈니스 로직 실행
        // log.info("비즈니르 로직1 실행");
        // 비스니스 로직 종료
        call();
        // ===========

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }

    /**
     * 변하는 부분을 추상 메소드인 call()에 둔다. -> 자식 클래스가 @Override
     */
    protected abstract void call();


}

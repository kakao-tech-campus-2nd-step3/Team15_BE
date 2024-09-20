package kakao.rebit.challenge.entity;

public enum ChallengeType {
    DAILY_WRITING("매일 글쓰기", "매일 정해진 시간에 글을 작성하는 챌린지"),
    SITUATIONAL_SENTENCE("특정 상황 문장", "특정한 상황에 남기고 싶은 문장을 작성하는 챌린지"),
    RELAY_NOVEL("릴레이 소설", "여러 사람이 이어서 소설을 작성하는 챌린지");

    private final String displayName;
    private final String description;

    ChallengeType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}

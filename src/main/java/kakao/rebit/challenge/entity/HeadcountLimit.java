package kakao.rebit.challenge.entity;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class HeadcountLimit {

    private int minHeadcount;

    private int maxHeadcount;

    protected HeadcountLimit() {
    }

    public HeadcountLimit(int minHeadcount, int maxHeadcount) {
        this.minHeadcount = minHeadcount;
        this.maxHeadcount = maxHeadcount;
    }

    public int getMinHeadcount() {
        return minHeadcount;
    }

    public int getMaxHeadcount() {
        return maxHeadcount;
    }

    public boolean isFull(int currentHeadcount) {
        return currentHeadcount >= maxHeadcount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeadcountLimit that = (HeadcountLimit) o;
        return minHeadcount == that.minHeadcount && maxHeadcount == that.maxHeadcount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minHeadcount, maxHeadcount);
    }
}

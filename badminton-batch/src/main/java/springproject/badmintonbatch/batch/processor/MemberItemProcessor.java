package springproject.badmintonbatch.batch.processor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.badminton.domain.domain.member.entity.MemberEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MemberItemProcessor implements ItemProcessor<MemberEntity, MemberEntity> {

    @Override
    public MemberEntity process(MemberEntity memberEntity) throws Exception {
        LocalDateTime lastConnectionAt = memberEntity.getLastConnectionAt();
        if (Before7Days(lastConnectionAt)) {
            return memberEntity;
        }
        return null;
    }

    private boolean Before7Days(LocalDateTime lastConnectionAt) {
        return lastConnectionAt != null && ChronoUnit.DAYS.between(lastConnectionAt, LocalDateTime.now()) > 7;
    }
}


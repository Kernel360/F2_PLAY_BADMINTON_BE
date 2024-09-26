package springproject.badmintonbatch.batch.processor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.badminton.domain.member.entity.MemberEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MemberItemProcessor implements ItemProcessor<MemberEntity, MemberEntity> {

	@Override
	public MemberEntity process(MemberEntity memberEntity) throws Exception {
		LocalDateTime lastConnectionAt = memberEntity.getLastConnectionAt();
		if (lastConnectionAt != null && ChronoUnit.DAYS.between(lastConnectionAt, LocalDateTime.now()) > 7) {
			return memberEntity;  // 삭제 대상으로 처리
		}
		return null;  // 처리할 필요 없는 경우 null 리턴
	}
}

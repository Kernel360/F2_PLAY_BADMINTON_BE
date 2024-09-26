package springproject.badmintonbatch.member;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@EnableScheduling
public class MemberBatchScheduler {

	private final MemberRepository memberRepository;

	@Scheduled(cron = "0 0 0 * * *")
	public void deleteMembersFromDb() {
		log.info("Deleting members");
		log.info("삭제 처리 중");

		List<MemberEntity> deleteMembers = memberRepository.findAllByIsDeletedTrue();

		for (MemberEntity memberEntity : deleteMembers) {
			LocalDateTime lastConnectionAt = memberEntity.getLastConnectionAt();
			if (lastConnectionAt != null) {
				if (ChronoUnit.DAYS.between(lastConnectionAt, LocalDateTime.now()) > 7) {
					memberRepository.delete(memberEntity);
					log.info("Delete member: {}", memberEntity.getProviderId());
				}
			}
		}
		log.info("Deleting members");
	}

}

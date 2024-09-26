package springproject.badmintonbatch.member;

import java.time.LocalDateTime;

import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableScheduling
public class MemberScheduled {

	private final MemberRepository memberRepository;
	private final JobLauncher jobLauncher;
	private final Job deleteMemberJob;

	@Scheduled(cron = "*/10 * * * * *")  // 10초마다 실행
	public void runDeleteMemberJob() throws Exception {
		JobParameters params = new JobParametersBuilder()
			.addString("runTime", LocalDateTime.now().toString())
			.toJobParameters();
		jobLauncher.run(deleteMemberJob, params);
	}

}

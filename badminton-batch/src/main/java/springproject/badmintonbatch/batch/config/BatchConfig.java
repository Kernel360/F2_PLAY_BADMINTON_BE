package springproject.badmintonbatch.batch.config;

import org.badminton.domain.member.entity.MemberEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Bean
	public Job deleteMemberJob(Step deleteMemberStep, JobRepository jobRepository) {
		return new JobBuilder("deleteMemberJob", jobRepository)
			.start(deleteMemberStep)
			.build();
	}

	@Bean
	public Step deleteMemberStep(ItemReader<MemberEntity> reader, ItemProcessor<MemberEntity, MemberEntity> processor,
		ItemWriter<MemberEntity> writer, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("deleteMemberStep", jobRepository)
			.<MemberEntity, MemberEntity>chunk(10, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}


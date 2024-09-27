package springproject.badmintonbatch.batch.writer;

import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberItemWriter implements ItemWriter<MemberEntity> {

	private final MemberRepository memberRepository;
	
	@Override
	public void write(Chunk<? extends MemberEntity> members) throws Exception {
		for (MemberEntity member : members) {
			if (member != null) {
				memberRepository.delete(member);
			}
		}
	}
}

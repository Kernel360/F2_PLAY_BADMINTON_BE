package springproject.badmintonbatch.batch.reader;

import java.util.List;

import org.badminton.domain.domain.member.entity.MemberEntity;
import org.badminton.domain.infrastructures.member.MemberRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberItemReader implements ItemReader<MemberEntity> {

    private final MemberRepository memberRepository;
    private List<MemberEntity> membersToDelete;

    @Override
    public MemberEntity read() {
        if (membersToDelete == null || membersToDelete.isEmpty()) {
            membersToDelete = memberRepository.findAllByIsDeletedTrue();
        }
        return membersToDelete.isEmpty() ? null : membersToDelete.remove(0);
    }
}

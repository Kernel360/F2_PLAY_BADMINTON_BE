
package org.badminton.api.aws.s3.service;

import org.badminton.api.aws.s3.model.dto.ImageUploadRequest;
import org.badminton.api.member.service.MemberService;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

@Service
public class MemberProfileImageService extends AbstractFileUploadService {

	private final MemberService memberService;
	private Long currentMemberId;

	public MemberProfileImageService(AmazonS3 s3Client, MemberService memberService) {
		super(s3Client);
		this.memberService = memberService;
	}

	public String uploadFile(ImageUploadRequest file, Long memberId) {
		this.currentMemberId = memberId;
		return super.uploadFile(file);

	}

	@Override
	public String makeFileName(String originalFilename) {
		if (this.currentMemberId == null) {
			throw new IllegalStateException("Member ID is not set. Make sure to call uploadFile method first.");
		}
		String[] originFile = originalFilename.split("\\.");
		String extension = originFile[originFile.length - 1];
		return "member-profile/" + this.currentMemberId + "/" + "image." + extension;
	}
}

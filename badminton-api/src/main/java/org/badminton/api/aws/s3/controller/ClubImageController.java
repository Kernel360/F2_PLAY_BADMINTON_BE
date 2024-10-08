package org.badminton.api.aws.s3.controller;

import org.badminton.api.aws.s3.model.dto.ClubImageUploadRequest;
import org.badminton.api.aws.s3.service.ClubImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/clubs/images")
public class ClubImageController {
	private final ClubImageService clubImageService;

	@Operation(
		summary = "클럽 이미지 업로드",
		description = "이미지를 S3에 업로드하는 API 입니다.",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			content = @Content(
				mediaType = "multipart/form-data",
				schema = @Schema(implementation = ClubImageUploadRequest.class)
			)
		)
	)
	@PostMapping
	public ResponseEntity<String> saveImage(@RequestPart("multipartFile") MultipartFile multipartFile) {
		ClubImageUploadRequest request = new ClubImageUploadRequest(multipartFile);
		return ResponseEntity.ok(clubImageService.uploadFile(request));
	}
	//TODO : 임시 테이블을 만들어 저장하고 배치에서 동호회 url 을 조회 후 미사용 객체들을 모두 삭제

}

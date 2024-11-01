package kakao.rebit.auth.event;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.s3.domain.S3Type;
import kakao.rebit.s3.dto.DownloadImageInfo;
import kakao.rebit.s3.dto.S3UploadKeyRequest;
import kakao.rebit.s3.service.S3Service;
import kakao.rebit.utils.file.FileUtil;
import kakao.rebit.utils.image.ImageDownloader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProfileImageUploadEventHandler {

    private final ImageDownloader imageDownloader;
    private final S3Service s3Service;
    private final MemberService memberService;

    public ProfileImageUploadEventHandler(ImageDownloader imageDownloader, S3Service s3Service, MemberService memberService) {
        this.imageDownloader = imageDownloader;
        this.s3Service = s3Service;
        this.memberService = memberService;
    }

    @Async
    @TransactionalEventListener
    @Transactional
    public void uploadProfileImage(RegisteredEvent event) {
        DownloadImageInfo downloadImageInfo = imageDownloader.downloadImageFromUrl(event.profileImageUrl());

        S3UploadKeyRequest s3UploadKeyRequest = s3Service.createS3UploadKeyRequestFromTypeAndFilename(S3Type.MEMBER,
                FileUtil.getFilenameFromUrl(event.profileImageUrl())); // imageUrl로부터 imageKey 및 contentType 획득

        Member member = memberService.findMemberByEmailOrThrow(event.email());
        member.changeImageKey(s3UploadKeyRequest.imageKey());

        s3Service.putObject(s3UploadKeyRequest, downloadImageInfo);
    }
}

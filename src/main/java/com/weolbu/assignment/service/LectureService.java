package com.weolbu.assignment.service;

import com.weolbu.assignment.dto.LectureCreateRequest;
import com.weolbu.assignment.entity.Lecture;
import com.weolbu.assignment.entity.Role;
import com.weolbu.assignment.entity.User;
import com.weolbu.assignment.exception.InstructorRoleRequiredException;
import com.weolbu.assignment.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final AuthService authService;
    private final LectureRepository lectureRepository;

    public void createLecture(UserDetails userDetails, LectureCreateRequest lectureRequest) {
        // 인증된 사용자 정보를 통해 User 엔티티 가져오기
        User instructor = authService.getUserFromUserDetails(userDetails);

        // 강사인지 확인
        validateInstructorRole(instructor);

        // 강의 생성 및 저장
        Lecture lecture = buildLecture(lectureRequest, instructor);
        lectureRepository.save(lecture);
    }

    // 강사인 지 확인
    private void validateInstructorRole(User user) {
        if (!user.getRole().equals(Role.INSTRUCTOR)) {
            throw new InstructorRoleRequiredException("강사만 강의를 등록할 수 있습니다.");
        }
    }

    // 강의 생성
    private Lecture buildLecture(LectureCreateRequest lectureRequest, User instructor) {
        return Lecture.builder()
                .title(lectureRequest.getTitle())
                .maxParticipants(lectureRequest.getMaxParticipants())
                .currentParticipants(0)
                .price(lectureRequest.getPrice())
                .instructor(instructor)
                .build();
    }
}
package com.project.team.plice.repository.member;


import com.project.team.plice.domain.member.Member;
import com.project.team.plice.dto.member.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional <Member> findByPhone(String phone);
    public List <Member> findByNickname(String nickname);
}

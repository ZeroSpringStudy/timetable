package org.zeropage.project.timetable.service;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.Member;
import org.zeropage.project.timetable.repository.MemberRepository;
import org.zeropage.project.timetable.repository.TimetableRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final TimetableRepository timetableRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화하는 클래스

    @Transactional
    public void save(Member member) {
        if (!memberRepository.isUserNameUseable(member.getUsername())) {
            throw new NonUniqueResultException("이미 사용중인 ID입니다.");
        }
        //member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    @Transactional
    public Member create(String username, String password) {
        Member member = new Member(username, passwordEncoder.encode(password));
        save(member);
        return member;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            return memberRepository.findByUserNameAndUserPw(userName);
            //놀랍게도 평문전송이 맞음
        } catch (NoResultException | EmptyResultDataAccessException e) {
            throw new NoResultException("ID 또는 비밀번호가 틀렸습니다.");
            //둘 중 뭐가 틀렸는지 모르도록 String 변경
        }
    }

    @Transactional
    public void delete(Member member) {
        timetableRepository.eraseMember(member);
        memberRepository.remove(member);
    }
}

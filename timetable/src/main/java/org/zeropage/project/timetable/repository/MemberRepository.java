package org.zeropage.project.timetable.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.zeropage.project.timetable.domain.Member;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public boolean isUserNameUseable(String userName) {
        return em.createQuery("select count (m) from Member m" +
                        " where m.userName=:userName", Long.class)
                .setParameter("userName", userName)
                .getSingleResult().equals(0L);
    }

    public Member findByUserNameAndUserPw(String userName, String userPw) {
        Member user = em.createQuery("select m from Member m" +
                                " where m.userName=:userName",
                        Member.class)
                .setParameter("userName", userName)
                .getSingleResult();
        if (!passwordEncoder.matches(userPw, user.getUserPW()))
            throw new NoResultException("비밀번호가 틀립니다.");
        return user;
    }

    public void remove(Member member) {
        em.remove(em.find(Member.class, member.getId()));
    }
}

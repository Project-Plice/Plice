package com.project.team.plice;

import com.project.team.plice.domain.admin.*;
import com.project.team.plice.domain.chat.ChatRoom;
import com.project.team.plice.domain.chat.MemberChatRoom;
import com.project.team.plice.domain.enums.MemberRole;
import com.project.team.plice.domain.inquire.Inquire;
import com.project.team.plice.domain.member.Member;
import com.project.team.plice.domain.post.Notice;
import com.project.team.plice.domain.post.Post;
import com.project.team.plice.domain.post.Reply;
import com.project.team.plice.repository.admin.AccessLogRepository;
import com.project.team.plice.repository.chat.ChatRoomRepository;
import com.project.team.plice.repository.data.ApartDataRepository;
import com.project.team.plice.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
//        initService.dbInit();
//        initService.adminInit();
//        initService.keywordInit();
//        initService.accessLogInit();
//        initService.postInit();
//        initService.inquiryInit();
//        initService.replyInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final BCryptPasswordEncoder passwordEncoder;
        private final MemberRepository memberRepository;
        private final ApartDataRepository apartDataRepository;
        private final ChatRoomRepository chatRoomRepository;
        private final AccessLogRepository accessLogRepository;

        public void replyInit() {
            Member[] members = new Member[3];
            members[0] = Member.builder()
                    .name("??????55")
                    .nickname("??????55")
                    .phone("01089891111")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .profileImgPath("/img/images/profile.jpg")
                    .build();
            members[1] = Member.builder()
                    .name("??????39")
                    .nickname("??????39")
                    .phone("01039399999")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .profileImgPath("/img/images/profile.jpg")
                    .build();
            members[2] = Member.builder()
                    .name("??????52")
                    .nickname("??????52")
                    .phone("01052529999")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .profileImgPath("/img/images/profile.jpg")
                    .build();

            for (Member member : members) {
                em.persist(member);
            }

            Post post = Post.builder()
                    .member(members[0])
                    .memberNickname(members[0].getNickname())
                    .title("??? ?????? ???????????? ?????? '??????'???")
                    .content("????????? ??????????????? ????????? ????????? 24??? ?????? 6??? ??????????????? ??????????????????????? ????????? ?????? 20??? ??????, " +
                            "????????? ??????????????? ?????? 15????????? ?????? 10??? ??????, ??????????????? ?????? 10????????? ?????? 5??? ???????????? ????????????.<br>" +
                            "?????? ?????? ??????????????? ?????? 9????????? ?????? 2??? ??????????????? ??????????????? ????????? ?????? ???????????? ??????????????? " +
                            "???????????? ???????????? ????????? ?????????.<br>" +
                            "?????? ????????? ??????????????? ??? ????????? ?????? ??????????????? ????????????.<br>" +
                            "?????? ???????????? ?????? ??????????????? ???????????? 10??? ?????? ?????? ?????? ???????????????.<br>" +
                            "?????? ?????????(?????????)??? ?????? ?????? ????????? ?????? 25.5???, ??????????????? ?????? 39.3????????? ????????????. " +
                            "?????????(?????????)??? ?????? 7??? ?????? ?????? ??????????????? ?????? 20.6????????? ????????????????????? ?????? 27.2?????????.<br>" +
                            "?????? ???????????? ??????????????? ?????? ??????????????? ?????? 17.8????????? ????????????????????? ?????? ?????? 26.1?????? ?????? 26.0?????????.<br>" +
                            "????????? ????????? ??????????????? ?????? ?????? 16.4?????? ?????? 25.5????????? ????????????.")
                    .build();
            em.persist(post);

            Reply[] replies = new Reply[6];

            replies[0] = Reply.builder()
                    .member(members[1])
                    .post(post)
                    .content("?????? ??? ?????? ???????????? ?????? ????????? ????????? ???????????? ?????? ?????? ?????? ???????????? ?????????, ???????????? " +
                            "?????? ??????(start) ?????? ????????? ???????????? ?????????. ?????? ????????? ?????? on ???????????? 5??? ?????? ?????? start ?????????")
                    .level(0)
                    .build();

            replies[1] = Reply.builder()
                    .member(members[2])
                    .post(post)
                    .content("???????????? ????????????.")
                    .parent(replies[0])
                    .level(1)
                    .build();

            replies[2] = Reply.builder()
                    .member(members[2])
                    .post(post)
                    .content("???????????? ?????????? ???????????????.")
                    .parent(replies[0])
                    .level(1)
                    .build();

            replies[3] = Reply.builder()
                    .member(members[1])
                    .post(post)
                    .content("???????????? ??????????????? ??????????????? ?????????????????? ????????? ???????????? ?????? ???????????? ?????????.")
                    .parent(replies[2])
                    .level(2)
                    .build();

            replies[4] = Reply.builder()
                    .member(members[0])
                    .post(post)
                    .content("????????? ???????????? ????????? ???????????? ?????????????????? ?????? ?????????")
                    .level(0)
                    .build();

            replies[5] = Reply.builder()
                    .member(members[2])
                    .post(post)
                    .content("????????? ???????????? ??????")
                    .level(0)
                    .build();

            for (Reply reply : replies) {
                em.persist(reply);
            }
        }

        public void inquiryInit() {
            Member member = Member.builder()
                    .name("????????????")
                    .nickname("????????????")
                    .phone("01062629999")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .profileImgPath("/img/images/profile.jpg")
                    .build();
            em.persist(member);

            List<Inquire> inquires = new ArrayList<>();
            for (int i = 1; i < 30; i++) {
                String type = "";
                switch (i % 4) {
                    case 0:
                        type = "????????? ????????????";
                        break;
                    case 1:
                        type = "?????? ????????????";
                        break;
                    case 2:
                        type = "?????? ??????";
                        break;
                    case 3:
                        type = "??????/?????? ??????";
                        break;
                }
                inquires.add(Inquire.builder()
                        .member(member)
                        .title("????????? ?????? " + i)
                        .content("????????? ????????? ?????? " + i)
                        .type(type)
                        .build());
            }
            inquires.forEach(inquire -> em.persist(inquire));
        }

        public void accessLogInit() {
            IP ip = IP.builder().ip("192.168.1.1").build();
            em.persist(ip);

            Member member = Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01022332323")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .profileImgPath("/img/images/profile.jpg")
                    .build();
            em.persist(member);

            List<AccessLog> accessLogs = new ArrayList<>();
            int day = 1000;

            while (day-- > 0) {
                for (int pageNum = 1; pageNum < 6; pageNum++) {
                    int randomPageCount = (int) (Math.random() * 6 + 5);
                    if (day > 365 && day < 730) {
                        switch (pageNum) {
                            case 1:
                                randomPageCount += 2;
                                break;
                            case 2:
                                randomPageCount += 4;
                                break;
                            case 3:
                                randomPageCount += 1;
                                break;
                            case 4:
                                randomPageCount += 3;
                                break;
                            case 5:
                                randomPageCount += 0;
                                break;
                        }
                    } else if (day < 366) {
                        switch (pageNum) {
                            case 1:
                                randomPageCount += 1;
                                break;
                            case 2:
                                randomPageCount += 5;
                                break;
                            case 3:
                                randomPageCount += 3;
                                break;
                            case 4:
                                randomPageCount += 2;
                                break;
                            case 5:
                                randomPageCount += 4;
                                break;
                        }
                    } else {
                        switch (pageNum) {
                            case 1:
                                randomPageCount += 1;
                                break;
                            case 2:
                                randomPageCount += 2;
                                break;
                            case 3:
                                randomPageCount += 4;
                                break;
                            case 4:
                                randomPageCount += 0;
                                break;
                            case 5:
                                randomPageCount += 3;
                                break;
                        }
                    }
                    if (((day / 30) % 12) % 2 == 0) {
                        switch (pageNum) {
                            case 1:
                                randomPageCount += 1;
                                break;
                            case 2:
                                randomPageCount += 2;
                                break;
                            case 3:
                                randomPageCount += 4;
                                break;
                            case 4:
                                randomPageCount += 0;
                                break;
                            case 5:
                                randomPageCount += 3;
                                break;
                        }
                    } else if (((day / 30) % 12) % 3 == 0) {
                        switch (pageNum) {
                            case 1:
                                randomPageCount += 2;
                                break;
                            case 2:
                                randomPageCount += 4;
                                break;
                            case 3:
                                randomPageCount += 1;
                                break;
                            case 4:
                                randomPageCount += 3;
                                break;
                            case 5:
                                randomPageCount += 0;
                                break;
                        }
                    }
                    if (day % 2 == 0) randomPageCount += 2;
                    if (day % 3 == 0) randomPageCount += 2;
                    String uri = "";

                    while (randomPageCount-- > 0) {
                        switch (pageNum) {
                            case 1:
                                uri = "/home";
                                break;
                            case 2:
                                uri = "/map";
                                break;
                            case 3:
                                uri = "/chat";
                                break;
                            case 4:
                                uri = "/post";
                                break;
                            case 5:
                                uri = "/contents";
                                break;
                        }
                        accessLogs.add(
                                AccessLog.builder()
                                        .ip(ip)
                                        .uri(uri)
                                        .member(member)
                                        .regDate(LocalDateTime.now().minusDays(day))
                                        .build());
                    }
                }
            }
            accessLogs.forEach(accessLog -> em.persist(accessLog));
        }

        public void adminInit() {
            List<AdminTeam> adminTeams = new ArrayList<>();
            adminTeams.add(AdminTeam.builder().name("???????????????").build());
            adminTeams.add(AdminTeam.builder().name("???????????????").build());
            adminTeams.forEach(adminTeam -> em.persist(adminTeam));

            List<Member> admins = new ArrayList<>();

            admins.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01032100575")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.SUPER_ADMIN)
                    .adminTeam(adminTeams.get(0))
                    .build());

            admins.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01036198976")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.ADMIN)
                    .adminTeam(adminTeams.get(0))
                    .build());

            admins.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01033149467")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.ADMIN)
                    .adminTeam(adminTeams.get(0))
                    .build());

            admins.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01051025975")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.ADMIN)
                    .adminTeam(adminTeams.get(0))
                    .build());

            admins.add(Member.builder()
                    .name("????????????")
                    .nickname("????????????")
                    .phone("01077771234")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.ADMIN)
                    .adminTeam(adminTeams.get(1))
                    .build());

            admins.add(Member.builder()
                    .name("????????????")
                    .nickname("????????????")
                    .phone("01077772345")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.ADMIN)
                    .adminTeam(adminTeams.get(1))
                    .build());

            admins.add(Member.builder()
                    .name("????????????")
                    .nickname("????????????")
                    .phone("01077773456")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.ADMIN)
                    .adminTeam(adminTeams.get(1))
                    .build());

            admins.add(Member.builder()
                    .name("????????????")
                    .nickname("????????????")
                    .phone("01077774567")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.ADMIN)
                    .adminTeam(adminTeams.get(1))
                    .build());

            admins.forEach(admin -> {
                em.persist(admin);
                em.persist(Authority.builder()
                        .member(admin)
                        .adminMng(true)
                        .memberMng(true)
                        .chatMng(true)
                        .postMng(true)
                        .inquiryMng(true)
                        .build());
            });
        }

        public void keywordInit() {
            List<SearchKeyword> keywords = new ArrayList<>();
            keywords.add(SearchKeyword.builder().keyword("????????? ?????????").count(62).build());
            keywords.add(SearchKeyword.builder().keyword("???????????? ?????????").count(30).build());
            keywords.add(SearchKeyword.builder().keyword("????????????????????????").count(20).build());
            keywords.add(SearchKeyword.builder().keyword("????????? ?????????").count(12).build());
            keywords.add(SearchKeyword.builder().keyword("????????? ?????????").count(10).build());
            keywords.add(SearchKeyword.builder().keyword("???????????????").count(8).build());
            keywords.add(SearchKeyword.builder().keyword("??????????????????").count(5).build());
            keywords.add(SearchKeyword.builder().keyword("????????? ????????? ?????????").count(3).build());
            keywords.forEach(e -> em.persist(e));
        }

        public void postInit() {
            Member member = Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01028228888")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .profileImgPath("/img/images/profile.jpg")
                    .build();

            Member admin = Member.builder()
                    .name("????????????")
                    .nickname("????????????")
                    .phone("01022028888")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .profileImgPath("/img/images/profile.jpg")
                    .build();

            em.persist(member);
            em.persist(admin);

            List<Post> posts = new ArrayList<>();
            for (int i = 1; i < 30; i++) {
                posts.add(Post.builder()
                        .title("????????? ?????? ?????? " + i)
                        .content("????????? ?????? ?????? " + i)
                        .member(member)
                        .memberNickname(member.getNickname())
                        .build());
            }
            posts.forEach(post -> em.persist(post));

            List<Notice> notices = new ArrayList<>();
            for (int i = 1; i < 30; i++) {
                notices.add(Notice.builder()
                        .title("???????????? ?????? ?????? " + i)
                        .content("???????????? ?????? ?????? " + i)
                        .member(admin)
                        .memberNickname(admin.getNickname())
                        .build());
            }
            notices.forEach(notice -> em.persist(notice));
        }

        public void dbInit() {

            List<Member> members = new ArrayList<>();

            members.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01012345678")
                    .birth("900101")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .profileImgPath("/img/images/profile.jpg")
                    .build());

            members.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01043214321")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .build());

            members.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01022223333")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .build());

            members.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01088887777")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .build());

            members.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01077776666")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .build());

            members.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01033334444")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .build());

            members.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01066665555")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .build());

            members.add(Member.builder()
                    .name("?????????")
                    .nickname("?????????")
                    .phone("01099998888")
                    .birth("999999")
                    .pw(passwordEncoder.encode("1234"))
                    .role(MemberRole.USER)
                    .build());

            members.forEach(member -> em.persist(member));

            List<ChatRoom> chatRooms = apartDataRepository.findAll().stream().map(apartData ->
                    ChatRoom.builder().apartData(apartData).build()).collect(Collectors.toList());

            chatRooms.get(10000).changeMemberCount(8);
            chatRooms.get(5000).changeMemberCount(6);
            chatRooms.get(1000).changeMemberCount(1);
            chatRooms.forEach(e -> em.persist(e));

            List<MemberChatRoom> memberChatRooms = new ArrayList<>();

            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(10000)).member(members.get(0)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(10000)).member(members.get(1)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(10000)).member(members.get(2)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(10000)).member(members.get(3)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(10000)).member(members.get(4)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(10000)).member(members.get(5)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(10000)).member(members.get(6)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(10000)).member(members.get(7)).build());

            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(5000)).member(members.get(0)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(5000)).member(members.get(1)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(5000)).member(members.get(2)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(5000)).member(members.get(3)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(5000)).member(members.get(4)).build());
            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(5000)).member(members.get(5)).build());

            memberChatRooms.add(MemberChatRoom.builder().chatRoom(chatRooms.get(1000)).member(members.get(0)).build());

            memberChatRooms.forEach(memberChatRoom -> em.persist(memberChatRoom));
        }
    }
}

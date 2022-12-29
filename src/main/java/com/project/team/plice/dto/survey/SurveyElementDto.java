package com.project.team.plice.dto.survey;

import com.project.team.plice.domain.member.Member;
import com.project.team.plice.domain.survey.Survey;
import com.project.team.plice.domain.survey.SurveyElement;
import lombok.Builder;
import lombok.Data;

@Data
public class SurveyElementDto {

    private Long id;
    private Survey survey;
    private Member member;
    private String content;
    private Integer select;

    @Builder
    public SurveyElementDto(Long id, Survey survey, Member member, String content, Integer select) {
        this.id = id;
        this.survey = survey;
        this.member = member;
        this.content = content;
        this.select = select;
    }

    public SurveyElement toEntity(){
        return SurveyElement.builder()
                .id(this.id)
                .survey(this.survey)
                .member(this.member)
                .content(this.content)
                .select(this.select)
                .build();
    }
}
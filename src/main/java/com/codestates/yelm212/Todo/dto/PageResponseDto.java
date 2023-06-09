package com.codestates.yelm212.Todo.dto;

import com.codestates.yelm212.Todo.member.entity.Member;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class PageResponseDto<T> {

    private List<T> data;
    private PageInfo pageInfo;

    public PageResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

}

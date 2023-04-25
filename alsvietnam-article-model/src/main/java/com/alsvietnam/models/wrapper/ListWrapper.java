package com.alsvietnam.models.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListWrapper<T> {

    private long total;

    @JsonProperty("max_result")
    private long maxResult;

    @JsonProperty("current_page")
    private long currentPage;

    @JsonProperty("total_page")
    private long totalPage;

    private List<T> data;

}

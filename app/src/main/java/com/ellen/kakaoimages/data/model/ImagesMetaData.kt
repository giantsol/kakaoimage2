package com.ellen.kakaoimages.data.model

data class ImagesMetaData(
    // 검색된 문서 수
    val total_count: Int,
    // total_count 중 노출 가능 문서 수
    val pageable_count: Int,
    // 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    val is_end: Boolean
)
package com.baghdad.repository.mapper

import com.baghdad.domain.model.PagedResult
import com.baghdad.repository.model.PagedResultDto

fun <DTO, ENTITY> PagedResultDto<DTO>.toPagedResult(
    dataMapper: (DTO) -> ENTITY
): PagedResult<ENTITY> {
    return PagedResult(
        data = data.map(dataMapper),
        nextKey = nextKey,
        prevKey = prevKey
    )
}
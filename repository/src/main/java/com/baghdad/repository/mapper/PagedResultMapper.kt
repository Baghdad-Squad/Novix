package com.baghdad.repository.mapper

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.repository.model.PagedResultDto

fun <DTO, ENTITY> PagedResultDto<DTO>.toPagedResult(
    dataMapper: (DTO) -> ENTITY
): PagedResult<ENTITY> {
    return PagedResult<ENTITY>(
        data = this.data.map(dataMapper),
        nextKey = this.nextKey,
        prevKey = this.prevKey
    )
}
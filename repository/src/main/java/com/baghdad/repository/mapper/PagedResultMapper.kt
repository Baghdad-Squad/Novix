package com.baghdad.repository.mapper

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.repository.model.PagedResultDto

fun <DTO, ENTITY> PagedResultDto<DTO>.toPagedResult(
    dataMapper: (DTO) -> ENTITY
): PagedResult<ENTITY> {
    return PagedResult(
        data = this.data.map(dataMapper),
        nextPage = this.nextKey,
        prevPage = this.prevKey
    )
}
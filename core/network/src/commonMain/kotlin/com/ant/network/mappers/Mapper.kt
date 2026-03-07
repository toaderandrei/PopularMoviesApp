package com.ant.network.mappers

/**
 * Generic interface for mapping network DTOs to domain models.
 *
 * @param F the source (from) type, typically a DTO.
 * @param T the target (to) type, typically a domain model.
 */
interface Mapper<F, T> {
    /** Maps [from] to the target type [T]. */
    suspend fun map(from: F): T
}

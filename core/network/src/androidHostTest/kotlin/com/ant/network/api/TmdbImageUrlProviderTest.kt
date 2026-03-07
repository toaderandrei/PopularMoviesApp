package com.ant.network.api

import kotlin.test.Test
import kotlin.test.assertEquals

class TmdbImageUrlProviderTest {

    @Test
    fun `Should build correct URL with backdrop size`() {
        val url = TmdbImageUrlProvider.getUrl("/abc123.jpg", BackdropSizes.W1280.wSize)

        assertEquals("https://image.tmdb.org/t/p/w1280//abc123.jpg", url)
    }

    @Test
    fun `Should build correct URL with poster size`() {
        val url = TmdbImageUrlProvider.getUrl("/poster.jpg", PosterSizes.W342.wSize)

        assertEquals("https://image.tmdb.org/t/p/w342//poster.jpg", url)
    }

    @Test
    fun `Should build correct URL with original size`() {
        val url = TmdbImageUrlProvider.getUrl("/image.jpg", BackdropSizes.ORIGINAL.wSize)

        assertEquals("https://image.tmdb.org/t/p/original//image.jpg", url)
    }

    @Test
    fun `Should handle all backdrop sizes`() {
        assertEquals("w300", BackdropSizes.W300.wSize)
        assertEquals("w780", BackdropSizes.W780.wSize)
        assertEquals("w1280", BackdropSizes.W1280.wSize)
        assertEquals("original", BackdropSizes.ORIGINAL.wSize)
    }

    @Test
    fun `Should handle all poster sizes`() {
        assertEquals("w92", PosterSizes.W92.wSize)
        assertEquals("w154", PosterSizes.W154.wSize)
        assertEquals("w185", PosterSizes.W185.wSize)
        assertEquals("w342", PosterSizes.W342.wSize)
        assertEquals("w500", PosterSizes.W500.wSize)
        assertEquals("w780", PosterSizes.W780.wSize)
        assertEquals("original", PosterSizes.ORIGINAL.wSize)
    }
}

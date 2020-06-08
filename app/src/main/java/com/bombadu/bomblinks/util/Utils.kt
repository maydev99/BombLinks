package com.bombadu.bomblinks.util

import org.nibor.autolink.LinkExtractor
import org.nibor.autolink.LinkType
import java.util.*

class Utils {

    fun extractUrl(myUrl: String): String {
        val linkExtractor = LinkExtractor.builder()
            .linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW, LinkType.EMAIL))
            .build()

        val links = linkExtractor.extractLinks(myUrl)
        val link = links.iterator().next()
        link.type
        link.beginIndex
        link.endIndex
        return myUrl.substring(link.beginIndex, link.endIndex)

    }
}
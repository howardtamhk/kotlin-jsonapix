package com.infinum.jsonapix.processor.specs.model

import com.infinum.jsonapix.core.common.JsonApiConstants
import com.infinum.jsonapix.core.common.JsonApiConstants.withName
import com.infinum.jsonapix.core.resources.Error
import com.infinum.jsonapix.core.resources.Links
import com.infinum.jsonapix.core.resources.Meta
import com.infinum.jsonapix.processor.LinksInfo
import com.infinum.jsonapix.processor.MetaInfo
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName

internal object JsonApiListSpecBuilder : BaseJsonApiModelSpecBuilder() {
    override fun getClassSuffixName(): String = JsonApiConstants.Suffix.JSON_API_LIST

    override fun getRootClassName(rootType: ClassName): TypeName {
        val itemType = ClassName.bestGuess(rootType.canonicalName.withName(JsonApiConstants.Suffix.JSON_API_LIST_ITEM))
        return List::class.asClassName().parameterizedBy(itemType)
    }

    override fun getParams(className: ClassName, isRootNullable: Boolean, metaInfo: MetaInfo?, linksInfo: LinksInfo?): List<ParameterSpec> {
        return listOf(
            JsonApiConstants.Keys.DATA.asParam(
                getRootClassName(className),
                isRootNullable,
                JsonApiConstants.Defaults.NULL.takeIf { isRootNullable }
            ),
            JsonApiConstants.Members.ROOT_LINKS.asParam(Links::class.asClassName(), true, JsonApiConstants.Defaults.NULL),
            JsonApiConstants.Keys.ERRORS.asParam(
                List::class.asClassName().parameterizedBy(Error::class.asClassName()),
                true,
                JsonApiConstants.Defaults.NULL
            ),
            JsonApiConstants.Members.ROOT_META.asParam(
                metaInfo?.rootClassName ?: Meta::class.asClassName(),
                true,
                JsonApiConstants.Defaults.NULL
            ),
        )
    }
}

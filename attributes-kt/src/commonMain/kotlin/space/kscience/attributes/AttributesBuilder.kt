/*
 * Copyright 2018-2023 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.attributes

/**
 * A safe builder for [Attributes]
 *
 * @param O type marker of an owner object, for which these attributes are made
 */
public class AttributesBuilder<out O> internal constructor(
    private val map: MutableMap<Attribute<*>, Any?>,
) : Attributes {

    public constructor() : this(mutableMapOf())

    override val content: Map<out Attribute<*>, Any?> get() = map

    public operator fun <T> set(attribute: Attribute<T>, value: T?) {
        if (value == null) {
            map.remove(attribute)
        } else {
            map[attribute] = value
        }
    }

    public operator fun <V> Attribute<V>.invoke(value: V?) {
        set(this, value)
    }

    public fun from(attributes: Attributes) {
        map.putAll(attributes.content)
    }

    public fun <V> SetAttribute<V>.add(
        attrValue: V,
    ) {
        val currentSet: Set<V> = get(this) ?: emptySet()
        map[this] = currentSet + attrValue
    }

    /**
     * Remove an element from [SetAttribute]
     */
    public fun <V> SetAttribute<V>.remove(
        attrValue: V,
    ) {
        val currentSet: Set<V> = get(this) ?: emptySet()
        map[this] = currentSet - attrValue
    }

    public fun build(): Attributes = AttributesImpl(map)
}

public inline fun <O> Attributes(builder: AttributesBuilder<O>.() -> Unit): Attributes =
    AttributesBuilder<O>().apply(builder).build()
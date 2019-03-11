package com.avd.data

import com.avd.exception.ItemNotFoundException
import com.avd.model.Model

interface Repository<T : Model> {

    /**
     * @return return item from repository by [id]
     */
    @Throws(ItemNotFoundException::class)
    fun get(id: Long): T

    /**
     * Change item in repository or insert new item if item not exists
     */
    fun change(item: T)

    /**
     * Delete item from repository by [id]
     */
    fun delete(id: Long)

    /**
     * @return return all items from repository
     */
    fun items(): Collection<T>
}
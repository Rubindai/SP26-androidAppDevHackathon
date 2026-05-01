package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Block
import com.example.myapplication.data.model.BlockCategory
import com.example.myapplication.data.model.Day
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor() {
    private val _addedIds = MutableStateFlow(setOf("INFO2300", "HIST1530", "SPAN2090"))
    val addedIds = _addedIds.asStateFlow()

    private val _blocks = MutableStateFlow(initialBlocks)
    val blocks = _blocks.asStateFlow()

    fun toggleAdded(id: String) {
        _addedIds.update { current ->
            if (id in current) current - id else current + id
        }
    }

    fun upsertBlock(block: Block) {
        _blocks.update { current ->
            val index = current.indexOfFirst { it.id == block.id }
            if (index == -1) current + block else current.toMutableList().apply { this[index] = block }
        }
    }

    fun deleteBlock(id: String) {
        _blocks.update { blocks -> blocks.filterNot { it.id == id } }
    }

    private companion object {
        val initialBlocks = listOf(
            Block("seed-0", Day.MON, 16f, 18f, "Club: Cornell Outing", BlockCategory.CLUB),
            Block("seed-1", Day.WED, 16f, 18f, "Club: Cornell Outing", BlockCategory.CLUB),
            Block("seed-2", Day.TUE, 15f, 17f, "Work study", BlockCategory.WORK),
            Block("seed-3", Day.THU, 15f, 17f, "Work study", BlockCategory.WORK),
        )
    }
}

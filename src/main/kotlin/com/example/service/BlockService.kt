package com.example.service

import com.example.data.repository.block.BlockRepository
import com.example.exceptions.ConflictException

class BlockService(
    private val blockRepository: BlockRepository
) {

    suspend fun blockUser(userId: Int, userIdToBlock: Int) {

        if (userId == userIdToBlock) {
            throw ConflictException("You cannot block yourself.")
        }

        blockRepository.blockUser(userId, userIdToBlock)
    }


    suspend fun unblockUser(userId: Int, userIdToUnblock: Int) {
        blockRepository.unblockUser(userId, userIdToUnblock)
    }

}
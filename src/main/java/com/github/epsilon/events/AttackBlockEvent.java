package com.github.epsilon.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class AttackBlockEvent extends Event implements ICancellableEvent {

    private BlockPos blockPos;
    private Direction direction;

    public AttackBlockEvent(BlockPos blockPos, Direction direction) {
        this.blockPos = blockPos;
        this.direction = direction;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}

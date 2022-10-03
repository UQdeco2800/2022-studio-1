package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;

public class NPCMovementTask extends DefaultTask implements PriorityTask {
    public NPCMovementTask(){}

    @Override
    public int getPriority(){
        return 1;
    }

    @Override
    public void start(){
        super.start();
        this.owner.getEntity().getEvents().addListener("movingNPC", this::MovingNpc);
        //owner.getEntity().getEvents().trigger("NPC");
    }

    @Override
    public void update(){
        owner.getEntity().getEvents().trigger("NPC");
    }

    private void MovingNpc(){
        owner.getEntity().getEvents().trigger("NPC");
    }
}

package com.deco2800.game.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

import com.deco2800.game.entities.NpcService;
import org.junit.Test;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.Tile;

import java.util.HashMap;
import java.util.Map;

public class NPC {


    @Test
    public void testSetNPCNum(){
        NpcService npcService = new NpcService();

        //default value is 0
        assertEquals(0, npcService.getNpcNum());

        npcService.setNpcNum(10);
        assertEquals(10, npcService.getNpcNum());

        //NPC num unchanged as input is invalid
        npcService.setNpcNum(-5);
        assertEquals(10, npcService.getNpcNum());

    }


    @Test
    public void testRegisterNPC() {
        //Create a mocked entity
        NpcService npcService = new NpcService();
        Entity entity = spy(Entity.class);
        npcService.register(entity);
        assertNotNull( npcService.getLastEntity());

        npcService.registerNamed("NPC",entity);
        assertNotNull(npcService.getNamedEntity("NPC"));

        Entity entity1 = spy(Entity.class);
        npcService.registerNamed("NPC1",entity1);
        Map<String, Entity>npc_list = new HashMap<String, Entity>();
        npc_list.put("NPC1",entity1);
        npc_list.put("NPC",entity);

        assertEquals(npc_list,npcService.getAllNamedEntities());
    }

//    @Test
//    public void testNPCClicked() {
//        //Create a mocked entity
//        NpcService npcService = new NpcService();
//        Entity npc0 = spy(Entity.class);
//        Entity npc1 = spy(Entity.class);
//
//        npcService.registerNamed("0",npc0);
//        npcService.registerNamed("1",npc1);
//        npcService.setNpcNum(2);
//        npc0.setPosition(50,50);
//        npc1.setPosition(10,10);
//
//        // due to the UGS relationship between mouse position and npc position is abnormal
//        // player is clicking npc if mouse position is (xPos+1 < mousePosV2.x && mousePosV2.x < xPos+3) &
//        // (yPos+0.5 < mousePosV2.y && mousePosV2.y < yPos+4)
//
//        NpcService.npcClicked(50,50);
//
//
//
//
//
//    }




}

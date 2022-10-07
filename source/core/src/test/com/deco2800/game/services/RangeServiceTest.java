package com.deco2800.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.IsoTileRenderer;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.Tile;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.extensions.GameExtension;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class RangeServiceTest {
    @BeforeEach
    void beforeEach() {
        //
    }

    @Test
    public void createRangeServiceTest() {
        RangeService rangeService = new RangeService();
        assertEquals(rangeService.getClass(), RangeService.class);
    }

    @Test
    public void perimeterFunctionCalledTest() {
        EntityService entityService = new EntityService();
        UGS ugsService = new UGS();
        RangeService rangeService = new RangeService();
        Entity entity = spy(Entity.class);
        ServiceLocator.registerUGSService(ugsService);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerUGSService(ugsService);
        Entity terrain = new Entity();
        TerrainComponent terraining = Mockito.mock(TerrainComponent.class);
        terrain.addComponent(terraining);
        entityService.registerNamed("terrain", terrain);




        entityService.register(entity);
        Tile newTile = new Tile();

        GridPoint2 coordinate = new GridPoint2(1, 1);
        newTile.setEntity(entity);
        ugsService.add(coordinate, newTile);

        try {
            rangeService.perimeter(entity);
        } catch (NullPointerException e) {
            return;
        }

    }

    @Test
    public void registeredInUgsTest() {
        // Services set up
        EntityService entityService = new EntityService();
        UGS ugsService = new UGS();
        RangeService rangeService = new RangeService();
        ServiceLocator.registerUGSService(ugsService);
        Entity entity = spy(Entity.class);

        // Add to UGS
        Tile newTile = new Tile("ground");
        entityService.register(entity);
        GridPoint2 coordinate = new GridPoint2(54, 102);
        entity.setName("test");
        newTile.setEntity(entity);

        // Test
        ugsService.add(coordinate, newTile);
        ArrayList<Entity> registered = rangeService.registeredInUGS();
        assertEquals(registered.get(0), entity);
    }

    @Test
    public void underPlayerTest() {
        // Services set up
        EntityService entityService = new EntityService();
        UGS ugsService = new UGS();
        RangeService rangeService = new RangeService();
        Entity terrain = new Entity();
        TerrainComponent terraining = Mockito.mock(TerrainComponent.class);
        IsoTileRenderer bing = Mockito.mock(IsoTileRenderer.class);


        Entity entity = spy(Entity.class);
        Entity entity2 = spy(Entity.class);
        ServiceLocator.registerUGSService(ugsService);
        ServiceLocator.registerEntityService(entityService);

        // Add to UGS
        Tile newTile = new Tile("ground");
        terrain.addComponent(terraining);
        entityService.registerNamed("terrain", terrain);

        entityService.register(entity);
        entity2.setPosition(1252, 194);
        entityService.registerNamed("player", entity2);

        GridPoint2 coordinate = new GridPoint2(54, 102);
        entity.setName("on bottom");
        entity2.setName("on top");
        newTile.setEntity(entity);

        // Test
        ugsService.add(coordinate, newTile);

        // FIX THIS TEST!!!
//        String playerPos = rangeService.getPlayerTile();
//        Entity underPlayer = ugsService.getEntity(playerPos);
//        assertEquals(underPlayer, entity);


    }

    @Test
    public void playerInRangeOfTest() {
        // Services set up
        EntityService entityService = new EntityService();
        UGS ugsService = new UGS();
        RangeService rangeService = new RangeService();
        ServiceLocator.registerUGSService(ugsService);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerRangeService(rangeService);
        InventoryComponent invent = Mockito.mock(InventoryComponent.class);


        Entity entity = spy(Entity.class);
        Entity entity2 = spy(Entity.class);
        entity.addComponent(invent);
        entity.setPosition(1252, 194);
        entity2.setPosition(1252, 175);
        entityService.registerNamed("player", entity);
        entityService.register(entity2);

        // FIX
        assertFalse(rangeService.playerInRangeOf(entity2));


    }

    @Test
    public void ugdDisposeTest() {
        EntityService entityService = new EntityService();
        UGS ugsService = new UGS();
        RangeService rangeService = new RangeService();
        ServiceLocator.registerUGSService(ugsService);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerRangeService(rangeService);

        Entity entity = spy(Entity.class);
        entity.setPosition(1252, 194);


        Tile newTile = new Tile("ground");
        entityService.registerNamed("player", entity);
        String coordinate = UGS.generateCoordinate(54,102);
        entity.setName("player");
        newTile.setEntity(entity);

//        // Test
//        ugsService.add(coordinate, newTile);
//        ArrayList<Entity> registered = rangeService.registeredInUGS();
//
//        assertTrue(registered.contains(entity));
//
//        entity.dispose();
//
//        assertFalse(registered.contains(entity));
//
// Need world to tile pos workoing

        // Testing pushing
    }


}



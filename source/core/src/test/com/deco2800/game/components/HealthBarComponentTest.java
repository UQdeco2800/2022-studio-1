package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.RenderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import util.EntityUtil;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(GameExtension.class)
@RunWith(MockitoJUnitRunner.class)
class HealthBarComponentTest {

    private RenderUtil renderUtil;

    private RenderService renderService;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerRenderService(new RenderService());
        renderUtil = Mockito.spy(RenderUtil.getInstance());
        renderService = Mockito.spy(ServiceLocator.getRenderService());

    }

//    @Test
//    public void shouldAdjustProgressBarCorrectlyWhenDamageInflicted() {
//        short commonLayer = (1 << 3);
//        HealthBarComponent healthBarComponent = new HealthBarComponent(100, 20);
//
//        // Mock out register so no renderables are registered // ignore warning will not be called
//        Mockito.doNothing().when(renderService).register(null);
//
//        // Stub getPixelsPerUnit()
//        doReturn(1f).when(renderUtil).getPixelsPerUnit();
//
//        // Use stubbed renderUtil
//        healthBarComponent.setRenderUtil(renderUtil);
//
//        Entity attacker = EntityUtil.createAttacker(commonLayer);
//        Entity target = EntityUtil.createTargetWithHealthBarComponent(commonLayer,100, healthBarComponent);
//
//        Fixture entityFixture = attacker.getComponent(HitboxComponent.class).getFixture();
//        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
//        attacker.getEvents().trigger("collisionStart", entityFixture, targetFixture);
//
//        target.getComponent(HealthBarComponent.class).draw(null); // fake render
//        assertTrue(healthBarComponent.getProgressBar().getValue() < 1f);
//    }
}
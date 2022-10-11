package com.deco2800.game.services;

import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.NpcService;
import com.deco2800.game.entities.StructureService;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services.
 * Warning: global access is a trap and should be used <i>extremely</i> sparingly.
 * Read the wiki for details (https://github.com/UQdeco2800/game-engine/wiki/Service-Locator).
 */
public class ServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
  private static EntityService entityService;
  private static RenderService renderService;
  private static RangeService rangeService;
  private static PhysicsService physicsService;
  private static GameTime timeSource;
  private static InputService inputService;
  private static ResourceService resourceService;
  private static StructureService structureService;
  private static DayNightCycleService dayNightCycleService;
  private static UGS ugsService;
  private static ResourceManagementService resourceManagementService;
  private static AchievementHandler achievementHandler;
  private static NpcService NpcService;

  public static UGS getUGSService() {return ugsService;}

  public static EntityService getEntityService() {
    return entityService;
  }

  public static RangeService getRangeService() {
    return rangeService;
  }

  public static RenderService getRenderService() {
    return renderService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }

  public static GameTime getTimeSource() {
    return timeSource;
  }

  public static InputService getInputService() {
    return inputService;
  }

  public static ResourceService getResourceService() {
    return resourceService;
  }

  public static ResourceManagementService getResourceManagementService() {
    return resourceManagementService;
  }

  public static StructureService getStructureService() { return structureService; }


  public static DayNightCycleService getDayNightCycleService () {

    return dayNightCycleService;
  }

  public static AchievementHandler getAchievementHandler() {
    return achievementHandler;
  }

  public static NpcService getNpcService () { return NpcService; }


  public static void registerEntityService(EntityService service) {
    logger.debug("Registering entity service {}", service);
    entityService = service;
  }

  public static void registerRangeService(RangeService service) {
    logger.debug("Registering entity service {}", service);
    rangeService = service;
  }

  public static void registerRenderService(RenderService service) {
    logger.debug("Registering render service {}", service);
    renderService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    logger.debug("Registering physics service {}", service);
    physicsService = service;
  }

  public static void registerTimeSource(GameTime source) {
    logger.debug("Registering time source {}", source);
    timeSource = source;
  }

  public static void registerInputService(InputService source) {
    logger.debug("Registering input service {}", source);
    inputService = source;
  }

  public static void registerResourceService(ResourceService source) {
    logger.debug("Registering resource service {}", source);
    resourceService = source;
  }

  public static void registerStructureService( StructureService source) {
    logger.debug("Registering structure service {}", source);
    structureService = source;
  }

  public static void registerUGSService( UGS source) {
    logger.debug("Registering structure service {}", source);
    ugsService = source;
  }

    public static void registerDayNightCycleService(DayNightCycleService source) {
    logger.debug("Registering day night cycle service {}", source);
    dayNightCycleService = source;
  }
  public static void registerResourceManagementService(ResourceManagementService source) {
    logger.debug("Registering resource management service {}", source);
    resourceManagementService = source;
  }

public static void registerAchievementHandler(AchievementHandler source){
    achievementHandler = source;
}

  public static void registerNpcService(NpcService source) {
    logger.debug("Registering Npc service {}", source);
    NpcService = source;
  }

  public static void clear() {
    entityService = null;
    renderService = null;
    physicsService = null;
    timeSource = null;
    inputService = null;
    resourceService = null;
    resourceManagementService = null;
    NpcService = null;

    if (dayNightCycleService != null) {
      dayNightCycleService.stop();
    }

    dayNightCycleService = null;
  }

  private ServiceLocator() {
    throw new IllegalStateException("Instantiating static util class");
  }

  public static void setNpcService(com.deco2800.game.entities.NpcService npcService) {
    NpcService = npcService;
  }
}

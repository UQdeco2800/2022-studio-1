package com.deco2800.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.ComponentType;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Core entity class. Entities exist in the game and are updated each frame. All
 * entities have a
 * position and scale, but have no default behaviour. Components should be added
 * to an entity to
 * give it specific behaviour. This class should not be inherited or modified
 * directly.
 *
 * <p>
 * Example use:
 *
 * <pre>
 * Entity player = new Entity()
 *     .addComponent(new RenderComponent())
 *     .addComponent(new PlayerControllerComponent());
 * ServiceLocator.getEntityService().register(player);
 * </pre>
 */
public class Entity {
  private static final Logger logger = LoggerFactory.getLogger(Entity.class);
  private static int nextId = 0;
  private static final String EVT_NAME_POS = "setPosition";

  private String name;
  private String creationMethod;
  private final int id;
  private Boolean collectable = false;
  private ResourceType resourceType;
  private int resourceAmount;
  private final IntMap<Component> components;
  private final EventHandler eventHandler;
  private boolean enabled = true;
  private boolean created = false;
  private Vector2 position = Vector2.Zero.cpy();
  private Vector2 scale = new Vector2(1, 1);
  private Array<Component> createdComponents;

  private Vector2 newPosition;
  private boolean tweening;
  private Vector2 tweeningVector = new Vector2();
  private int rotation = -1;

  // TODO: Fix Comment Array. Make HashMap<String, Component> so to be able to
  // search for a specific component
  public Entity() {
    id = nextId;
    nextId++;

    components = new IntMap<>(4);
    eventHandler = new EventHandler();
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setRotation(int rotation) {
    this.rotation = rotation;
  }

  public int getRotation() {
    return this.rotation;
  }

  public void setCreationMethod(String classMethod) {
    this.creationMethod = classMethod;
  }

  public String getCreationMethod(){
    return this.creationMethod;
  }

  /**
   * Enable or disable an entity. Disabled entities do not run update() or
   * earlyUpdate() on their
   * components, but can still be disposed.
   *
   * @param enabled true for enable, false for disable.
   */
  public void setEnabled(boolean enabled) {
    logger.debug("Setting enabled={} on entity {}", enabled, this);
    this.enabled = enabled;
  }

  public boolean isCollectable() {
    return collectable;
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  public void collectResources() {
    MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).addResources(resourceType,
        resourceAmount);
  }

  public void setResourceType(ResourceType resourceType) {
    this.resourceType = resourceType;
  }

  public void setCollectable(Boolean collectable) {
    this.collectable = collectable;
  }

  public void setResourceAmount(int resourceAmount) {
    this.resourceAmount = resourceAmount;
  }

  /**
   * Get the entity's game position.
   *
   * @return position
   */
  public Vector2 getPosition() {
    return position.cpy(); // Cpy gives us pass-by-value to prevent bugs
  }

  public void tweenPosition(Vector2 newPosition) {

    tweening = true;
    this.newPosition = newPosition;

    float xDiff = Math.abs(position.x - newPosition.x);
    float yDiff = Math.abs(position.y - newPosition.y);
    boolean xReached = xDiff < 1;
    boolean yReached = yDiff < 1;

    if (xReached && yReached) {
      tweening = false;
    }

    tweeningVector.x = newPosition.x > position.x ? 0.5f : -0.5f;
    tweeningVector.y = newPosition.y > position.y ? 0.25f : -0.25f;

    if (xDiff < 1) {
      tweeningVector.x = 0;
    }

    if (yDiff < 1) {
      tweeningVector.y = 0;
    }

    setPosition(position.cpy().add(tweeningVector));

  }

  /**
   * Set the entity's game position.
   *
   * @param position new position.
   */
  public void setPosition(Vector2 position) {
    this.position = position.cpy();
    getEvents().trigger(EVT_NAME_POS, position.cpy());
  }

  /**
   * Set the entity's game position.
   *
   * @param x new x position
   * @param y new y position
   */
  public void setPosition(float x, float y) {
    this.position.x = x;
    this.position.y = y;
    getEvents().trigger(EVT_NAME_POS, position.cpy());
  }

  /**
   * Set the entity's game position and optionally notifies listeners.
   *
   * @param position new position.
   * @param notify   true to notify (default), false otherwise
   */
  public void setPosition(Vector2 position, boolean notify) {
    this.position = position;
    if (notify) {
      getEvents().trigger(EVT_NAME_POS, position);
      getEvents().trigger("updateUgs");
    }
  }

  /**
   * Set the entity's position at a specific tile position
   *
   */
  public void setTileGridPosition(GridPoint2 tileCoord) {
    this.position = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        .tileToWorldPosition(tileCoord);
  }

  /**
   * Get the entity's scale. Used for rendering and physics bounding box
   * calculations.
   *
   * @return Scale in x and y directions. 1 = 1 metre.
   */
  public Vector2 getScale() {
    return scale.cpy(); // Cpy gives us pass-by-value to prevent bugs
  }

  /**
   * Set the entity's scale.
   *
   * @param scale new scale in metres
   */
  public void setScale(Vector2 scale) {
    this.scale = scale.cpy();
  }

  /**
   * Set the entity's scale.
   *
   * @param x width in metres
   * @param y height in metres
   */
  public void setScale(float x, float y) {
    this.scale.x = x;
    this.scale.y = y;
  }

  /**
   * Set the entity's width and scale the height to maintain aspect ratio.
   *
   * @param x width in metres
   */
  public void scaleWidth(float x) {
    this.scale.y = this.scale.y / this.scale.x * x;
    this.scale.x = x;
  }

  /**
   * Set the entity's height and scale the width to maintain aspect ratio.
   *
   * @param y height in metres
   */
  public void scaleHeight(float y) {
    this.scale.x = this.scale.x / this.scale.y * y;
    this.scale.y = y;
  }

  /**
   * Get the entity's center position
   *
   * @return center position
   */
  public Vector2 getCenterPosition() {
    return getPosition().mulAdd(getScale(), 0.5f);
  }

  /**
   * Get a component of type T on the entity.
   *
   * @param type The component class, e.g. RenderComponent.class
   * @param <T>  The component type, e.g. RenderComponent
   * @return The entity component, or null if nonexistent.
   */
  @SuppressWarnings("unchecked")
  public <T extends Component> T getComponent(Class<T> type) {
    ComponentType componentType = ComponentType.getFrom(type);
    return (T) components.get(componentType.getId());
  }

  /**
   * Add a component to the entity. Can only be called before the entity is
   * registered in the world.
   *
   * @param component The component to add. Only one component of a type can be
   *                  added to an entity.
   * @return Itself
   */
  public Entity addComponent(Component component) {
    if (created) {
      logger.error(
          "Adding {} to {} after creation is not supported and will be ignored", component, this);
      return this;
    }
    ComponentType componentType = ComponentType.getFrom(component.getClass());
    if (components.containsKey(componentType.getId())) {
      logger.error(
          "Attempted to add multiple components of class {} to {}. Only one component of a class "
              + "can be added to an entity, this will be ignored.",
          component,
          this);
      return this;
    }
    components.put(componentType.getId(), component);
    component.setEntity(this);

    return this;
  }

  /**
   * Dispose of the entity. This will dispose of all components on this entity.
   */
  public void dispose() {
    for (Component component : createdComponents) {
      component.dispose();
    }
    ServiceLocator.getEntityService().unregister(this);
  }

  /**
   * Create the entity and start running. This is called when the entity is
   * registered in the world,
   * and should not be called manually.
   */
  public void create() {
    if (created) {
      logger.error(
          "{} was created twice. Entity should only be registered with the entity service once.",
          this);
      return;
    }
    createdComponents = components.values().toArray();
    for (Component component : createdComponents) {
      component.create();
    }
    created = true;
  }

  /**
   * Perform an early update on all components. This is called by the entity
   * service and should not
   * be called manually.
   */
  public void earlyUpdate() {
    if (!enabled) {
      return;
    }

    if (tweening) {
      tweenPosition(newPosition);
    }

    for (Component component : createdComponents) {
      component.triggerEarlyUpdate();
    }
  }

  /**
   * Perform an update on all components. This is called by the entity service and
   * should not be
   * called manually.
   */
  public void update() {
    if (!enabled) {
      return;
    }
    for (Component component : createdComponents) {
      component.triggerUpdate();
    }
  }

  /**
   * This entity's unique ID. Used for equality checks
   *
   * @return unique ID
   */
  public int getId() {
    return id;
  }

  /**
   * Get the event handler attached to this entity. Can be used to trigger events
   * from an attached
   * component, or listen to events from a component.
   *
   * @return entity's event handler
   */
  public EventHandler getEvents() {
    return eventHandler;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Entity && ((Entity) obj).getId() == this.getId());
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    return String.format(this.getClass().getName() + "@" + id + ":" + name);
  }
}

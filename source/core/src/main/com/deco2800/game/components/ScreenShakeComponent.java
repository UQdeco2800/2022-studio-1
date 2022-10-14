package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;

import java.util.Random;

public class ScreenShakeComponent {
        public float time;
        Random random;
        float x, y, z;
        float current_time;
        float power;
        float current_power;

        public ScreenShakeComponent(){
            time = 0;
            current_time = 0;
            power = 0;
            current_power = 0;
        }

        // Call this function with the force of the shake
        // and how long it should last
        public void ScreenShake(float power, float time) {
            random = new Random();
            this.power = power;
            this.time = time;
            this.current_time = 0;
        }

        public void tick(float delta){
            // GameController contains the camera
            // Hero is the character centre screen

            Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
            CameraComponent camComp = camera.getComponent(CameraComponent.class);
            if(current_time <= time) {
                current_power = power * ((time - current_time) / time);
                // generate random new x and y values taking into account
                // how much force was passed in
                x = (random.nextFloat() - 0.5f) * 2 * current_power;
                y = (random.nextFloat() - 0.5f) * 2 * current_power;
                z = (random.nextFloat() - 0.5f) * 2 * current_power;

                // Set the camera to this new x/y position
                camComp.getCamera().translate(-x,-y,-z);
                current_time += delta;
            } else {
                // When the shaking is over move the camera back to the hero position
                //camera.position.x = hero.x;
                //camera.position.y = hero.y;
            }
        }

}

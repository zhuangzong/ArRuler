package com.kang.ardemo.ardemo;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

public class FaceToCameraNode extends Node {
    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        Scene scene = getScene();
        Vector3 cameraPosition = scene.getCamera().getWorldPosition();
        Vector3 nodePosition = FaceToCameraNode.this.getWorldPosition();
        Vector3 direction = Vector3.subtract(cameraPosition, nodePosition);
        FaceToCameraNode.this.setWorldRotation(Quaternion.lookRotation(direction, Vector3.up()));
    }
}

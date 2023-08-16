package com.kang.ardemo.ardemo;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyFragment myFragment;
    private List<AnchorInfoBean> dataArray = new ArrayList<>();
    AnchorNode startNode;
    TransformableNode startTrNode;

    AnchorNode secondNode;
    TransformableNode secondTrNode;

    Node lineNode;
    FaceToCameraNode faceToCameraNode;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.iv_main_clear).setOnClickListener(view -> {

            dataArray.clear();
            myFragment.getArSceneView().getScene().removeChild(startNode);
            myFragment.getArSceneView().getScene().removeChild(startTrNode);
            if (secondNode!=null){
                myFragment.getArSceneView().getScene().removeChild(secondNode);
                myFragment.getArSceneView().getScene().removeChild(startTrNode);
            }
        });

        myFragment = (MyFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        if (myFragment == null) {
            return;
        }

        myFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            if (dataArray.size() == 2) {
                return;
            }
            if (dataArray.size() == 1) {
                AnchorInfoBean anchorInfoBean = new AnchorInfoBean("", hitResult.createAnchor(), 0.0);
                dataArray.add(anchorInfoBean);
                addAnchor(hitResult, 2);
                Anchor endAnchor = dataArray.get(1).anchor;
                Anchor startAnchor = dataArray.get(0).anchor;
                Pose startPose = endAnchor.getPose();
                Pose endPose = startAnchor.getPose();
                float dx = startPose.tx() - endPose.tx();
                float dy = startPose.ty() - endPose.ty();
                float dz = startPose.tz() - endPose.tz();
                dataArray.get(1).length=Math.sqrt((dx * dx + dy * dy + dz * dz));
                drawLine(dataArray.get(1).length);
                return;
            }

            if (dataArray.size() == 0) {
                AnchorInfoBean anchorInfoBean = new AnchorInfoBean("", hitResult.createAnchor(), 0.0);
                dataArray.add(anchorInfoBean);
                addAnchor(hitResult, 1);
            }
        });
    }

    private void addAnchor(HitResult hitResult, int position) {
        Anchor anchor = hitResult.createAnchor();
        if (position == 1) {
            startNode = new AnchorNode(anchor);
            startNode.setParent(myFragment.getArSceneView().getScene());
        } else {
            secondNode = new AnchorNode(anchor);
            secondNode.setParent(myFragment.getArSceneView().getScene());
        }

        MaterialFactory.makeOpaqueWithColor(MainActivity.this, new Color(0.33f, 0.87f, 0f))
                .thenAccept(material -> {
                    ModelRenderable sphere = ShapeFactory.makeSphere(0.02f, Vector3.zero(), material);
                    if (position == 1) {
                        startTrNode = new TransformableNode(myFragment.getTransformationSystem());
                        startTrNode.setParent(startNode);
                        startTrNode.setRenderable(sphere);
                        startTrNode.select();
                        startTrNode.setOnTouchListener((hitTestResult, motionEvent1) -> {
                            startTrNode.select();
                            if (motionEvent1.getAction() == MotionEvent.ACTION_UP) {
                                if (dataArray.size() > 1) {

                                    startNode.removeChild(lineNode);

                                    AnchorInfoBean anchorInfoBean = new AnchorInfoBean("", startNode.getAnchor(), 0.0);
                                    dataArray.set(0,anchorInfoBean);

                                    Anchor startAnchor = dataArray.get(dataArray.size()-2).anchor;
                                    AnchorNode startNode = new AnchorNode(startAnchor);
                                    Anchor endAnchor = dataArray.get(dataArray.size()-1).anchor;
                                    AnchorNode endNode = new AnchorNode(endAnchor);
                                    double ddx = (startNode.getWorldPosition().x - endNode.getWorldPosition().x);
                                    double ddy = (startNode.getWorldPosition().y - endNode.getWorldPosition().y);
                                    double ddz = (startNode.getWorldPosition().z - endNode.getWorldPosition().z);

                                    drawLine(Math.sqrt(ddx * ddx + ddy * ddy + ddz * ddz));
                                }
                            }
                            return true;
                        });
                    } else {
                        secondTrNode = new TransformableNode(myFragment.getTransformationSystem());
                        secondTrNode.setParent(secondNode);
                        secondTrNode.setRenderable(sphere);
                        secondTrNode.select();
                        secondTrNode.setOnTouchListener((hitTestResult, motionEvent1) -> {
                            secondTrNode.select();
                            if (motionEvent1.getAction() == MotionEvent.ACTION_UP) {
                                if (dataArray.size() > 1) {

                                    startNode.removeChild(lineNode);
                                    AnchorInfoBean anchorInfoBean = new AnchorInfoBean("", secondNode.getAnchor(), 0.0);
                                    dataArray.set(1,anchorInfoBean);

                                    Anchor startAnchor = dataArray.get(dataArray.size()-2).anchor;
                                    AnchorNode startNode = new AnchorNode(startAnchor);
                                    Anchor endAnchor = dataArray.get(dataArray.size()-1).anchor;
                                    AnchorNode endNode = new AnchorNode(endAnchor);
                                    double ddx = (startNode.getWorldPosition().x - endNode.getWorldPosition().x);
                                    double ddy = (startNode.getWorldPosition().y - endNode.getWorldPosition().y);
                                    double ddz = (startNode.getWorldPosition().z - endNode.getWorldPosition().z);

                                    drawLine(Math.sqrt(ddx * ddx + ddy * ddy + ddz * ddz));
                                }
                            }
                            return true;
                        });
                    }
                });
    }

    private void drawLine(final Double length) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Vector3 firstWorldPosition = startNode.getWorldPosition();
            Vector3 secondWorldPosition = secondNode.getWorldPosition();

            Vector3 difference = Vector3.subtract(firstWorldPosition, secondWorldPosition);
            Vector3 directionFromTopToBottom = difference.normalized();
            Quaternion rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());

            MaterialFactory.makeOpaqueWithColor(MainActivity.this, new Color(0.33f, 0.87f, 0f))
                    .thenAccept(material -> {
                        ModelRenderable sphere = ShapeFactory.makeCube(new Vector3(0.01f, 0.01f, difference.length()), Vector3.zero(), material);
                        lineNode = new Node();
                        lineNode.setParent(startNode);
                        lineNode.setRenderable(sphere);
                        lineNode.setWorldPosition(Vector3.add(firstWorldPosition, secondWorldPosition).scaled(0.5f));
                        lineNode.setWorldRotation(rotationFromAToB);

//                        final Node lineNode2 = new Node();
//                        lineNode2.setParent(secondNode);
//                        lineNode2.setRenderable(sphere);
//                        lineNode2.setWorldPosition(Vector3.add(firstWorldPosition, secondWorldPosition).scaled(0.5f));
//                        lineNode2.setWorldRotation(rotationFromAToB);
//                        lineNodeArray.add(lineNode2);

                        ViewRenderable.builder()
                                .setView(MainActivity.this, R.layout.renderable_text)
                                .build()
                                .thenAccept(viewRenderable -> {
                                    TextView textView = ((TextView) viewRenderable.getView());
                                    textView.setText(String.format("%scm", new BigDecimal(length * 100).setScale(2, RoundingMode.UP).doubleValue()));
                                    viewRenderable.setShadowCaster(false);
                                    faceToCameraNode = new FaceToCameraNode();
                                    faceToCameraNode.setParent(lineNode);
                                    faceToCameraNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0f, 1f, 0f), 90f));
                                    faceToCameraNode.setLocalPosition(new Vector3(0f, 0.02f, 0f));
                                    faceToCameraNode.setRenderable(viewRenderable);
                                });
                    });

        }
    }
}

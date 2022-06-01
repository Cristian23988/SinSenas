// Copyright 2021 The MediaPipe Authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.SinSenas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;


import com.example.SinSenas.Class.Punto;
import com.example.SinSenas.Class.Sena;
import com.example.SinSenas.db.DbPunto;
import com.example.SinSenas.db.DbSena;

import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsResult;

import java.util.ArrayList;
import java.util.List;

/** An ImageView implementation for displaying {@link HandsResult}. */
public class HandsResultImageView extends AppCompatImageView {
  private static final String TAG = "HandsResultImageView";

  private static final int LEFT_HAND_CONNECTION_COLOR = Color.parseColor("#30FF30");
  private static final int RIGHT_HAND_CONNECTION_COLOR = Color.parseColor("#FF3030");
  private static final int CONNECTION_THICKNESS = 8; // Pixels
  private static final int LEFT_HAND_HOLLOW_CIRCLE_COLOR = Color.parseColor("#30FF30");
  private static final int RIGHT_HAND_HOLLOW_CIRCLE_COLOR = Color.parseColor("#FF3030");
  private static final int HOLLOW_CIRCLE_WIDTH = 5; // Pixels
  private static final int LEFT_HAND_LANDMARK_COLOR = Color.parseColor("#FF3030");
  private static final int RIGHT_HAND_LANDMARK_COLOR = Color.parseColor("#30FF30");
  private static final int LANDMARK_RADIUS = 10; // Pixels
  private Bitmap latest;
  public HandsResultImageView(Context context) {
    super(context);
    setScaleType(ScaleType.FIT_CENTER);
  }

  /**
   * Sets a {@link HandsResult} to render.
   *
   * @param result a {@link HandsResult} object that contains the solution outputs and the input
   *     {@link Bitmap}.
   */
  public void setHandsResult(HandsResult result) {
    if (result == null) {
      return;
    }
      
    //crea una capa encima de la imagen para mostrar los puntos, es decir no edita la imagen
    Bitmap bmInput = result.inputBitmap();
    int width = bmInput.getWidth();
    int height = bmInput.getHeight();
    latest = Bitmap.createBitmap(width, height, bmInput.getConfig());
    Canvas canvas = new Canvas(latest);
    canvas.drawBitmap(bmInput, new Matrix(), null);

    //Solo dibuja la mano
    int numHands = result.multiHandLandmarks().size();
    for (int i = 0; i < numHands; ++i) {
      drawLandmarksOnCanvas(
          result.multiHandLandmarks().get(i).getLandmarkList(),
          result.multiHandedness().get(i).getLabel().equals("Left"),
          canvas,
          width,
          height);
    }
  }

  /** Updates the image view with the latest {@link HandsResult}. */
  public void update() {
    postInvalidate();
    if (latest != null) {
      setImageBitmap(latest);
    }
  }

  /** Dibujar mano en la capa agregada al inicio*/
  private void drawLandmarksOnCanvas(
      List<NormalizedLandmark> handLandmarkList,
      boolean isLeftHand,
      Canvas canvas,
      int width,
      int height) {
    // Draw connections.
    for (Hands.Connection c : Hands.HAND_CONNECTIONS) {
      Paint connectionPaint = new Paint();
      connectionPaint.setColor(isLeftHand ? LEFT_HAND_CONNECTION_COLOR : RIGHT_HAND_CONNECTION_COLOR);
      connectionPaint.setStrokeWidth(CONNECTION_THICKNESS);
      NormalizedLandmark start = handLandmarkList.get(c.start());
      NormalizedLandmark end = handLandmarkList.get(c.end());

      canvas.drawLine(
          start.getX() * width,
          start.getY() * height,
          end.getX() * width,
          end.getY() * height,
          connectionPaint);
    }
    /*
    Paint landmarkPaint = new Paint();
    landmarkPaint.setColor(isLeftHand ? LEFT_HAND_LANDMARK_COLOR : RIGHT_HAND_LANDMARK_COLOR);
    // Draws landmarks.
    for (LandmarkProto.NormalizedLandmark landmark : handLandmarkList) {
      canvas.drawCircle(
          landmark.getX() * width, landmark.getY() * height, LANDMARK_RADIUS, landmarkPaint);
    }
    landmarkPaint.setColor(
        isLeftHand ? LEFT_HAND_HOLLOW_CIRCLE_COLOR : RIGHT_HAND_HOLLOW_CIRCLE_COLOR);
    landmarkPaint.setStrokeWidth(HOLLOW_CIRCLE_WIDTH);
    landmarkPaint.setStyle(Paint.Style.STROKE);
    for (LandmarkProto.NormalizedLandmark landmark : handLandmarkList) {
      canvas.drawCircle(
          landmark.getX() * width,
          landmark.getY() * height,
          LANDMARK_RADIUS + HOLLOW_CIRCLE_WIDTH,
          landmarkPaint);
    }

    Paint PointsNumber = new Paint();
    PointsNumber.setColor(Color.BLACK);
    PointsNumber.setStrokeWidth(2f);
    PointsNumber.setStyle(Paint.Style.STROKE);
    ArrayList<Punto> puntos = new ArrayList<Punto>();
    ArrayList<Sena> senas = new ArrayList<Sena>();
    int idPoint=0;
    for (LandmarkProto.NormalizedLandmark landmark : handLandmarkList) {
      canvas.drawText(String.valueOf(idPoint), landmark.getX() * width, landmark.getY() * height, PointsNumber);
    idPoint=idPoint+1;
    puntos.add(new Punto(idPoint,landmark.getX()* width,landmark.getY()* height));
    }

    //----->No borrar this important!!!!!!!

    String manoLeft = isLeftHand ? "Izq" : "Der";
    int id =0;
    if (manoLeft.equals("Izq")){id=2;}else{id=1;}
    senas.add(new Sena(id,manoLeft,puntos));

    //DbSena dbsena=new DbSena(HandsResultImageView.this);
    //ArrayList<Sena> senas = new ArrayList<Sena>();
    DbSena dbsena=new DbSena(contextoo);
    DbPunto dbpunto=new DbPunto(contextoo);

    //---Guardar mensaje---------------
    String mensaje = "";

    for(Sena sen : senas){
     // Log.i("----Seña:", String.valueOf(sen.getSena()));
    //  Log.i("----SeñaID:", String.valueOf(sen.getId()));
      dbsena.insertSena(String.valueOf(sen.getSena()));
      mensaje += "Seña: ";
      mensaje += String.valueOf(sen.getSena());
      for(Punto punt : sen.getPuntos()){
        dbpunto.insertPunto(punt.getVectorX(),punt.getVectorY(),sen.getId());
        mensaje += "\nvector[ ";
        mensaje += String.valueOf(punt.getVectorX());
        mensaje += " : ";
        mensaje += String.valueOf(punt.getVectorY());
        mensaje += "]\n";
      }
    }
    this.setMensaje(mensaje);
    */
  }
}
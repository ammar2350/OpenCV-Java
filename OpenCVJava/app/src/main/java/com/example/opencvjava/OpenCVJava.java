package com.example.opencvjava;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public class OpenCVJava {
    public static void main(String[] args) {
        // LOAD LIBRARY
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Create a blank canvas with a size of 400x400 pixels and a white background color
        Mat canvas = new Mat(new Size(400, 400), CvType.CV_8UC3, new Scalar(255, 255, 255)); // CV_8UC3 for 3-channel color images

        // LOAD IMAGE
        String imagePath = "app/src/main/assets/images/things.jpg";
        Mat image = Imgcodecs.imread(imagePath);

        // SHOW IMAGE
//        HighGui.imshow("Image", image);

        // SAVE IMAGE
//        Imgcodecs.imwrite("app/src/main/assets/images/things2.jpg", image);

        // LOAD VIDEO
        String videoPath = "app/src/main/assets/videos/highway.mp4";
        VideoCapture video = new VideoCapture(videoPath);
        Mat videoFrame = new Mat();
        video.read(videoFrame);
//        HighGui.imshow("Video", videoFrame);

        while(true){
            if (!video.read(videoFrame)) {
                System.out.println("No more frames to read, done.");
                break;
            }

            HighGui.imshow("Video", videoFrame);

            // Waiting for 33 ms to adjust video speed ~30 fps
            if (HighGui.waitKey(33) == 27) { // Press ESC to exit
                break;
            }
        }

//        // LOAD CAMERA
//        VideoCapture camera = new VideoCapture(0);
//        Mat cameraFrame = new Mat();
//        camera.read(cameraFrame);
////        HighGui.imshow("Video", videoFrame);
//
//        while(true){
//            if (!camera.read(cameraFrame)) {
//                System.out.println("No more frames to read, done.");
//                break;
//            }
//
////            HighGui.imshow("Camera", cameraFrame);
//
//            // Waiting for 33 ms to adjust video speed ~30 fps
//            if (HighGui.waitKey(33) == 27) { // Press ESC to exit
//                break;
//            }
//        }


        // DRAW IN CANVAS

        // Top left and bottom right coordinate points
        Point pt1 = new Point(50, 50);
        Point pt2 = new Point(200, 200);

        // Color: BGR format (Blue, Green, Red)
        Scalar colorRed = new Scalar(0, 0, 255); // Red
        Scalar colorGreen = new Scalar(0, 255, 0); // Green
        Scalar colorBlue = new Scalar(255, 0, 0); // Blue

        // Line thickness
        int thickness1 = 3;
        int thickness2 = -1;

        // Drawing a rectangle on the canvas
        Imgproc.rectangle(canvas, pt1, pt2, colorRed, thickness1);
        // Drawing a line on the canvas
        Imgproc.line(canvas, pt1, pt2, colorGreen, thickness1);
        // Drawing a circle on the canvas
        int radius = 20;
        Imgproc.circle(canvas, pt1, radius, colorBlue, thickness2);
        HighGui.imshow("Drawing", canvas);

        // Translation
        Mat translatedImage = new Mat();
        Mat translationMatrix = Mat.eye(2, 3, CvType.CV_32F);
        translationMatrix.put(0, 2, 200); // x translation
        translationMatrix.put(1, 2, 100); // y translation
        Imgproc.warpAffine(image, translatedImage, translationMatrix, new Size(image.cols(), image.rows()));
        HighGui.imshow("Translated Image", translatedImage);

        // Rotation
        Mat rotatedImage = new Mat();
        Point center = new Point(image.cols() / 2, image.rows() / 2);
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, 45, 1.0); // 45 degrees
        Imgproc.warpAffine(image, rotatedImage, rotationMatrix, image.size());
        HighGui.imshow("Rotated Image", rotatedImage);

        // Resize
        Mat resizedImage = new Mat();
        Imgproc.resize(image, resizedImage, new Size(300, 300));
        HighGui.imshow("Resized Image", resizedImage);

        // Flipping
        Mat flippedImage = new Mat();
        Core.flip(image, flippedImage, 1); // horizontal flip
        HighGui.imshow("Flipped Image", flippedImage);

        // Cropping
        Rect cropArea = new Rect(50, 50, 300, 300);
        Mat croppedImage = new Mat(image, cropArea);
        HighGui.imshow("Cropped Image", croppedImage);

        // GRAY IMAGE
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        HighGui.imshow("Gray Image", grayImage);

        // BLUR IMAGE
        Mat blurImage = new Mat();
        Size blurSize = new Size(50, 50);
        Imgproc.blur(grayImage, blurImage, blurSize);
        HighGui.imshow("Blur Image", blurImage);

        // EDGE DETECTION
        Mat edges = new Mat();
        Imgproc.Canny(image, edges, 100, 200);
        HighGui.imshow("Edge Detection", edges);

        // Applying threshold to obtain binary image
        Mat binary = new Mat();
        Imgproc.threshold(grayImage, binary, 100, 255, Imgproc.THRESH_BINARY_INV);
        HighGui.imshow("Binary Image", binary);

        // Find the contour
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Gambar bounding rect
        Mat result = image.clone();
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint contour = contours.get(i);
            if (Imgproc.contourArea(contour) > 200) { // Check contour area
                Rect rect = Imgproc.boundingRect(contour);
                Imgproc.rectangle(result, rect, new Scalar(0, 255, 0), 2);
            }
        }

        HighGui.imshow("Contours Result", result);

        HighGui.waitKey(0);
    }
}

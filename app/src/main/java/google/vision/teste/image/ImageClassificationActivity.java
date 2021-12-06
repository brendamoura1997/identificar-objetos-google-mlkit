package google.vision.teste.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import google.vision.teste.helpers.BoxWithText;
import google.vision.teste.helpers.MLImageHelperActivity;
import google.vision.teste.translate.Language;
import google.vision.teste.translate.TranslateAPI;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.ArrayList;
import java.util.List;

public class ImageClassificationActivity extends MLImageHelperActivity {
    private ImageLabeler imageLabeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLabeler = ImageLabeling.getClient(new ImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f)
                .build());
    }
    @Override
    protected void runDetection(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        Bitmap finalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        imageLabeler.process(inputImage).addOnSuccessListener(imageLabels -> {
           StringBuilder sb = new StringBuilder();
            //List<BoxWithText> boxes = new ArrayList();

           for (ImageLabel label : imageLabels) {

               sb.append(label.getText()).append(": ").append(label.getConfidence()).append("\n");

               /*
               String word = label.getText();

               TranslateAPI translateAPI = new TranslateAPI(
                       Language.ENGLISH,
                       Language.PORTUGUESE,
                       word);

               translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                   @Override
                   public void onSuccess(String translatedText) {
                       //Log.d(TAG, "onSuccess: " + translatedText);
                       //textView.setText(translatedText);
                       //sb.append(label.getText()).append(": ").append(label.getConfidence()).append("\n");
                   }

                   @Override
                   public void onFailure(String ErrorText) {
                       //Log.d(TAG, "onFailure: " + ErrorText);
                   }
               });
               */

               //boxes.add(new BoxWithText(imageLabels.getTrackingId() + "", imageLabels.getBoundingBox()));
               //getInputImageView().setImageBitmap(drawDetectionResult(finalBitmap, boxes));

           }
           if (imageLabels.isEmpty()) {
               getOutputTextView().setText("Não pôde ser classificado!!!");
           } else {


               TranslateAPI translateAPI = new TranslateAPI(
                       Language.ENGLISH,
                       Language.PORTUGUESE,
                       sb.toString());

               translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                   @Override
                   public void onSuccess(String translatedText) {
                       //Log.d(TAG, "onSuccess: " + translatedText);
                       //textView.setText(translatedText);
                       getOutputTextView().setText(translatedText);
                       //sb.append(label.getText()).append(": ").append(label.getConfidence()).append("\n");
                   }

                   @Override
                   public void onFailure(String ErrorText) {
                       //Log.d(TAG, "onFailure: " + ErrorText);
                   }
               });


           }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }
}

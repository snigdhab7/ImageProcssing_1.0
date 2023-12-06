package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * Transformation class that extracts luma component from an image.
 */
public class LumaComponent extends AbstractImageTransformation {
  /**
   * Extracts the luma component from the input image.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file after luma extraction.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters or configurations for the transformation.
   * @return A two-dimensional array of pixels representing the luma component of the image.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    Pixel pixel;
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        // process pixel
        pixel = pixelMatrix[i][j];
        outputPixelMatrix[i][j] = new Pixel((int) (0.2126 * pixel.getRed()),
                (int) (0.7152 * pixel.getGreen()),
                (int) (0.0722 * pixel.getBlue()), pixel.getAlpha());
      }
    }
    return outputPixelMatrix;
  }
}

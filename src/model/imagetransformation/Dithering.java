package model.imagetransformation;

import java.util.Map;
import model.image.Pixel;

/**
 * Transformation class that applies dithering to convert an image to black and white.
 */
public class Dithering extends AbstractImageTransformation {

    /**
     * Applies dithering to convert the input image to black and white.
     *
     * @param inputFileName       The name of the input image file.
     * @param outputFileName      The name of the output image file after dithering.
     * @param imagesPixelMatrices A map containing pixel matrices of different images.
     * @param otherParams         Additional parameters or configurations for the transformation.
     * @return A two-dimensional array of pixels representing the dithered image.
     */
    @Override
    public Pixel[][] generate(String inputFileName, String outputFileName,
                              Map<String, Pixel[][]> imagesPixelMatrices,
                              Object... otherParams) {
        System.out.println("In dithering");
        Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
        int pixelMatrixRows = pixelMatrix.length;
        int pixelMatrixCols = pixelMatrix[0].length;
        Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];

        // Copy the original pixelMatrix to outputPixelMatrix
        for (int i = 0; i < pixelMatrixRows; i++) {
            for (int j = 0; j < pixelMatrixCols; j++) {
                outputPixelMatrix[i][j] = new Pixel(
                        pixelMatrix[i][j].getRed(),
                        pixelMatrix[i][j].getGreen(),
                        pixelMatrix[i][j].getBlue()
                );
            }
        }

        for (int i = 0; i < pixelMatrixRows; i++) {
            for (int j = 0; j < pixelMatrixCols; j++) {
                int oldColor = outputPixelMatrix[i][j].getRed(); // Assuming greyscale, so only red-component is used
                int newColor = (oldColor < 128) ? 0 : 255; // Convert to black or white
                int error = oldColor - newColor;

                outputPixelMatrix[i][j] = new Pixel(newColor, newColor, newColor);

                distributeError(outputPixelMatrix, pixelMatrixCols, pixelMatrixRows, i, j, error);
            }
        }

        return outputPixelMatrix;
    }

    private void distributeError(Pixel[][] pixelMatrix, int width, int height, int x, int y, int error) {
        // Floyd-Steinberg error diffusion coefficients
        double[] coefficients = {7.0 / 16.0, 3.0 / 16.0, 5.0 / 16.0, 1.0 / 16.0};

        // Define the neighbors affected by the error diffusion
        int[][] neighbors = {
                {0, 1},   // Right
                {1, -1},  // Next-row-left
                {1, 0},   // Below
                {1, 1}    // Next-row-right
        };

        // Distribute the error to neighboring pixels
        for (int i = 0; i < 4; i++) {
            int nx = x + neighbors[i][1];
            int ny = y + neighbors[i][0];

            if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                int newError = (int) (coefficients[i] * error);
                int updatedValue = pixelMatrix[ny][nx].getRed() + newError;
                updatedValue = Math.min(255, Math.max(0, updatedValue));
                pixelMatrix[ny][nx] = new Pixel(updatedValue, updatedValue, updatedValue);
            }
        }
    }
}

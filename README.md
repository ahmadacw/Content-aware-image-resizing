# Content-aware-image-resizing
Utilizing dynamic programing in order to resize digital images with minimal loss of quality

We do this by removing "seams" from the image, so if we want to reduce the size by 2 in the x dimension.
We find two vertical seams to remove.

The seams can be choosen using different methods, the most basic being straight rows of pixels.
The technique that we used was to calculate an energy function for the image and remove the scene with
The least contributed energy to the image.

The energy function calculates a value for each pixel based on its similarity to its neighbouring pexils,
The more similar it is the lower it's energy.

We utilize this energy function in two methods:-

1. Minimal loss, we remove the seam that has the least weight.
2. Minimal loss but also with maximum energy for the produced image, these two parameters need to be balanced,
   Using some trial and error.

For can see the preformance of each method in the results folder.

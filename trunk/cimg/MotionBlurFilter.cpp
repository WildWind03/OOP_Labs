#include "MotionBlurFilter.h"

MotionBlurFilter::MotionBlurFilter(size_t angle, size_t speed) : angle(angle), speed(speed)
{
	if (speed > maxSpeed || angle > maxAngle)
	{
		throw std::invalid_argument(CANT_APPLY_FILTER_STR);
	}
}

Pixel MotionBlurFilter::getNewPixel(const Image & image, int x2, int y2, size_t pixelSpeed) const
{
	if (image.getHeight() <= speed || image.getWidth() <= speed)
	{
		throw std::invalid_argument(CANT_APPLY_FILTER_STR);
	}

	float redSum = 0;
	float greenSum = 0;
	float blueSum = 0;

	int deltaX = std::cos(angle * M_PI / 180) * pixelSpeed;
	int deltaY = std::sin(angle * M_PI / 180) * pixelSpeed;

	int x1 = x2 - deltaX;
	int y1 = y2 + deltaY;

	if ((x1 < 0 || y1 < 0 || !image.isPointInImage(x1, y1)) && pixelSpeed > 0)
	{
		return getNewPixel(image, x2, y2, --pixelSpeed);
	}

	deltaX = std::abs(deltaX);
	deltaY = std::abs(deltaY);

	if (0 == deltaX && 0 == deltaY)
	{
		return image.getPixel(x2, y2);
	}

    const int signX = x1 < x2 ? 1 : -1;
    const int signY = y1 < y2 ? 1 : -1;

    if (x1 <= 0 && y1 <= 0)
    {
		return getNewPixel(image, x2 + 1, y2 + 1, pixelSpeed);
    }

    if (x1 <= 0 && y1 >= image.getHeight() - 1)
    {
    	return getNewPixel(image, x2 + 1, y2 - 1, pixelSpeed);
    }

    if (x1 >= image.getWidth() - 1 && y1 <= 0)
    {
		return getNewPixel(image, x2 - 1, y2 + 1, pixelSpeed);
    }

    if (x1 >= image.getWidth() - 1 && y1 >= image.getHeight() - 1)
    {
		return getNewPixel(image, x2 - 1, y2 - 1, pixelSpeed);
    }

    if (x1 <= 0)
    {
    	return getNewPixel(image, x2 + 1, y2, pixelSpeed);
    }

    if (x1 >= image.getWidth() - 1)
    {
    	return getNewPixel(image, x2 - 1, y2, pixelSpeed);
    }

    if (y1 <= 0)
    {
    	return getNewPixel(image, x2, y2 + 1, pixelSpeed);
    }

    if (y1 >= image.getHeight() - 1)
    {
    	return getNewPixel(image, x2, y2 - 1, pixelSpeed);
    }

    int iter = deltaX < deltaY ? deltaY : deltaX;

    int error = deltaX - deltaY;

    Pixel curPixel;

    while ((x1 != x2) || (y1 != y2))
    {
		curPixel = image.getPixel(x1, y1);

	    redSum += curPixel.getRed() / iter;
	    greenSum += curPixel.getGreen() / iter;
	    blueSum += curPixel.getBlue() / iter;

        const int error2 = error * 2;

        if (error2 > -deltaY) 
        {
            error -= deltaY;
            x1 += signX;
        }

        if(error2 < deltaX) 
        {
            error += deltaX;
            y1 += signY;
        } 	
    }

 	if (redSum > maxColor)
	{
		redSum = maxColor;
	}
	if (redSum < minColor)
	{
		redSum = minColor;
	}

	if (greenSum > maxColor)
	{
		greenSum = maxColor;
	}
	if (greenSum < minColor)
	{
		greenSum = minColor;
	}

	if (blueSum > maxColor)
	{
		blueSum = maxColor;
	}
	if (blueSum < minColor)
	{
		blueSum = minColor;
	}

    return Pixel(redSum, greenSum, blueSum);
}

Pixel MotionBlurFilter::getNearPixel(const Image & image, int x, int y) const
{
	size_t pixelX, pixelY;

	if (x < minColor)
	{
		pixelX = minColor;
	}
	else
	{
		if (x >= image.getWidth())
		{
			pixelX = image.getWidth() - 1;
		}
		else
		{
			pixelX = x;
		}

	}

	if (y < minColor)
	{
		pixelY = minColor;
	}
	else
	{
		if (y >= image.getHeight())
		{
			pixelY = image.getHeight() - 1;
		}
		else
		{
			pixelY = y;
		}
	}

	return image.getPixel(pixelX, pixelY);
}

Image MotionBlurFilter::apply(const Image & image) const
{

	std::vector<std::vector<Pixel>> pixels;

	pixels.resize(image.getWidth());

	for (size_t i = 0; i < image.getWidth(); ++i)
	{
		pixels[i].resize(image.getHeight());
	}

	for (size_t i = 0; i < image.getWidth(); ++i)
	{
		for (size_t k = 0; k < image.getHeight(); ++k)
		{
			pixels[i][k] = getNewPixel(image, i, k, speed);
		}
	}

	return Image(pixels);
}

MotionBlurFilter::~MotionBlurFilter()
{

}
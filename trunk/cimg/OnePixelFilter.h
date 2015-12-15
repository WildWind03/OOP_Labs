#pragma once

#include "BaseFunctor.h"
#include "BaseFilter.h"

template <class Functor>
	class OnePixelFilter : public BaseFilter
	{
		Functor * functor;

	public:

		OnePixelFilter(const OnePixelFilter & onePixelFilter) = delete;
		OnePixelFilter & operator= (const OnePixelFilter & onePixelFilter) = delete;

		template <typename...TArgs>
			OnePixelFilter(TArgs...args)
			{
				functor = new Functor(args...);
			}

		virtual Image * apply(const Image & image) override
		{
			Image * filteredImage = new Image(image);

			for (size_t i = 0; i < image.getWidth(); ++i)
			{
				for (size_t k = 0; k < image.getHeight(); ++k)
				{
					Pixel * pixel = filteredImage -> getPixel(i, k);
					(*functor)(pixel);
				}
			}

			return filteredImage;
		}

		virtual ~OnePixelFilter()
		{
			delete(functor);
		}

	};
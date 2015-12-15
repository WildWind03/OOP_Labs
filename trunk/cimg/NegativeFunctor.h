#pragma once

#include "BaseFunctor.h"

#include <cstdio>

class NegativeFunctor : public BaseFunctor
{
	const unsigned char maxColor = 255;
	
public:
	NegativeFunctor();

	NegativeFunctor(const NegativeFunctor & negativeFunctor) = delete;
	NegativeFunctor & operator= (const NegativeFunctor & negativeFunctor) = delete;

	virtual void operator()(Pixel * pixel) const override;

	virtual ~NegativeFunctor();
};
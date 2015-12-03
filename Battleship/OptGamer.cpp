#include "OptGamer.h"

OptGamer::OptGamer() : Gamer () 
{
	st = GamerState::EXPLORE;
	shotCounter = 0;
}

void OptGamer::fillStShotsList(size_t hField, size_t wField)
{
	for (size_t i = 0; i < hField; ++i)
	{
		for (size_t k = 0; k < wField; ++k)
		{
			ShotPoint p(i, k);

			stShots.push_back(p);
		}
	}

	std::random_shuffle(stShots.begin(), stShots.end());
}

ShotPoint OptGamer::getNextStandartShot()
{
	ShotPoint p = stShots.back();
	stShots.pop_back();

	return p;
}

void OptGamer::onGameEnded(bool isWon)
{
	isReady = false;
}

void OptGamer::onGameStarted(size_t h, size_t w)
{
	injured.clear();
	stShots.clear();
	nextShots.clear();

	isReady = true;
	shotCounter = 0;
	st = GamerState::EXPLORE;

	fillStShotsList(h, w);
}

void OptGamer::onRecieveShotState(ShotState state, ShotPoint p)
{
	switch (state)
	{
		case ShotState::INJURED :

			st = GamerState::HIT;

			injured.push_back(p);

			nextShots.clear();

			break;

		case ShotState::DESTROYED :

			injured.clear();

			nextShots.clear();

			st = GamerState::EXPLORE;

			break;
	}
}
 
ShipPoint OptGamer::getPointForShip(const size_t sizeOfShip, const MyFieldView & myFieldV)
{
	if (false == isReadyToStart())
	{
		throw std::runtime_error(initError);
	}

	size_t h, w;
	bool isVert;

	if (1 == sizeOfShip)
	{
		h = myRand::getRand(2, myFieldV.getHeight() - 3);
		w = myRand::getRand(2, myFieldV.getWidth() - 3);

		isVert = true;
	}
	else
	{
		h = myRand::getRand(0, myFieldV.getHeight() - 1);

		if ((0 == h) || (myFieldV.getHeight() - 1 == h))
		{
			w = myRand::getRand(0, myFieldV.getWidth() - 1);
		}
		else
		{
			bool c = myRand::getRand(0,1);

			if (false == c)
			{
				w = 0;
			}
			else
			{
				w = myFieldV.getWidth() - 1;
			}
		}

		if ((0 == h) && (0 == w))
		{
			isVert = myRand::getRand(0,1);
		}
		else
		{
			if (myFieldV.getWidth() - 1 == w)
			{
				isVert = true;
			}
			else
			{
				if ((myFieldV.getHeight() - 1 != h) && (0 != h))
				{
					isVert = true;
				}
				else
				{
					isVert = false;
				}
			}
		}
	}

	return ShipPoint(h, w, isVert);
}

void OptGamer::fillNextShotsListNoOrient(size_t hField, size_t wField)
{
	nextShots.clear();

	long long h = injured[0].getHeight();
	long long w = injured[0].getWidth();

	long long maxH = hField;
	long long maxW = wField;

	if (h + 1 < maxH)
	{
		nextShots.push_back(ShotPoint(h + 1, w));
	}

	if (w + 1 < maxW)
	{
		nextShots.push_back(ShotPoint(h, w + 1));
	}

	if (h - 1 >= 0)
	{
		nextShots.push_back(ShotPoint(h - 1, w));
	}

	if (w - 1 >= 0)
	{
		nextShots.push_back(ShotPoint(h, w - 1));
	}
}

bool OptGamer::isInjVert() const
{
	if(injured[0].getHeight() == injured[1].getHeight())
	{
		return false;
	}
	else
	{
		return true;
	}
}

size_t OptGamer::getMaxWidthInj() const
{
	if (isInjVert())
	{
		return injured[0].getWidth();
	}
	else
	{
		size_t maxWidth = injured[0].getWidth();

		for (size_t i = 1; i < injured.size(); ++i)
		{
			if (injured[i].getWidth() > maxWidth)
			{
				maxWidth = injured[i].getWidth();
			}
		}

		return maxWidth;
	}
}

size_t OptGamer::getMaxHeightInj() const
{
	if (!isInjVert())
	{
		return injured[0].getHeight();
	}
	else
	{
		size_t maxHeight = injured[0].getHeight();

		for (size_t i = 1; i < injured.size(); ++i)
		{
			if (injured[i].getHeight() > maxHeight)
			{
				maxHeight = injured[i].getHeight();
			}
		}

		return maxHeight;
	}
}

size_t OptGamer::getMinWidthInj() const
{
	if (isInjVert())
	{
		return injured[0].getWidth();
	}
	else
	{
		size_t minWidth = injured[0].getWidth();

		for (size_t i = 1; i < injured.size(); ++i)
		{
			if (injured[i].getWidth() < minWidth)
			{
				minWidth = injured[i].getWidth();
			}
		}

		return minWidth;
	}
}

size_t OptGamer::getMinHeightInj() const
{
	if (!isInjVert())
	{
		return injured[0].getHeight();
	}
	else
	{
		size_t minHeight = injured[0].getHeight();

		for (size_t i = 1; i < injured.size(); ++i)
		{
			if (injured[i].getHeight() < minHeight)
			{
				minHeight = injured[i].getHeight();
			}
		}

		return minHeight;
	}
}

void OptGamer::fillNextShotsListOrient(size_t hField, size_t wField)
{
	nextShots.clear();

	long long h1, h2, w1, w2;

	long long maxH = hField;
	long long maxW = wField;

	if (isInjVert())
	{
		w1 = injured[0].getWidth();
		w2 = injured[0].getWidth();

		h1 = getMaxHeightInj() + 1;
		h2 = getMinHeightInj() - 1;

		if (h1 < maxH)
		{
			nextShots.push_back(ShotPoint(h1, w1));
		}

		if (h2 >= 0)
		{
			nextShots.push_back(ShotPoint(h2, w2));
		}
	}
	else
	{
		h1 = injured[0].getHeight();
		h2 = injured[0].getHeight();

		w1 = getMaxWidthInj() + 1;
		w2 = getMinWidthInj() - 1;

		if (w1 < maxW)
		{
			nextShots.push_back(ShotPoint(h1, w1));
		}

		if (w2 >= 0)
		{
			nextShots.push_back(ShotPoint(h2, w2));
		}
	}
}

ShotPoint OptGamer::getNextHitShot()
{
	ShotPoint p = nextShots.back();
	nextShots.pop_back();

	return p;
}

void OptGamer::fillNextShots(size_t hField, size_t wField)
{
	if (1 == injured.size())
	{
		fillNextShotsListNoOrient(hField, wField);
	}
	else
	{
		fillNextShotsListOrient(hField, wField);
	}
}

bool OptGamer::isPossibleToBeShipThere(const ShotPoint & p, const EnemyFieldView & v) const
{
	for (size_t i = 0; i < injured.size(); ++i)
	{
		if (v.isExistLearntDestrCellNear(p, injured[i]))
		{
			return false;
		}
	}

	return true;
}

ShotPoint OptGamer::getPointForShot(const MyFieldView & myFieldV, const EnemyFieldView & enemyFieldV)
{
	if (false == isReadyToStart())
	{
		throw std::runtime_error(initError);
	}

	if (GamerState::EXPLORE == st) 
	{
		ShotPoint p = getNextStandartShot();

		while(enemyFieldV.isExistLearntDestrCellNear(p))
		{
			p = getNextStandartShot();
		}

		return p;
	}
	else
	{

		if (nextShots.empty())
		{
			fillNextShots(myFieldV.getHeight(), myFieldV.getWidth());
		}

		ShotPoint p = getNextHitShot();

		while (!isPossibleToBeShipThere(p, enemyFieldV))
		{
			p = getNextHitShot();	
		}

		return p;
	}
}

OptGamer::~OptGamer()
{

}
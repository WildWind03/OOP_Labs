#pragma once

struct BmpFileHeader
{
    unsigned short bfType;

    unsigned int bfSize;
    unsigned short bfReserved1;
    unsigned short bfReserved2;
    unsigned int bfOffBits;
};
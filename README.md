# Fine fractals zoom videos

### Java project to make fine fractal zoom videos

<br>

## Finebrot

Finebrot is a fine fractal, in this king of fractal we are interested in which path a point takes
during the calculation before it diverges.

During the calculation, each point (which originates at the center of a pixel) generates path,  
all these calculation paths together are what makes the Finebrot fractal and image.

A mathematician might call generated Finebrot an orbital density map of z<sup>2</sup>


## Mandelbrot

Represents Mandelbrot set, it is used to maintain interesting points for fine fractal computation.
All the interesting points are at the horizon of Mandelbrot set.


## Variables

- x, y - numbers represent screen pixels
- re, im - numbers represent real x,y coordinates (point), located in the center of a pixel


## Mandelbrot calculation

For each pixel x,y on displayed area AreaMandelbrotImpl, take point in the center of the pixel re,im
this center point (re, im) is origin (originRe,originIm) of this single calculation, **path (re<sub>i</sub>, im<sub>
i</sub>) which the calculation visits is ignored**

repeat calculation

> re -> (re * re) - (im * im) + originRe  
> im -> 2 * re * im + originIm

In confusing terminology if complex numbers, that would be

> z -> z<sup>2</sup> + c

Where c is a constant different for each calculated pixel, and z<sub>0</sub> = c<sub>0</sub>

There are humungous jumps each iteration, but all function involved is smooth.

Count how many iteration it takes, for this calculation to diverge, but count only to ITERATION_MAX.

Those which didn't diverge, color black.  
Those which did diverge before ITERATION_MAX was reached, but after ITERATION_MIN, those are the interesting points.  
Those are usually colored in various bright colors.

<br>

## Finebrot calculation

Calculation is the same

> re -> (re * re) - (im * im) + originRe  
> im -> 2 * re * im + originIm

but

we are interested in each point the calculation visits.  
That is calculation `path`, represented as `ArrayList<double[]> path`

To make an image, for each path element (re<sub>i</sub>, im<sub>i</sub>) increase value of corresponding pixel by one.


## CPU calculation

- All calculation happen in PathThread, one Thread calculates for one origin re,im
- ExecutorService uses N-1 CPU cores.


## Memory consumption

- Finebrot fractal is held in computer memory as individual calculation paths.
- With optimization, it is 5 paths for each pixel. One origin (re, im) in the center of the pixel,
  other 4 forming a square inside the pixel.
- This calculation paths are held in memory because as the zoom progresses, path elements (re, im) double
  , will move to new screen pixels (x,y) int.
- Path elements (re, im) which move out of the screen boundary are removed. But there is still lots of data in memory.
- For **full HD** video it is **recommended** to have 32GB ram.
- To save memory set RESOLUTION_MULTIPLIER = none.


## How to make Video from images with sound

- in `tools.video` is ready to use `FinebrotListOfImagesToVideoWithAudio`
- which will combine generated images with audio file
- edit constants to point to your image directory and audio file.
- Library used is `org.bytedeco.ffmpeg`


## Application overview

`Main.java`

* Starts the application and configures all the important constants to generate fractals.  
* Fractal, ColorPalette, Screen Resolution, etc.


`fine.fractals.color.`

* Contains color palettes used to color Finebrot data into image.


`fine.fractals.concurent.`

* Contains `PathThread` which performs calculation for points of MandelbrotDomain
* Elements of all calculation paths (re<sub>i</sub>, im<sub>i</sub>)<sub>n</sub> are elements of Finebrot fractal.


`fine.fractals.context.finebrot.`

* Area - defines which parts of Finebrot is displayed and calculated

* Domain - holds the calculated data `ArrayList<double[]> paths`

* Finebrot - holds the displayed screen data `int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT] elementsStaticScreen`


`fine.fractals.context.mandelbrot.`

* Area - defines domain for Finebrot calculation, all the interesting pints are at the horizon of Mandelbrot set.

* Domain - holds the Mandelbrot data `MandelbrotElement[RESOLUTION_WIDTH][RESOLUTION_HEIGHT]` which contain relevant
Mandelbrot pixel states
* These states are then important to optimize calculation

Mandelbrot - Provides access to mandelbrot data, initiates calculation for each interesting element.


`.fine.fractals.context.`

* `ApplicationImpl` - Provides aces to displayed windows, and high level application functionality, like zooming

* `FractalEngineImpl` - Organizes operations related to calculation, such as clearing and coloring data

* `TargetImpl` - represents point re,im at the center of displayed AreaFinebrot, AreaMandelbrot towards which application
zooms


`.fine.fractals.data.`

* `Mem` - represents unit of memory on which most calculations are performed.


`.fine.fractals.fractal.`

* Contains definitions of various fractals, there are not many of them as focus is on other areas of development.


`.fine.fractals.machine.`

* There are classes and methods which perform actions relevant to optimized calculation.


`.fine.fractals.perfect.coloring.PerfectColorDistributionImpl` 

* perfectly colors any set od fractal data.

* There may be huge differences in generated data each calculation iteration.  


`.fine.fractals.tools.video.ListOfImagesToVideoWithAudio` 

* contains functionality to render video with sound from generated list of Finebrot images.


## Contributions are welcomed

```
git cone https://github.com/rusty-brown/fine-fractals-zoom-videos.git
```


## Random ides to do

- Make fine fractal from Riemann zeta function and zoom into -1/12

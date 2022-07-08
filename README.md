# Fine fractals zoom videos

### Java project to make fine fractal zoom videos

## Finebrot

In fine fractals we are interested in all intermediate results for a specific calculation, for example z<sup>2</sup>  
All these intermediate results form a **calculation path**.

We calculate at least one calculation path for each pixel on the screen.

All these **calculation paths together** are what makes the **Finebrot fractal**.

A mathematician might call generated Finebrot an **orbital density map** of z<sup>2</sup>

## Mandelbrot

Represents Mandelbrot set, it is used to maintain interesting points for fine fractal computation.

All the interesting points are at the horizon of Mandelbrot set.

In Mandelbrot window, they are shown in `red` color.

## Variables

- `px`, `py` - numbers represent `int` coordinates of screen pixels, also use simple `x`, `y`
- `re`, `im` - numbers represent `double` x, y coordinates (point), located in the center of a pixel
- `m` - carrier object for effective calculation

## Mandelbrot calculation

For each pixel x,y, take point in the center of the pixel `re`,`im`.

Start calculation below, and repeat it, for at most `ITERATION_MAX` times, or until the calculation result diverges.   
(see `CALCULATION_BOUNDARY`)

> re -> (re * re) - (im * im) + originRe  
> im -> 2 * re * im + originIm

`re` and `im` varies as calculation progresses. For each pixel, `originRe`, and `originIm` are constant.

In confusing terminology if complex numbers, that would be

> z -> z<sup>2</sup> + c

Where c is a constant, and z<sub>0</sub> = c<sub>0</sub>

### To make an image

Count how many iteration it took, for the calculation to diverge.

Pixels, for which the calculation didn't diverge, that is `ITERATION_MAX` was reached, those color **black**.

Those pixels for which the calculation diverged after at least ITERATION_MIN, but before ITERATION_MAX
calculation iterations, those color with various **bright colors**.

## Finebrot calculation

Calculation is the same

> re -> (re * re) - (im * im) + originRe  
> im -> 2 * re * im + originIm

but

we are interested in each point the **calculation path**.

That is calculation `path`, represented as `ArrayList<double[]> path`

### To make an image

For each path element (re<sub>i</sub>, im<sub>i</sub>) increase value of corresponding pixel by one.

Color the resulting values by **decent colors**.

## CPUs consumption

- All calculation happen in PathThread.
- ExecutorService uses N-1 CPU cores.
- It takes minutes to generate decent image or video frame on decent CPU.
- Full zoom video or 10k resolution image may take 24h+

## Memory consumption

- Finebrot fractal is held in computer memory as individual calculation paths.
- With optimization, it is 5 paths for each pixel. One origin (re, im) in the center of the pixel,
  other 4 forming a square inside the pixel.
- This calculation paths are held in memory because as the zoom progresses, path elements (re, im) double
  , will move to new screen pixels (x,y) int.
- Path elements (re, im) which move out of the screen boundary are removed. But there is still lots of data in memory.
- For **full HD** video it is **recommended** to have 16GB ram.
- To save memory set RESOLUTION_MULTIPLIER = none.

## How to make Video from images with sound

- In `tools.video` is ready to use `ListOfImagesToVideoWithAudio`
- Which will combine generated images with audio file
- Edit constants to point to your image directory and audio file.
- Library used is `org.bytedeco.ffmpeg`

## Application overview

`fine.fractals`

Classes representing specific fractals

These classes contain the relevant equation `math()` and all the relevant variables.

Definitions of image resolution, color palette, etc.

`fine.fractals.color.`

* Contains color palettes used to perfectly color Finebrot data into image

`fine.fractals.fractal.finebrot.`

* Area - defines which parts of Finebrot is displayed and calculated

* Paths - holds all the calculation paths data `ArrayList<double[]> paths`

* Pixels - holds the `double[]` data mapped to `int[]` screen pixels

Packages `.finite.`, `.infinite.`, `.euler.`, `.phoenix.`

Contains implementations for various kinds of fractals this application can calculate.

`fine.fractals.fractal.mandelbrot.`

* Area - defines domain for Finebrot calculation, all the interesting pints are at the horizon of Mandelbrot set.

* Domain - holds the Mandelbrot data `MandelbrotElement[RESOLUTION_WIDTH][RESOLUTION_HEIGHT]`  
  Which contain relevant Mandelbrot pixel states. These states are then important to optimize calculation

`fine.fractals.machine.`

`PathCalculationThread` - Performs calculation the calculations, elements of all calculation paths (re<sub>i</sub>,
im<sub>i</sub>)<sub>n</sub> are the elements of Finebrot fractal.

* `TargetImpl` - represents point `re,im` at the center of displayed `AreaFinebrot` and `AreaMandelbrot` towards which
  application zooms

`.fine.fractals.tools.video.ListOfImagesToVideoWithAudio`

* contains functionality to render video with sound from generated list of images.

## Contributions are welcomed

```
https://github.com/rusty-brown/fine-fractals-zoom-videos.git
```

## Random ideas to do

- Make fine fractal from Riemann zeta function and zoom into -1/12
- Check distance of last few calculation iterations to optimize calculation by closing useless paths quickly.


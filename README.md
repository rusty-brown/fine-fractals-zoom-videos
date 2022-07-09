# Fine fractals zoom videos

### Java project to make fine fractal zoom videos

## Finebrot

In fine fractals we are interested in all intermediate results for a specific calculation, for example z<sup>2</sup>  
All these intermediate results form a **calculation path**.

We calculate at least one calculation path for each pixel on the screen.

All these **calculation paths together** are what makes the **Finebrot fractal**.

![Infinite Finebrot](src/main/resources/images/Finebrot.jpg)
![Infinite Finebrot](src/main/resources/images/Euler.jpg)

A mathematician might call generated Finebrot an **orbital density map**

## Mandelbrot

Represents Mandelbrot fractal, it is used to maintain interesting points for fine fractal computation.

All the interesting points are at the horizon of Mandelbrot set.

In Mandelbrot window, they are shown in `red` color.

## Variables

`px`, `py` - numbers represent `int` x,y coordinates of screen pixels

`re`, `im` - numbers represent `double` x,y coordinates (point), located in the center of a pixel

`m` - carrier object for effective calculation

## Mandelbrot calculation

For each pixel `px`,`py`, take point in the center of that pixel `re`,`im`, it is called **origin**.

Start calculation below, and repeat it, for at most `ITERATION_MAX` times, or until the calculation result diverges.   
(see `CALCULATION_BOUNDARY`)

> re -> (re * re) - (im * im) + originRe  
> im -> 2 * re * im + originIm

`re` and `im` varies as calculation progresses.
For specific pixel, `originRe`, and `originIm` are constant.

In deliberately confusing terminology if complex numbers, that would be

> z -> z<sup>2</sup> + c

Where c is a constant, which differs for every single point, and z<sub>0</sub> = c<sub>0</sub>

### To make a classic image of Mandelbrot set

Count how many iteration it took, for the calculation to diverge.

Pixels, for which the calculation didn't diverge, that is `ITERATION_MAX` was reached, those color **black**.

Those pixels for which the calculation diverged after at least `ITERATION_MIN`, but before `ITERATION_MAX`
calculation iterations, those color with various **bright colors**.

## Finebrot calculation

Calculation is the same

> re -> (re * re) - (im * im) + originRe  
> im -> 2 * re * im + originIm

![Infinite Finebrot](src/main/resources/images/Infinite-Finebrot.jpg)

but we are interested in each point of the **calculation path**.

That is calculation `path`, represented as `ArrayList<double[]> path`

### To make an image

For each path element (re<sub>i</sub>, im<sub>i</sub>) increase value of corresponding pixel by one.

Color the resulting values by **decent colors**.

## CPUs consumption

All calculation happen in PathCalculationThread.

ExecutorService uses N-1 CPU cores.

It takes minutes to generate decent image or video frame on good CPU.

Full zoom video or 10k resolution image may take 24h+

## Memory consumption

Finebrot fractal is held in computer memory as individual calculation paths.

With optimization, it is 5 paths for each pixel. One origin (re, im) in the center of the pixel,  
other 4 forming a square inside the pixel. That is optimization recommended to make frames for a video.

This calculation paths are held in memory because as the zoom progresses,   
path elements re<sub>i</sub>,im<sub>i</sub> will move to new screen pixels xp,py.

Path elements re,im which move out of the screen boundary are removed. But there is still plenty of data in memory.

For **full HD** video it is **recommended** to have at least **16GB RAM**.

To save memory set RESOLUTION_MULTIPLIER = none.

## How to make Video from images with sound

In `tools.video` is ready to use `ListOfImagesToVideoWithAudio`

Which will combine generated images with audio file to make a video.

Edit constants annotated with `@EditMe`to point to your image directory and audio file.

Library used to generate video with sound in Java is `org.bytedeco.ffmpeg`

## Application overview

`fine.fractals`

Root package contains Classes representing specific fractals.

These classes contain the relevant equation `math()` and all the relevant variables.

Definitions of image resolution, color palette, etc.

`fine.fractals.color.`

Contains color palettes used to perfectly color Finebrot data into image

`fine.fractals.fractal.finebrot.`

Area - defines which parts of Finebrot is displayed and calculated

Paths - holds all the calculation paths data `ArrayList<double[]> paths`

Pixels - holds the `double[]` data mapped to `int[]` screen pixels

Packages `.finite.`, `.infinite.`, `.euler.`, `.phoenix.`

Contains implementations for various kinds of fractals this application can calculate.

`fine.fractals.fractal.mandelbrot.`

Area - defines domain for Finebrot calculation, all the interesting pints are at the horizon of Mandelbrot set.

Pixels - holds the Mandelbrot data `MandelbrotElement[RESOLUTION_WIDTH][RESOLUTION_HEIGHT]`    
Which contain relevant Mandelbrot pixel states. These states are then important to optimize calculation.

`fine.fractals.machine.`

`PathCalculationThread` - Performs the actual calculations for `MandelbrotElements` and
puts good results (calculation paths) in `PathsFinebrot`

`TargetImpl` - represents point `re,im` at the center of displayed `AreaFinebrot` and `AreaMandelbrot`Towards this point
the application zooms

## How to use Fine fractals zoom videos

In IDE, navigate to any fine fractal in root package, for example `fine.fractals.Finebrot_Side`
and see the few constants which configure the application.

Execute `main` method.

## Contributions are welcomed

```
https://github.com/rusty-brown/fine-fractals-zoom-videos.git
```

## Random ideas to do

Make fine fractal from Riemann zeta function and zoom into -1/12

Check distance of last few calculation iterations if it is possible to better optimize calculation by closing useless
calculation paths quickly.

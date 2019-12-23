
Assume foxy is the angle from top screen to bottom screen and usually we use fovy/2

area light & soft shadow using monte carlo integration
ray max reflection of 5 times
multithreaded (parallel.for)
bounding volume for mesh only

*temporary display is available in GUI to show the render speed at different area, 
	but the color may looks very weird
	the color correction occurs when all pixels are rendered 
	so wait for the render to finish before compare the image to a reference

a4data:

all initial xml provided are kept (except metaball and 3hhhb because i didnt implemented it)

extra xml (note 
cornellq11 will cause light bounces and soft shadows and has around 1200 triangles in total
	but since computation power is limited the number of samples for shadow is pretty low, 
	and might have some imperfection in soft shadow area if 
	obj scene made with 3dmax
	it tooks me 2 hour to render this image

box480k is a box composed by 480k. 
	when rendering, rays outside of the bounding box are super fast, inside are super slow
	this scene is made for showing the acceleration of bounding box on mesh
	 (easy box model made with 3dmax)

TwoSpherePlaneAreaLight is the original TwoSpleresPlane modified with area light(small radius spheric light)

TwoSpherePlaneR is a fast mirror reflection with low complexity


